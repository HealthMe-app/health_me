package com.example.healthme.repository

import androidx.lifecycle.LiveData
import com.example.healthme.data.medicine.MedicineDao
import com.example.healthme.model.Medicine

class MedicineRepository(private val medicineDao: MedicineDao) {

    val readALlData: LiveData<List<Medicine>> = medicineDao.readAllData()

    fun searchDatabase(searchQuery: String): LiveData<List<Medicine>> {
        return medicineDao.searchDatabase(searchQuery)
    }
}