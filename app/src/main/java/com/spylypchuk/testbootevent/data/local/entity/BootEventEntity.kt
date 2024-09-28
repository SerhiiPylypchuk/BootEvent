package com.spylypchuk.testbootevent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.format.DateTimeFormat

@Entity(tableName = "boot_event")
data class BootEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long, // Store boot event time as a long (Unix time)
)

//TODO - add dismiss counter
