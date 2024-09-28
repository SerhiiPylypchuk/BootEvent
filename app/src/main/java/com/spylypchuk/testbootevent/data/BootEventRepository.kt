package com.spylypchuk.testbootevent.data

import com.spylypchuk.testbootevent.model.BootEvent
import kotlinx.coroutines.flow.Flow

interface BootEventRepository {
    suspend fun saveBootEvent()
    suspend fun getLastBootEvent(): BootEvent?
    suspend fun getSecondLastBootEvent(): BootEvent?
    suspend fun getAllBootEvents(): Flow<List<BootEvent>>
}