package com.geofence.geofencing_mobile.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.geofence.geofencing_mobile.model.entities.Fence

/**
 * query all fences and by id , delete,insert and update fence in the data source
 */
@Dao
interface FenceDao {
    @Query("SELECT * FROM fence")
    suspend fun getAll(): List<Fence>

    @Query("SELECT * FROM fence WHERE id = :id")
    suspend fun getById(id: Long): Fence?

    @Insert
    suspend fun insert(fence: Fence): Long

    @Update
    suspend fun update(fence: Fence)

    @Delete
    suspend fun delete(fence: Fence)
}
