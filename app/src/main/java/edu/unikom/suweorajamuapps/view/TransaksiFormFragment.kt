package edu.unikom.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.suweorajamuapps.R
import edu.unikom.suweorajamuapps.data.model.Transaksi
import edu.unikom.suweorajamuapps.data.model.TransaksiDisplay
import edu.unikom.suweorajamuapps.databinding.FragmentTransaksiFormBinding
import edu.unikom.suweorajamuapps.util.DateTimePickerUtil
import edu.unikom.suweorajamuapps.viewmodel.TransaksiViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransaksiFormFragment : Fragment() {
    private var _binding: FragmentTransaksiFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransaksiViewModel by viewModels()

    private var transaksiDisplay: TransaksiDisplay? = null

    private var selectedBarangId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiFormBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transaksiDisplay = arguments?.getParcelable("transaksiDisplay")


        //spinner
        viewModel.barangList.observe(viewLifecycleOwner) { barangList ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                barangList.map { it.nama }
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.spinnerBarang.adapter = adapter

        //datetimepicker
        binding.edtTgl.setOnClickListener {
            DateTimePickerUtil.showDateTimePicker(requireContext()) { dateTime ->
                binding.edtTgl.setText(dateTime)
            }
        }
        transaksiDisplay?.let { transaksiItem ->
            val indext = barangList.indexOfFirst {
                it.id == transaksiItem.barangId
            }
            if (indext >= 0) {
                binding.spinnerBarang.setSelection(indext)
            }
            binding.edtJumlahBarang.setText(transaksiItem.jumlah.toString())
            binding.edtTgl.setText(transaksiItem.tanggal)
            binding.btnHapusTransaksi.visibility = View.VISIBLE
        }

        binding.btnSimpanTransaksi.setOnClickListener {
            val jumlahStr = binding.edtJumlahBarang.text.toString()
            val tanggal = binding.edtTgl.text.toString()
            val jumlah = jumlahStr.toIntOrNull()
            selectedBarangId = barangList.getOrNull(binding.spinnerBarang.selectedItemPosition)?.id

            if (selectedBarangId == null || selectedBarangId == 0) {
                Toast.makeText(requireContext(), "Pilih barang terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (jumlahStr.isBlank() || jumlah == null || jumlah <= 0) {
                Toast.makeText(requireContext(), "Jumlah barang tidak valid", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (tanggal.isBlank()) {
                Toast.makeText(requireContext(), "Tanggal tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val newTransaksi = Transaksi(
                id = transaksiDisplay?.id ?: 0,
                barangId = selectedBarangId ?: 0,
                jumlah = jumlah,
                tanggal = tanggal
            )
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSimpanTransaksi.isEnabled = false

            lifecycleScope.launch {
                if (transaksiDisplay == null) {
                    viewModel.addTransaksi(newTransaksi)
                    delay(300)
                    viewModel.loadTransaksi()
                    Toast.makeText(
                        requireContext(),
                        "Transaksi berhasil ditambahkan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.updateTransaksi(newTransaksi)
                    delay(300)
                    viewModel.loadTransaksi()
                    Toast.makeText(
                        requireContext(),
                        "Transaksi berhasil diupdate",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.progressBar.visibility = View.GONE
                binding.btnSimpanTransaksi.isEnabled = true
                parentFragmentManager.popBackStack()
            }
        }
        binding.btnHapusTransaksi.setOnClickListener {
            transaksiDisplay?.let { transaksiToDelete
                ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus Transaksi ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deleteTransaksi(transaksiToDelete.toTransaksi())
                            Toast.makeText(
                                requireContext(),
                                "Transaksi berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(300)
                            viewModel.loadTransaksi()
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }

        }
        }
        viewModel.loadBarang()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}