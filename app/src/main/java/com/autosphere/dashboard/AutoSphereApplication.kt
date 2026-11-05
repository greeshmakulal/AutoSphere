package com.autosphere.dashboard

import android.app.Application
import com.autosphere.dashboard.di.AutoSphereAppContainer

class AutoSphereApplication : Application() {

    lateinit var appContainer: AutoSphereAppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AutoSphereAppContainer(this)
        appContainer.carUxRestrictionsWrapper.startListening()
    }

    override fun onTerminate() {
        appContainer.carPropertyManagerWrapper.disconnect()
        appContainer.carUxRestrictionsWrapper.stopListening()
        appContainer.vehicleSimulator.stopSimulation()
        super.onTerminate()
    }
}
