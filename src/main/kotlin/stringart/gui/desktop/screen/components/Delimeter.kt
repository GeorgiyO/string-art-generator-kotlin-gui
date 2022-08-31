package stringart.gui.desktop.screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Delimiter(caption: String) {
  Row(modifier = Modifier.fillMaxWidth().padding(5.dp).drawBehind {
    val strokeWidth = 1.5f * density
    val y = size.height - strokeWidth / 2

    drawLine(
      Color.LightGray,
      Offset(0f, y),
      Offset(size.width, y),
      strokeWidth
    )
  }) {}
  Text(
    modifier = Modifier.padding(start = 10.dp),
    text = caption,
    fontWeight = FontWeight.Bold
  )
}

@Composable
fun Delimiter() {
  Row(modifier = Modifier.fillMaxWidth().padding(5.dp).drawBehind {
    val strokeWidth = 1.5f * density
    val y = size.height - strokeWidth / 2

    drawLine(
      Color.LightGray,
      Offset(0f, y),
      Offset(size.width, y),
      strokeWidth
    )
  }) {}
}