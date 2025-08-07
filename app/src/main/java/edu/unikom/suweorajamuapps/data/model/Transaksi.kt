package edu.unikom.suweorajamuapps.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "Transaksi",
            foreignKeys =[
        ForeignKey(
            entity = Barang::class,
            parentColumns = ["id"],
            childColumns = ["barangId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("barangId")]
)
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val barangId: Int,
    val jumlah: Int,
    val tanggal: String,

    ) : Parcelable
