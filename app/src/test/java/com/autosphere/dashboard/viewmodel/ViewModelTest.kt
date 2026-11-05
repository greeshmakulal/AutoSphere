package com.autosphere.dashboard.viewmodel

import com.autosphere.dashboard.data.mock.VehicleSimulator
import com.autosphere.dashboard.data.repository.ClimateRepositoryImpl
import com.autosphere.dashboard.data.repository.MediaRepositoryImpl
import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.domain.model.UserSettings
import com.autosphere.dashboard.domain.repository.SettingsRepository
import com.autosphere.dashboard.domain.repository.VehicleRepository
import com.autosphere.dashboard.domain.usecase.ControlClimateUseCase
import com.autosphere.dashboard.domain.usecase.GetVehicleTelemetryUseCase
import com.autosphere.dashboard.domain.usecase.ManageSettingsUseCase
import com.autosphere.dashboard.domain.usecase.MediaPlaybackUseCase
import com.autosphere.dashboard.presentation.dashboard.DashboardViewModel
import com.autosphere.dashboard.presentation.drivingmodes.DrivingModesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var simulator: VehicleSimulator
    private lateinit var vehicleRepo: FakeVehicleRepository
    private lateinit var climateRepo: ClimateRepositoryImpl
    private lateinit var mediaRepo: MediaRepositoryImpl
    private lateinit var settingsRepo: FakeSettingsRepository

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var drivingModesViewModel: DrivingModesViewModel

    @Before
    fun setUp() {
        simulator = VehicleSimulator(testScope)
        simulator.stopSimulation()
        vehicleRepo = FakeVehicleRepository(simulator)
        climateRepo = ClimateRepositoryImpl()
        mediaRepo = MediaRepositoryImpl(testScope)
        settingsRepo = FakeSettingsRepository()

        val telemetryUseCase = GetVehicleTelemetryUseCase(vehicleRepo)
        val climateUseCase = ControlClimateUseCase(climateRepo)
        val mediaUseCase = MediaPlaybackUseCase(mediaRepo)
        val settingsUseCase = ManageSettingsUseCase(settingsRepo)

        dashboardViewModel = DashboardViewModel(
            telemetryUseCase,
            climateUseCase,
            mediaUseCase,
            settingsUseCase
        )

        drivingModesViewModel = DrivingModesViewModel(telemetryUseCase)
    }

    @Test
    fun testDashboardGearSelection() {
        dashboardViewModel.selectGear(Gear.PARK)
        assertEquals(Gear.PARK, dashboardViewModel.vehicleState.value.gear)

        dashboardViewModel.selectGear(Gear.DRIVE)
        assertEquals(Gear.DRIVE, dashboardViewModel.vehicleState.value.gear)
    }

    @Test
    fun testDashboardToggles() {
        dashboardViewModel.toggleHeadlights()
        assertFalse(dashboardViewModel.vehicleState.value.isHeadlightsOn)

        dashboardViewModel.toggleDoorLocks()
        assertFalse(dashboardViewModel.vehicleState.value.isDoorsLocked)
    }

    @Test
    fun testDrivingModeSelection() {
        drivingModesViewModel.selectDrivingMode(DrivingMode.SPORT)
        assertEquals(DrivingMode.SPORT, drivingModesViewModel.vehicleState.value.drivingMode)

        drivingModesViewModel.selectDrivingMode(DrivingMode.ECO)
        assertEquals(DrivingMode.ECO, drivingModesViewModel.vehicleState.value.drivingMode)
    }

    @Test
    fun testClimateAdjustmentViaDashboardViewModel() {
        val initialDriverTemp = dashboardViewModel.climateState.value.driverTempC
        dashboardViewModel.adjustDriverTemp(1.0f)
        assertEquals(initialDriverTemp + 1.0f, dashboardViewModel.climateState.value.driverTempC, 0.01f)
    }

    private class FakeVehicleRepository(private val sim: VehicleSimulator) : VehicleRepository {
        override val vehicleState = sim.vehicleState
        override fun setGear(gear: Gear) = sim.setGear(gear)
        override fun toggleHeadlights() = sim.toggleHeadlights()
        override fun toggleDoorLocks() = sim.toggleDoorLocks()
        override fun toggleParkingBrake() = sim.toggleParkingBrake()
        override fun setDrivingMode(mode: DrivingMode) = sim.setDrivingMode(mode)
        override fun setDemoMode(enabled: Boolean) = sim.setDemoMode(enabled)
        override fun setSimulatedSpeed(speedKmh: Float) = sim.setSimulatedSpeed(speedKmh)
        override fun setCharging(isCharging: Boolean) = sim.setCharging(isCharging)
    }

    private class FakeSettingsRepository : SettingsRepository {
        private val _settings = MutableStateFlow(UserSettings())
        override val userSettings: StateFlow<UserSettings> = _settings.asStateFlow()

        override suspend fun updateDarkMode(isDark: Boolean) {
            _settings.value = _settings.value.copy(isDarkMode = isDark)
        }
        override suspend fun updateUseFahrenheit(useF: Boolean) {
            _settings.value = _settings.value.copy(useFahrenheit = useF)
        }
        override suspend fun updateUseMph(useMph: Boolean) {
            _settings.value = _settings.value.copy(useMph = useMph)
        }
        override suspend fun updateBrightness(brightness: Int) {
            _settings.value = _settings.value.copy(displayBrightnessPercent = brightness)
        }
        override suspend fun updateVolume(volume: Int) {
            _settings.value = _settings.value.copy(masterVolumePercent = volume)
        }
        override suspend fun updateSoundProfile(profile: SoundProfile) {
            _settings.value = _settings.value.copy(soundProfile = profile)
        }
        override suspend fun updateBluetooth(enabled: Boolean) {
            _settings.value = _settings.value.copy(isBluetoothEnabled = enabled)
        }
        override suspend fun updateWifi(enabled: Boolean) {
            _settings.value = _settings.value.copy(isWifiEnabled = enabled)
        }
        override suspend fun updateHotspot(enabled: Boolean) {
            _settings.value = _settings.value.copy(isHotspotEnabled = enabled)
        }
        override suspend fun updateDemoMode(enabled: Boolean) {
            _settings.value = _settings.value.copy(isSimulatorDemoMode = enabled)
        }
    }
}
