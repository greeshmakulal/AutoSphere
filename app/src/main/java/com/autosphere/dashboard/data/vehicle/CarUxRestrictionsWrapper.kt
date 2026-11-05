package com.autosphere.dashboard.data.vehicle

import android.car.Car
import android.car.drivingstate.CarUxRestrictions
import android.car.drivingstate.CarUxRestrictionsManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Android Automotive OS CarUxRestrictionsManager wrapper.
 * Observes driving restrictions and informs the UI when video playback,
 * detailed text entry, or full keyboard should be restricted for safety.
 */
class CarUxRestrictionsWrapper(private val context: Context) {

    companion object {
        private const val TAG = "CarUxRestrictions"
    }

    private var car: Car? = null
    private var restrictionsManager: CarUxRestrictionsManager? = null

    private val _isDistractionOptimizationRequired = MutableStateFlow(false)
    val isDistractionOptimizationRequired: StateFlow<Boolean> =
        _isDistractionOptimizationRequired.asStateFlow()

    private val listener = CarUxRestrictionsManager.OnUxRestrictionsChangedListener { restrictions ->
        val requiresOpt = restrictions.isRequiresDistractionOptimization
        _isDistractionOptimizationRequired.value = requiresOpt
        Log.d(TAG, "AAOS UX restrictions changed: requiresDistractionOptimization=$requiresOpt")
    }

    fun startListening() {
        try {
            car = Car.createCar(context.applicationContext)
            restrictionsManager = car?.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE) as? CarUxRestrictionsManager
            restrictionsManager?.registerListener(listener)

            restrictionsManager?.currentCarUxRestrictions?.let {
                _isDistractionOptimizationRequired.value = it.isRequiresDistractionOptimization
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "UX Restrictions permission not granted: ${e.message}")
        } catch (e: Exception) {
            Log.w(TAG, "CarUxRestrictionsManager error: ${e.message}")
        }
    }

    fun stopListening() {
        try {
            restrictionsManager?.unregisterListener()
        } catch (e: Exception) {
            Log.w(TAG, "Error unregistering UX listener: ${e.message}")
        }
        try {
            car?.disconnect()
        } catch (e: Exception) {
            Log.w(TAG, "Error disconnecting Car: ${e.message}")
        }
        restrictionsManager = null
        car = null
    }
}
