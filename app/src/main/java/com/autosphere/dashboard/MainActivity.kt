package com.autosphere.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.autosphere.dashboard.presentation.navigation.AutoSphereMainApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as AutoSphereApplication).appContainer

        setContent {
            AutoSphereMainApp(container = appContainer)
        }
    }
}
