package com.example.testui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testui.component.OptionsWidget

val options: List<Option> = listOf(
    Option(iconId = R.drawable.ic_baseline_airplanemode_active_24, title = "Airplane"),
    Option(iconId = R.drawable.ic_outline_signal_cellular_alt_24, title = "Cellular"),
    Option(iconId = R.drawable.ic_outline_wifi_24, title = "Wi-fi"),
    Option(iconId = R.drawable.ic_baseline_bluetooth_24, title = "Bluetooth"),
    Option(iconId = R.drawable.ic_baseline_developer_mode_24, title = "Developer"),
    Option(iconId = R.drawable.ic_baseline_android_24, title = "Android"),
    Option(iconId = R.drawable.ic_baseline_airplanemode_active_24, title = "Airplane"),
    Option(iconId = R.drawable.ic_outline_signal_cellular_alt_24, title = "Cellular"),
    Option(iconId = R.drawable.ic_outline_wifi_24, title = "Wi-fi"),
    Option(iconId = R.drawable.ic_baseline_bluetooth_24, title = "Bluetooth"),
    Option(iconId = R.drawable.ic_baseline_developer_mode_24, title = "Developer"),
    Option(iconId = R.drawable.ic_baseline_android_24, title = "Android")
)

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalAnimationApi::class,
        ExperimentalFoundationApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                OptionsWidget(
                    modifier = Modifier.align(Alignment.TopStart).padding(32.dp),
                    size = 150.dp,
                    options = options
                )
            }
        }
    }

}


data class Option(
    val iconId: Int,
    val title: String
)