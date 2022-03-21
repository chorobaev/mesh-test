package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.flaterlab.meshexam.data.database.entity.host.HostingEntity

@Dao
internal interface HostingDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vararg hostings: HostingEntity)

    @Query("SELECT * FROM hostings WHERE hostingId = :id")
    suspend fun getHostingById(id: String): HostingEntity

    @Query("SELECT * FROM hostings ORDER BY startedAt DESC")
    suspend fun getAll(): List<HostingEntity>
}