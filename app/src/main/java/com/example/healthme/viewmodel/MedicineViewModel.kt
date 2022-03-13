package com.example.healthme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.healthme.data.medicine.MedicineDatabase
import com.example.healthme.model.Medicine
import com.example.healthme.repository.MedicineRepository

class MedicineViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Medicine>>
    private val repository: MedicineRepository

    init {
        val medicineDao = MedicineDatabase.getDatabase(application).medicineDao()
        repository = MedicineRepository(medicineDao)
        readAllData = repository.readALlData
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Medicine>> {
        return repository.searchDatabase(searchQuery)
    }
}