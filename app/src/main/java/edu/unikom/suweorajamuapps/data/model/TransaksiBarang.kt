package edu.unikom.suweorajamuapps.data.model

import androidx.room.Embedded
import androidx.room.Relation


data class TransaksiBarang(

    @Embedded
    val transaksi: Transaksi,

    @Relation(
        parentColumn = "barangId",
        entityColumn = "id"
    )

    val barang: Barang

)
