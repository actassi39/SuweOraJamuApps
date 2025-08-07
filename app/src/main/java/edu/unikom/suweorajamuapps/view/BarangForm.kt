package edu.unikom.suweorajamuapps.view

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.suweorajamuapps.R
import edu.unikom.suweorajamuapps.data.model.Barang
import edu.unikom.suweorajamuapps.databinding.FragmentBarangFormBinding
import edu.unikom.suweorajamuapps.util.ImageUtils.copyImageToInternalStorage
import edu.unikom.suweorajamuapps.viewmodel.BarangViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class BarangForm : Fragment() {
    private var _binding: FragmentBarangFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarangViewModel by viewModels()

    private var barang: Barang? = null
    private var imguri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val safeUri = copyImageToInternalStorage(requireContext(), uri)
                safeUri?.let { newUri ->
                    imguri = newUri
                    binding.imgPreview.setImageURI(imguri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangFormBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barang = arguments?.getParcelable("barang")

        barang?.let {
            binding.edtNama.setText(it.nama)
            binding.edtHarga.setText(it.harga.toString())
            imguri = Uri.parse(it.imguri)
            val file = File(imguri?.path ?: "")
            if (file.exists()) {
                binding.imgPreview.setImageURI(imguri)
            } else {
                binding.imgPreview.setImageResource(R.drawable.ic_launcher_foreground)
            }
            binding.btnHapus.visibility = View.VISIBLE

        }
        binding.imgPreview.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.btnImg.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSimpan.setOnClickListener {
            val nama = binding.edtNama.text.toString()
            val harga = binding.edtHarga.text.toString().toDoubleOrNull()
            val imageUriString = imguri?.toString() ?: ""
            if (nama.isBlank() || harga == null || imageUriString == null) {
                Toast.makeText(
                    requireContext(),
                    "Isi semua field dan juga gambarnya",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSimpan.isEnabled = false
            lifecycleScope.launch {
                val barangBaru = Barang(
                    id = barang?.id ?: 0,
                    nama = nama,
                    harga = harga,
                    imguri = imguri.toString()
                )

                if (barang == null) {
                    viewModel.insertBarang(barangBaru)
                    Toast.makeText(
                        requireContext(),
                        "Barang berhasil ditambahkan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.updateBarang(barangBaru)
                    Toast.makeText(
                        requireContext(),
                        "Barang berhasil diperbaharui",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                delay(500)
                binding.progressBar.visibility = View.GONE
                binding.btnSimpan.isEnabled = true
                parentFragmentManager.popBackStack()

            }

        }
        binding.btnHapus.setOnClickListener {
            barang?.let {
                AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menghapus barang ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deleteBarang(it)
                            Toast.makeText(
                                requireContext(),
                                "Barang berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        parentFragmentManager.popBackStack()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}