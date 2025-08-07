package edu.unikom.suweorajamuapps.repository

import edu.unikom.suweorajamuapps.data.dao.BarangDao
import edu.unikom.suweorajamuapps.data.dao.TransaksiDao
import edu.unikom.suweorajamuapps.data.model.Barang
import edu.unikom.suweorajamuapps.data.model.Transaksi
import edu.unikom.suweorajamuapps.data.model.TransaksiBarang
import javax.inject.Inject

class SuweOraJamuRepository @Inject constructor(
    private val barangDao: BarangDao,
    private val transaksiDao: TransaksiDao

) {
    suspend fun insertBarang(Barang: Barang) = barangDao.insertBarang(Barang)
    suspend fun updateBarang(Barang: Barang) = barangDao.updateBarang(Barang)
    suspend fun deleteBarang(Barang: Barang) = barangDao.deleteBarang(Barang)
    suspend fun getallBarang() = barangDao.getallBarang()

    suspend fun insertTransaksi(Transaksi: Transaksi) = transaksiDao.insertTransaksi(Transaksi)
    suspend fun updateTransaksi(Transaksi: Transaksi) = transaksiDao.updateTransaksi(Transaksi)
    suspend fun deleteTransaksi(Transaksi: Transaksi) = transaksiDao.deleteTransaksi(Transaksi)
    suspend fun getAllTransaksiWithBarang(): List<TransaksiBarang> =
        transaksiDao.getAllTransaksiWithBarang()

}