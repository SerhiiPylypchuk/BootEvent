package com.spylypchuk.testbootevent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spylypchuk.testbootevent.data.local.entity.BootEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BootEventDao {
    @Insert
    suspend fun insertBootEvent(bootEvent: BootEventEntity)

    @Query("SELECT * FROM boot_event ORDER BY timestamp DESC")
    fun getAllBootEvents(): Flow<List<BootEventEntity>>

    @Query("SELECT * FROM boot_event ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastBootEvent(): BootEventEntity?

    @Query("SELECT * FROM boot_event ORDER BY timestamp DESC LIMIT 1 OFFSET 1")
    suspend fun getSecondLastBootEvent(): BootEventEntity?
}