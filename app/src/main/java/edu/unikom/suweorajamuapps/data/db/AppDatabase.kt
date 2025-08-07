package edu.unikom.suweorajamuapps.data.db
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.unikom.suweorajamuapps.data.dao.BarangDao
import edu.unikom.suweorajamuapps.data.dao.TransaksiDao
import edu.unikom.suweorajamuapps.data.model.Barang
import edu.unikom.suweorajamuapps.data.model.Transaksi


@Database(
    entities = [Barang::class,Transaksi::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao
    abstract fun transaksiDao(): TransaksiDao
}