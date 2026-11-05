package com.autosphere.dashboard.repository

import com.autosphere.dashboard.data.repository.ClimateRepositoryImpl
import com.autosphere.dashboard.domain.model.ClimateState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ClimateRepositoryTest {

    private lateinit var repository: ClimateRepositoryImpl

    @Before
    fun setUp() {
        repository = ClimateRepositoryImpl()
    }

    @Test
    fun testInitialClimateState() {
        val state = repository.climateState.value
        assertEquals(21.5f, state.driverTempC, 0.01f)
        assertEquals(22.0f, state.passengerTempC, 0.01f)
        assertTrue(state.isAcOn)
        assertTrue(state.isAutoMode)
    }

    @Test
    fun testAdjustDriverTemperature() {
        repository.adjustDriverTemp(1.0f)
        assertEquals(22.5f, repository.climateState.value.driverTempC, 0.01f)

        repository.adjustDriverTemp(-1.0f)
        assertEquals(21.5f, repository.climateState.value.driverTempC, 0.01f)
    }

    @Test
    fun testTemperatureClamping() {
        repository.setDriverTemp(40.0f)
        assertEquals(ClimateState.MAX_TEMP_C, repository.climateState.value.driverTempC, 0.01f)

        repository.setDriverTemp(10.0f)
        assertEquals(ClimateState.MIN_TEMP_C, repository.climateState.value.driverTempC, 0.01f)
    }

    @Test
    fun testDualZoneSync() {
        repository.toggleDualZoneSync()
        assertTrue(repository.climateState.value.isDualZoneSynced)
        assertEquals(repository.climateState.value.driverTempC, repository.climateState.value.passengerTempC, 0.01f)

        // Adjusting driver temp when synced should update passenger temp
        repository.adjustDriverTemp(2.0f)
        assertEquals(repository.climateState.value.driverTempC, repository.climateState.value.passengerTempC, 0.01f)

        // Adjusting passenger temp directly should decouple sync
        repository.adjustPassengerTemp(1.0f)
        assertFalse(repository.climateState.value.isDualZoneSynced)
    }

    @Test
    fun testFanSpeed() {
        repository.setFanSpeed(4)
        assertEquals(4, repository.climateState.value.fanSpeed)

        // Clamping between 1 and 5
        repository.setFanSpeed(8)
        assertEquals(5, repository.climateState.value.fanSpeed)

        repository.setFanSpeed(0)
        assertEquals(1, repository.climateState.value.fanSpeed)
    }

    @Test
    fun testSeatHeatingCycle() {
        assertEquals(1, repository.climateState.value.driverSeatHeatingLevel)

        repository.cycleDriverSeatHeating()
        assertEquals(2, repository.climateState.value.driverSeatHeatingLevel)

        repository.cycleDriverSeatHeating()
        assertEquals(3, repository.climateState.value.driverSeatHeatingLevel)

        repository.cycleDriverSeatHeating()
        assertEquals(0, repository.climateState.value.driverSeatHeatingLevel)

        repository.cycleDriverSeatHeating()
        assertEquals(1, repository.climateState.value.driverSeatHeatingLevel)
    }

    @Test
    fun testToggles() {
        val ac = repository.climateState.value.isAcOn
        repository.toggleAc()
        assertEquals(!ac, repository.climateState.value.isAcOn)

        val frontDefrost = repository.climateState.value.isFrontDefrostOn
        repository.toggleFrontDefrost()
        assertEquals(!frontDefrost, repository.climateState.value.isFrontDefrostOn)

        val rearDefrost = repository.climateState.value.isRearDefrostOn
        repository.toggleRearDefrost()
        assertEquals(!rearDefrost, repository.climateState.value.isRearDefrostOn)
    }
}
