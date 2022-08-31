package stringart.gui.desktop.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nekogochan.stringart.generator.StringArtGenerator
import nekogochan.stringart.generator.nail.connections.connections.CircleConnections
import nekogochan.stringart.generator.nail.connections.layout.CircleLayout
import nekogochan.stringart.generator.processor.Processor
import nekogochan.stringart.math.circle.Circle
import nekogochan.stringart.math.point.Point
import stringart.gui.desktop.AppState
import stringart.gui.desktop.screen.components.Delimiter
import stringart.gui.desktop.screen.components.DoubleInput
import stringart.gui.desktop.screen.components.IntInput


class StringArtSettings {
  var strokeWidth: Double = 0.0
  val processor = ProcessorSettings()
  val connections = ConnectionsSettings()

  class ProcessorSettings {
    var weightCalcStep = 0.0
    var removeStep = 0.0
    var removeValue = 0.0
  }

  class ConnectionsSettings {
    var distanceToNails = 0.0
    var count = 0
    var nailsRadius = 0.0
    var r = 0.0
    var width = 1000
    var height = 1000
  }

  fun convertToGenerator(): StringArtGenerator {
    return StringArtGenerator(
      Processor(
        processor.weightCalcStep,
        processor.removeStep,
        processor.removeValue
      ),
      CircleConnections(connections.distanceToNails).connect(
        CircleLayout(
          Circle(
            Point(
              (connections.width / 2).toDouble(),
              (connections.height / 2).toDouble()
            ),
            connections.r
          ),
          connections.count,
          connections.nailsRadius
        )
      )
    )
  }
}

@Composable
fun SettingsScreen(state: AppState) {
  println("<top>.SettingsScreen")
  val processor = state.settings.processor
  val connections = state.settings.connections
  var changed by mutableStateOf(false)

  DisposableEffect(null) {
    onDispose {
      if (changed) {
        state.generator = state.settings.convertToGenerator()
      }
    }
  }

  Column {
    Box(modifier = Modifier.width(600.dp).padding(10.dp)) {
      DoubleInput("Stroke width", state.settings.strokeWidth) {
        state.settings.strokeWidth = it
        changed = true
      }
    }
    Delimiter("Processor")
    Column(modifier = Modifier.width(600.dp).padding(10.dp)) {
      DoubleInput("Weight calc step", processor.weightCalcStep) {
        processor.weightCalcStep = it
        changed = true
      }
      DoubleInput("Remove step", processor.removeStep) {
        processor.removeStep = it
        changed = true
      }
      DoubleInput("Remova value", processor.removeValue) {
        processor.removeValue = it
        changed = true
      }
    }
    Delimiter("Connections")
    Column(modifier = Modifier.width(600.dp).padding(10.dp)) {
      DoubleInput("distanceToNails", connections.distanceToNails) {
        connections.distanceToNails = it
        changed = true
      }
      IntInput("count", connections.count) {
        connections.count = it
        changed = true
      }
      DoubleInput("nailsRadius", connections.nailsRadius) {
        connections.nailsRadius = it
        changed = true
      }
      DoubleInput("r", connections.r) {
        connections.r = it
        changed = true
      }
      IntInput("width", connections.width) {
        connections.width = it
        changed = true
      }
      IntInput("height", connections.height) {
        connections.height = it
        changed = true
      }
    }
  }
}