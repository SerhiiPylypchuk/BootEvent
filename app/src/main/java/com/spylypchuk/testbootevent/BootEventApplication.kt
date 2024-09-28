package com.spylypchuk.testbootevent

import android.app.Application
import com.spylypchuk.testbootevent.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BootEventApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BootEventApplication)
            modules(appModule)
        }
    }
}