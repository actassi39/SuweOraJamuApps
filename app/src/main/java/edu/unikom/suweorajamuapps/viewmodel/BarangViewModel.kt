package edu.unikom.suweorajamuapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.unikom.suweorajamuapps.data.model.Barang
import edu.unikom.suweorajamuapps.repository.SuweOraJamuRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarangViewModel @Inject constructor(
    private val repository: SuweOraJamuRepository
) : ViewModel() {
    private val _barangList = MutableLiveData<List<Barang>>()
    val barangList: LiveData<List<Barang>> = _barangList

    init {
        loadbarang()
    }

    fun loadbarang() {
        viewModelScope.launch {
            _barangList.value = repository.getallBarang()
        }
    }

    fun insertBarang(barang: Barang) {
        viewModelScope.launch {
            repository.insertBarang(barang)
            loadbarang()
        }
    }

    fun updateBarang(barang: Barang) {
        viewModelScope.launch {
            repository.updateBarang(barang)
            loadbarang()
        }
    }
    fun deleteBarang(barang: Barang) {
        viewModelScope.launch {
            repository.deleteBarang(barang)
            loadbarang()
        }
    }

}