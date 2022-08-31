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
import stringart.gui.desktop.screen.stringart.StringArtScreen
import stringart.gui.desktop.screen.stringart.StringArtState

class AppState {
  val settings = StringArtSettings().apply {
    strokeWidth = 0.1
    connections.apply {
      height = 1000
      width = 1000
      count = 200
      nailsRadius = 2.0
      r = 490.0
      distanceToNails = 3.0
    }
    processor.apply {
      removeValue = 0.1
      removeStep = 0.5
      weightCalcStep = 3.0
    }
  }
  val imageSetup = ImageSetup()
  val stringArtState = StringArtState()
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
      "StringArt" to @Composable { StringArtScreen(appState) }
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
