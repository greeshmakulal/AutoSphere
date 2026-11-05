package com.autosphere.dashboard.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.autosphere.dashboard.di.AutoSphereAppContainer
import com.autosphere.dashboard.presentation.climate.ClimateControlScreen
import com.autosphere.dashboard.presentation.climate.ClimateViewModel
import com.autosphere.dashboard.presentation.components.AutomotiveNavDestination
import com.autosphere.dashboard.presentation.components.AutomotiveNavRail
import com.autosphere.dashboard.presentation.dashboard.DashboardHomeScreen
import com.autosphere.dashboard.presentation.dashboard.DashboardViewModel
import com.autosphere.dashboard.presentation.drivingmodes.DrivingModesScreen
import com.autosphere.dashboard.presentation.drivingmodes.DrivingModesViewModel
import com.autosphere.dashboard.presentation.media.MediaCenterScreen
import com.autosphere.dashboard.presentation.media.MediaViewModel
import com.autosphere.dashboard.presentation.settings.SettingsScreen
import com.autosphere.dashboard.presentation.settings.SettingsViewModel
import com.autosphere.dashboard.presentation.theme.AutoSphereTheme
import com.autosphere.dashboard.presentation.theme.DarkBackground
import com.autosphere.dashboard.presentation.vehicle.VehicleInfoScreen
import com.autosphere.dashboard.presentation.vehicle.VehicleViewModel

@Composable
fun AutoSphereMainApp(
    container: AutoSphereAppContainer,
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AutomotiveNavDestination.HOME.route

    val settingsViewModel = rememberSettingsViewModel(container)
    val userSettings by settingsViewModel.userSettings.collectAsState()

    val dashboardViewModel = rememberDashboardViewModel(container)
    val mediaViewModel = rememberMediaViewModel(container)
    val climateViewModel = rememberClimateViewModel(container)
    val vehicleViewModel = rememberVehicleViewModel(container)
    val navigationViewModel = rememberNavigationViewModel(container)
    val drivingModesViewModel = rememberDrivingModesViewModel(container)

    AutoSphereTheme(darkTheme = userSettings.isDarkMode) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            // Persistent Automotive Navigation Rail (Landscape left-docked)
            AutomotiveNavRail(
                currentRoute = currentRoute,
                onNavigate = { destination ->
                    if (currentRoute != destination.route) {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            // Dynamic Screen Content with Animated Transitions
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = AutomotiveNavDestination.HOME.route,
                    enterTransition = {
                        fadeIn(animationSpec = tween(280)) +
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                    animationSpec = tween(280)
                                )
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(280))
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(280))
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(280)) +
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                                    animationSpec = tween(280)
                                )
                    }
                ) {
                    composable(AutomotiveNavDestination.HOME.route) {
                        DashboardHomeScreen(
                            viewModel = dashboardViewModel,
                            onNavigateToMedia = {
                                navController.navigate(AutomotiveNavDestination.MEDIA.route)
                            }
                        )
                    }

                    composable(AutomotiveNavDestination.MEDIA.route) {
                        MediaCenterScreen(viewModel = mediaViewModel)
                    }

                    composable(AutomotiveNavDestination.CLIMATE.route) {
                        ClimateControlScreen(viewModel = climateViewModel)
                    }

                    composable(AutomotiveNavDestination.VEHICLE.route) {
                        VehicleInfoScreen(viewModel = vehicleViewModel)
                    }

                    composable(AutomotiveNavDestination.NAVIGATION.route) {
                        NavigationScreen(viewModel = navigationViewModel)
                    }

                    composable(AutomotiveNavDestination.MODES.route) {
                        DrivingModesScreen(viewModel = drivingModesViewModel)
                    }

                    composable(AutomotiveNavDestination.SETTINGS.route) {
                        SettingsScreen(viewModel = settingsViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberDashboardViewModel(container: AutoSphereAppContainer): DashboardViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        DashboardViewModel(
            telemetryUseCase = container.getVehicleTelemetryUseCase,
            climateUseCase = container.controlClimateUseCase,
            mediaUseCase = container.mediaPlaybackUseCase,
            settingsUseCase = container.manageSettingsUseCase
        )
    }
}

@Composable
private fun rememberMediaViewModel(container: AutoSphereAppContainer): MediaViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        MediaViewModel(container.mediaPlaybackUseCase)
    }
}

@Composable
private fun rememberClimateViewModel(container: AutoSphereAppContainer): ClimateViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        ClimateViewModel(container.controlClimateUseCase, container.manageSettingsUseCase)
    }
}

@Composable
private fun rememberVehicleViewModel(container: AutoSphereAppContainer): VehicleViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        VehicleViewModel(container.getVehicleTelemetryUseCase, container.manageSettingsUseCase)
    }
}

@Composable
private fun rememberNavigationViewModel(container: AutoSphereAppContainer): NavigationViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        NavigationViewModel(container.navigationRepository)
    }
}

@Composable
private fun rememberDrivingModesViewModel(container: AutoSphereAppContainer): DrivingModesViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        DrivingModesViewModel(container.getVehicleTelemetryUseCase)
    }
}

@Composable
private fun rememberSettingsViewModel(container: AutoSphereAppContainer): SettingsViewModel {
    return androidx.lifecycle.viewmodel.compose.viewModel {
        SettingsViewModel(container.manageSettingsUseCase, container.getVehicleTelemetryUseCase)
    }
}
