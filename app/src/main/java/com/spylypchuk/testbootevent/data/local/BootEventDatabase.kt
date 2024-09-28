package com.spylypchuk.testbootevent.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spylypchuk.testbootevent.data.local.dao.BootEventDao
import com.spylypchuk.testbootevent.data.local.entity.BootEventEntity

@Database(entities = [BootEventEntity::class], version = 1)
abstract class BootEventDatabase : RoomDatabase() {
    abstract fun bootEventDao(): BootEventDao
}