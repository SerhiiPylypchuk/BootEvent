package com.spylypchuk.testbootevent.data

import com.spylypchuk.testbootevent.data.local.dao.BootEventDao
import com.spylypchuk.testbootevent.data.local.entity.BootEventEntity
import com.spylypchuk.testbootevent.data.mapper.toBootEvent
import com.spylypchuk.testbootevent.model.BootEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BootEventRepositoryImpl(
    private val bootEventDao: BootEventDao,
): BootEventRepository {
    override suspend fun saveBootEvent() {
        bootEventDao.insertBootEvent(BootEventEntity(timestamp = System.currentTimeMillis()))
    }

    override suspend fun getLastBootEvent(): BootEvent? {
        return bootEventDao.getLastBootEvent()?.toBootEvent()
    }

    override suspend fun getSecondLastBootEvent(): BootEvent? {
        return bootEventDao.getSecondLastBootEvent()?.toBootEvent()
    }

    override suspend fun getAllBootEvents(): Flow<List<BootEvent>> {
        return bootEventDao.getAllBootEvents().map { it.map { it.toBootEvent() } }
    }
}