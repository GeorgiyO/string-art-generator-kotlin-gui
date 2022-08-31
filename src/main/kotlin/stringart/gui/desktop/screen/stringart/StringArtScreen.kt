package stringart.gui.desktop.screen.stringart

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import nekogochan.stringart.math.linesegment.LineSegment
import stringart.gui.desktop.AppState
import stringart.gui.desktop.screen.components.IntInput
import stringart.gui.desktop.screen.croppingResized
import java.util.stream.Stream
import kotlin.io.path.Path
import kotlin.streams.toList

class StringArtState {
  val segments: MutableList<LineSegment> = mutableListOf()
  var updatePeriod: Int = 100
  var drawCount: Int = 1000
}

@Composable
fun StringArtScreen(state: AppState) {
  println("<top>.StringArtScreen")
  val connections = state.settings.connections
  val generator = state.generator

  fun resetGeneratorData() {
    generator.data = convertImageToDoubleArray(state.imageSetup.filepath, connections.width, connections.height)
  }
  resetGeneratorData()

  var updatePeriod by mutableStateOf(state.stringArtState.updatePeriod)
  var drawCount by mutableStateOf(state.stringArtState.drawCount)
  val segments = remember { mutableStateListOf(*state.stringArtState.segments.toTypedArray())}

  DisposableEffect(null) {
    onDispose {
      println("fuck call on dispose")
      state.stringArtState.apply {
        this.segments.clear()
        this.segments.addAll(segments)
        this.updatePeriod = updatePeriod
        this.drawCount = drawCount
        println("segments ${segments.size}")
        println("updatePeriod $updatePeriod")
        println("drawCount $drawCount")
      }
    }
  }

  Row {
    Column(
      modifier = Modifier.width(400.dp).padding(10.dp)
    ) {
      Text(
        modifier = Modifier.padding(bottom = 15.dp),
        fontSize = 1.5.em,
        text = "Total lines drown: ${segments.size}"
      )
      Column {
        IntInput("Update period", updatePeriod) { updatePeriod = it }
        IntInput("Draw count", drawCount) { drawCount = it }
      }
      Button(onClick = {
        Thread {
          iteratePartially(
            totalWeight = drawCount,
            weightPerIter = updatePeriod
          ) { count ->
            println("SUKA $count")
            Stream.generate(generator::proceed)
              .limit(count.toLong())
              .toList()
              .let(segments::addAll)
            Thread.sleep(50)
          }
        }.start()
      }) {
        Text("Draw")
      }
      Button(onClick = {
        resetGeneratorData()
        segments.clear()
      }) {
        Text("Reset")
      }
    }
    StringArtImage(
      segments = segments,
      nails = generator.nails,
      width = connections.width,
      height = connections.height,
      strokeWidth = state.settings.strokeWidth
    )
  }
}

private fun convertImageToDoubleArray(path: String, width: Int, height: Int): Array<DoubleArray> {
  val file = Path(path).toFile()
  if (file.exists().not()) {
    return arrayOf()
  }
  val image = loadImageBitmap(file.inputStream()).toAwtImage().croppingResized(width, height)
  return Array(image.width) { x ->
    DoubleArray(image.height) { y ->
      1.0 - RGB.ofPlain(image.getRGB(x, y)).grayscale()
    }
  }
}

private fun iteratePartially(totalWeight: Int, weightPerIter: Int, fn: (Int) -> Unit) {
  var i = totalWeight
  while (i > weightPerIter) {
    fn(weightPerIter)
    i -= weightPerIter
  }
  fn(weightPerIter)
}