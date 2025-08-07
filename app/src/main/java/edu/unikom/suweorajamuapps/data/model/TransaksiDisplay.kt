package edu.unikom.suweorajamuapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TransaksiDisplay(
    val id: Int,
    val barangId: Int,
    val jumlah: Int,
    val tanggal: String,
): Parcelable{
    constructor(transaksi: Transaksi): this(
        id = transaksi.id,
        barangId = transaksi.barangId,
        jumlah = transaksi.jumlah,
        tanggal = transaksi.tanggal
    )

    fun toTransaksi(): Transaksi{
        return Transaksi(
            id = id,
            barangId = barangId,
            jumlah = jumlah,
            tanggal = tanggal)
    }
}
