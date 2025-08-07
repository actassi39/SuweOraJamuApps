package edu.unikom.suweorajamuapps.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.unikom.suweorajamuapps.data.dao.BarangDao
import edu.unikom.suweorajamuapps.data.dao.TransaksiDao
import edu.unikom.suweorajamuapps.data.db.AppDatabase
import edu.unikom.suweorajamuapps.repository.SuweOraJamuRepository
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "suweorajamuapps_db"
        ).build()
    }

    @Provides
    fun provideBarangDao(db: AppDatabase): BarangDao = db.barangDao()


    @Provides
    fun provideTransaksiDao(db: AppDatabase): TransaksiDao = db.transaksiDao()

    fun provideRepository(
        barangDao: BarangDao,
        transaksiDao: TransaksiDao)
    : SuweOraJamuRepository {
        return SuweOraJamuRepository(barangDao,transaksiDao)
    }
}