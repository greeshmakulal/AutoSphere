package com.autosphere.dashboard.mock

import com.autosphere.dashboard.data.mock.VehicleSimulator
import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.Gear
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleSimulatorTest {

    private lateinit var simulator: VehicleSimulator

    @Before
    fun setUp() {
        simulator = VehicleSimulator()
        simulator.stopSimulation() // Stop background loop for deterministic tests
    }

    @Test
    fun testInitialVehicleState() {
        val state = simulator.vehicleState.value
        assertEquals(Gear.DRIVE, state.gear)
        assertTrue(state.batteryPercent in 0..100)
        assertTrue(state.estimatedRangeKm > 0)
        assertTrue(state.isHeadlightsOn)
        assertTrue(state.isDoorsLocked)
    }

    @Test
    fun testGearChangeLogic() {
        // Shift to Park
        simulator.setGear(Gear.PARK)
        assertEquals(Gear.PARK, simulator.vehicleState.value.gear)
        assertEquals(0f, simulator.vehicleState.value.speedKmh)
        assertTrue(simulator.vehicleState.value.isParkingBrakeEngaged)

        // Shift to Drive
        simulator.setGear(Gear.DRIVE)
        assertEquals(Gear.DRIVE, simulator.vehicleState.value.gear)
        assertFalse(simulator.vehicleState.value.isParkingBrakeEngaged)
    }

    @Test
    fun testToggleControls() {
        val initialLights = simulator.vehicleState.value.isHeadlightsOn
        simulator.toggleHeadlights()
        assertEquals(!initialLights, simulator.vehicleState.value.isHeadlightsOn)

        val initialLocks = simulator.vehicleState.value.isDoorsLocked
        simulator.toggleDoorLocks()
        assertEquals(!initialLocks, simulator.vehicleState.value.isDoorsLocked)

        simulator.toggleParkingBrake()
        assertTrue(simulator.vehicleState.value.isParkingBrakeEngaged)
        assertEquals(0f, simulator.vehicleState.value.speedKmh)
    }

    @Test
    fun testDrivingModeSelection() {
        simulator.setDrivingMode(DrivingMode.ECO)
        assertEquals(DrivingMode.ECO, simulator.vehicleState.value.drivingMode)

        val ecoRange = simulator.vehicleState.value.estimatedRangeKm

        simulator.setDrivingMode(DrivingMode.SPORT)
        assertEquals(DrivingMode.SPORT, simulator.vehicleState.value.drivingMode)
        val sportRange = simulator.vehicleState.value.estimatedRangeKm

        // Eco range should be greater than Sport range
        assertTrue("Eco range ($ecoRange) should exceed Sport range ($sportRange)", ecoRange > sportRange)
    }

    @Test
    fun testChargingMode() {
        simulator.setCharging(true)
        val state = simulator.vehicleState.value
        assertTrue(state.isCharging)
        assertEquals(150.0f, state.chargingPowerKw, 0.01f)
        assertEquals(Gear.PARK, state.gear)
        assertEquals(0f, state.speedKmh)

        simulator.setCharging(false)
        assertFalse(simulator.vehicleState.value.isCharging)
        assertEquals(0f, simulator.vehicleState.value.chargingPowerKw)
    }

    @Test
    fun testSimulatedSpeedSetting() {
        simulator.setSimulatedSpeed(88.0f)
        assertEquals(88.0f, simulator.vehicleState.value.speedKmh, 0.01f)
        assertEquals(88.0f * 76.5f, simulator.vehicleState.value.rpm, 0.1f)
    }
}
