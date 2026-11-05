package com.autosphere.dashboard.di

import android.content.Context
import com.autosphere.dashboard.data.local.AutoSphereDatabase
import com.autosphere.dashboard.data.mock.VehicleSimulator
import com.autosphere.dashboard.data.preferences.UserSettingsDataStore
import com.autosphere.dashboard.data.repository.ClimateRepositoryImpl
import com.autosphere.dashboard.data.repository.MediaRepositoryImpl
import com.autosphere.dashboard.data.repository.NavigationRepositoryImpl
import com.autosphere.dashboard.data.repository.SettingsRepositoryImpl
import com.autosphere.dashboard.data.repository.VehicleRepositoryImpl
import com.autosphere.dashboard.data.vehicle.CarPropertyManagerWrapper
import com.autosphere.dashboard.data.vehicle.CarUxRestrictionsWrapper
import com.autosphere.dashboard.domain.repository.ClimateRepository
import com.autosphere.dashboard.domain.repository.MediaRepository
import com.autosphere.dashboard.domain.repository.NavigationRepository
import com.autosphere.dashboard.domain.repository.SettingsRepository
import com.autosphere.dashboard.domain.repository.VehicleRepository
import com.autosphere.dashboard.domain.usecase.ControlClimateUseCase
import com.autosphere.dashboard.domain.usecase.GetVehicleTelemetryUseCase
import com.autosphere.dashboard.domain.usecase.ManageSettingsUseCase
import com.autosphere.dashboard.domain.usecase.MediaPlaybackUseCase

/**
 * Dependency Injection Container for AutoSphere.
 * Provides lazy singletons for Room Database, DataStore, AAOS wrappers,
 * simulated telemetry sources, repositories, and use cases.
 */
class AutoSphereAppContainer(private val context: Context) {

    val database: AutoSphereDatabase by lazy {
        AutoSphereDatabase.getInstance(context)
    }

    val userSettingsDataStore: UserSettingsDataStore by lazy {
        UserSettingsDataStore(context)
    }

    val vehicleSimulator: VehicleSimulator by lazy {
        VehicleSimulator()
    }

    val carPropertyManagerWrapper: CarPropertyManagerWrapper by lazy {
        CarPropertyManagerWrapper(context)
    }

    val carUxRestrictionsWrapper: CarUxRestrictionsWrapper by lazy {
        CarUxRestrictionsWrapper(context)
    }

    val vehicleRepository: VehicleRepository by lazy {
        VehicleRepositoryImpl(vehicleSimulator, carPropertyManagerWrapper)
    }

    val climateRepository: ClimateRepository by lazy {
        ClimateRepositoryImpl()
    }

    val mediaRepository: MediaRepository by lazy {
        MediaRepositoryImpl()
    }

    val navigationRepository: NavigationRepository by lazy {
        NavigationRepositoryImpl()
    }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(userSettingsDataStore)
    }

    // Use cases
    val getVehicleTelemetryUseCase: GetVehicleTelemetryUseCase by lazy {
        GetVehicleTelemetryUseCase(vehicleRepository)
    }

    val controlClimateUseCase: ControlClimateUseCase by lazy {
        ControlClimateUseCase(climateRepository)
    }

    val mediaPlaybackUseCase: MediaPlaybackUseCase by lazy {
        MediaPlaybackUseCase(mediaRepository)
    }

    val manageSettingsUseCase: ManageSettingsUseCase by lazy {
        ManageSettingsUseCase(settingsRepository)
    }
}
