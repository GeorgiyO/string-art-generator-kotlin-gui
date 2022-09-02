package stringart.gui.desktop.screen.image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import stringart.gui.desktop.AppState
import kotlin.io.path.Path

class ImageSetup {
  var filepath = ""
}

@Composable
fun ImageScreen(state: AppState) {
  var filepath by remember { mutableStateOf(state.imageSetup.filepath) }

  val imageExists = Path(filepath).toFile().extension == "png"
  val connections = state.settings.connections

  Column(modifier = Modifier.fillMaxSize()) {
    Row {
      ImagePicker(filepath) {
        filepath = it
        state.imageSetup.filepath = it
      }
      Text("Image path: ${filepath}", modifier = Modifier.padding(start = 5.dp))
    }
    if (imageExists) {
      ImageView(filepath, connections.width, connections.height) {
        Text("Image not found")
      }
    }
  }
}
