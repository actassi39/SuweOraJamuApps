package edu.unikom.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.suweorajamuapps.R
import edu.unikom.suweorajamuapps.adapter.BarangAdapter
import edu.unikom.suweorajamuapps.databinding.FragmentBarangBinding
import edu.unikom.suweorajamuapps.viewmodel.BarangViewModel

@AndroidEntryPoint
class BarangFragment : Fragment() {

    private var _binding: FragmentBarangBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BarangViewModel
    private lateinit var adapter: BarangAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BarangViewModel::class.java]

        adapter = BarangAdapter { barang ->
            val bundle = Bundle().apply {
                putParcelable("barang", barang)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BarangForm::class.java, bundle)
                .addToBackStack(null)
                .commit()

        }
        binding.recycleviewBarang.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleviewBarang.adapter = adapter
        viewModel.barangList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)

            if (list.isNullOrEmpty()) {
                binding.recycleviewBarang.visibility = View.GONE
                binding.textEmptyState.visibility = View.VISIBLE
            }
            else{
                binding.textEmptyState.visibility = View.GONE
                binding.recycleviewBarang.visibility = View.VISIBLE
            }
        }


        binding.btnTambahBarang.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BarangForm::class.java, null)
                .addToBackStack(null)
                .commit()
        }
        viewModel.loadbarang()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}