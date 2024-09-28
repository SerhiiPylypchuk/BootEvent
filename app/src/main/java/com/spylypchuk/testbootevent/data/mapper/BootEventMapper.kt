package com.spylypchuk.testbootevent.data.mapper

import com.spylypchuk.testbootevent.data.local.entity.BootEventEntity
import com.spylypchuk.testbootevent.model.BootEvent

fun BootEventEntity.toBootEvent() = BootEvent(
    id = id,
    timestamp = timestamp
)

fun BootEvent.toBootEventEntity() = BootEventEntity(
    id = id,
    timestamp = timestamp
)