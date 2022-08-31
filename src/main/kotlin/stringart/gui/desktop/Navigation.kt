package stringart.gui.desktop

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

typealias Frame = @Composable () -> Unit

class Navigation(private val entries: Map<String, Frame>) {

  var active by mutableStateOf(entries.values.iterator().next())
    private set

  @Composable
  fun Buttons() {
    entries.forEach {
      val (k, v) = it
      Button(
        onClick = {
          active = v
        }) {
        Text(text = k)
      }
    }
  }

  @Composable
  fun Active() {
    active()
  }
}