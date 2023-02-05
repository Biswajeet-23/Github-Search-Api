package com.androidproject.githubuserapp.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavRepoDao {

    @Insert
    suspend fun insertProduct(product : FavRepoModel)

    @Delete
    suspend fun deleteProduct(product : FavRepoModel)

    @Query("SELECT * FROM repos")
    fun getAllProducts() : LiveData<List<FavRepoModel>>

    @Query("SELECT * FROM repos WHERE id = :repoId")
    fun isExist(repoId: Int?) : FavRepoModel
}