package com.spylypchuk.testbootevent.di

import androidx.room.Room
import com.spylypchuk.testbootevent.data.BootEventRepository
import com.spylypchuk.testbootevent.data.BootEventRepositoryImpl
import com.spylypchuk.testbootevent.data.local.BootEventDatabase
import com.spylypchuk.testbootevent.view.MainViewModel
import com.spylypchuk.testbootevent.work.BootNotificationWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get(), BootEventDatabase::class.java, "boot_event_database")
            .build()
    }

    single { get<BootEventDatabase>().bootEventDao() }

    worker { BootNotificationWorker(get(), get()) }
    viewModelOf(::MainViewModel)

    single<BootEventRepository> { BootEventRepositoryImpl(get()) }


}