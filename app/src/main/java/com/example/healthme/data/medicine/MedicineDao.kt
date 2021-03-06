package com.example.healthme.data.medicine

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.healthme.model.Medicine

@Dao
interface MedicineDao {
    @Query("SELECT * FROM document")
    fun readAllData(): LiveData<List<Medicine>>

    @Query("SELECT * FROM document WHERE name LIKE :searchQuery OR indication LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Medicine>>
}