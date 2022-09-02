package stringart.gui.desktop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import stringart.gui.desktop.screen.image.ImageScreen
import stringart.gui.desktop.screen.image.ImageSetup
import stringart.gui.desktop.screen.settings.SettingsScreen
import stringart.gui.desktop.screen.settings.StringArtSettings
import stringart.gui.desktop.screen.stepbystep.StepByStepScreen
import stringart.gui.desktop.screen.stringart.StringArtScreen
import stringart.gui.desktop.screen.stringart.StringArtState

class AppState {
  val settings = StringArtSettings().apply {
    strokeWidth = 0.2
    connections.apply {
      height = 600
      width = 600
      count = 250
      nailsRadius = 1.0
      r = 290.0
      distanceToNails = 1.0
    }
    processor.apply {
      removeValue = 0.06
      removeStep = 0.1
      weightCalcStep = 1.0
    }
  }
  val imageSetup = ImageSetup().apply {
    filepath = "C:\\Users\\sgs08\\Pictures\\Anime-D184D18DD0BDD0B4D0BED0BCD18B-Kara-no-Kyoukai-Ryougi-Shiki-7214020.png"
  }
  val stringArtState = StringArtState().apply {
    drawCount = 4000
    updatePeriod = 500
  }
  var generator = settings.convertToGenerator()
}

@Composable
fun App() {
  println("Fucking redraw all fucking app")
  val appState = AppState()
  val navigation = Navigation(
    mapOf(
      "Settings" to @Composable { SettingsScreen(appState) },
      "Image" to @Composable { ImageScreen(appState) },
      "StringArt" to @Composable { StringArtScreen(appState) },
      "Step-by-step drawer" to @Composable { StepByStepScreen(appState) }
    )
  )
  Column {
    Row {
      navigation.Buttons()
    }
    navigation.Active()
  }
}

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "String Art Generator",
    state = rememberWindowState(width = 1000.dp, height = 700.dp)
  ) {
    App()
  }
}
