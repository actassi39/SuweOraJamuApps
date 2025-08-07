package edu.unikom.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.suweorajamuapps.R
import edu.unikom.suweorajamuapps.adapter.TransaksiAdapter
import edu.unikom.suweorajamuapps.data.model.TransaksiDisplay
import edu.unikom.suweorajamuapps.databinding.FragmentTransaksiBinding
import edu.unikom.suweorajamuapps.viewmodel.TransaksiViewModel
import java.util.Collections.list

@AndroidEntryPoint
class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransaksiViewModel
    private lateinit var adapter: TransaksiAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TransaksiViewModel::class.java]
        adapter = TransaksiAdapter { transaksi ->
            val display = TransaksiDisplay(
                id = transaksi.transaksi.id,
                barangId = transaksi.barang.id,
                jumlah = transaksi.transaksi.jumlah,
                tanggal = transaksi.transaksi.tanggal
            )
            val bundle = Bundle().apply {
                putParcelable("transaksi", display)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFormFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()

        }


        //cons recyclerview
        binding.recycleviewTransaksi.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleviewTransaksi.adapter = adapter

        //Viewmodel List transaksi
        viewModel.transaksiList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())
            //empty logic
            if(list.isEmpty()){
                binding.textEmptyTransaksi.visibility = View.VISIBLE
                binding.recycleviewTransaksi.visibility = View.GONE
            }else{
                binding.textEmptyTransaksi.visibility = View.GONE
                binding.recycleviewTransaksi.visibility = View.VISIBLE


            }
            val totalBiaya = adapter.getTotalBiaya()
            val formatted = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID")).format(totalBiaya)
            binding.txtTotalBiaya.text = "Total Biaya: $formatted"
        }

        binding.btnTambahTransaksi.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFormFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        viewModel.loadTransaksi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}