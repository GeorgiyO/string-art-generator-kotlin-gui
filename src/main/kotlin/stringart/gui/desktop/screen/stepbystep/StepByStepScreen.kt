package stringart.gui.desktop.screen.stepbystep

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import nekogochan.stringart.generator.nail.Nail
import nekogochan.stringart.math.linesegment.LineSegment
import nekogochan.stringart.math.point.Point
import stringart.gui.desktop.AppState
import stringart.gui.desktop.screen.components.IntInput
import stringart.gui.desktop.screen.stringart.StringArtImage
import java.awt.Color
import java.util.Comparator.comparingDouble
import java.util.stream.StreamSupport.stream as stream

@Composable
fun StepByStepScreen(state: AppState) {
  println("<top>.StepByStepScreen")
  val generator = state.generator
  val segments = state.stringArtState.segments
  val currSegments = remember { mutableStateListOf<LineSegment>() }
  var valueForSetPos by remember { mutableStateOf(0) }

  fun setPos(newPos: Int) {
    println("set val new pos: $newPos")
    if (newPos < 0) {
      return
    }
    val currPos = currSegments.size
    println("set val curr pos: $currPos")
    val diff = newPos - currPos
    if (diff > 0) {
      val segmentsToAdd = segments.subList(currPos, minOf(newPos, segments.size))
      currSegments.addAll(segmentsToAdd)
    } else {
      currSegments.removeRange(newPos, currSegments.size)
    }
  }

  fun goNext() {
    setPos(currSegments.size + 1)
  }

  fun goBack() {
    setPos(currSegments.size - 1)
  }

  data class ClosestNails(val a: Nail?, val b: Nail?);
  fun closestNails(seg: LineSegment?): ClosestNails {

    if (seg == null) {
      return ClosestNails(null, null)
    }

    fun closestNail(point: Point): Nail? {
      return stream(generator.nails.spliterator(), false)
        .max(comparingDouble { it.distanceTo(point) })
        .orElse(null)
    }

    return ClosestNails(
      closestNail(seg.a),
      closestNail(seg.b)
    )
  }

  val (fromNail, toNail) = closestNails(currSegments.lastOrNull())

  Row {
    Column(modifier = Modifier.width(350.dp).padding(15.dp)) {
      Column(
        modifier = Modifier.padding(bottom = 15.dp),
      ) {
        @Composable
        fun RichText(text: String) {
          Text(text = text, fontSize = 1.5.em)
        }
        RichText("Current segments count: ${currSegments.size}")
        if (fromNail != null) {
          RichText("From: ${fromNail.idx}")
        }
        if (toNail != null) {
          RichText("To: ${toNail.idx}")
        }
      }
      IntInput("set pos", valueForSetPos) { println(it); valueForSetPos = it }
      Button(onClick = { valueForSetPos.let(::println); setPos(valueForSetPos) }) {
        Text("Set current pos!")
      }
      Button(onClick = { goNext() }) {
        Text("Go next")
      }
      Button(onClick = { goBack() }) {
        Text("Go back")
      }
    }
    StringArtImage(
      segments = currSegments,
      nails = generator.nails,
      width = state.settings.connections.width,
      height = state.settings.connections.height,
      strokeWidth = state.settings.strokeWidth,
      lastLineColor = Color.RED
    )
  }
}