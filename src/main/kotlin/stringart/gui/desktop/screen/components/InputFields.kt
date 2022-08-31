package stringart.gui.desktop.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DoubleInput(caption: String, initValue: Double, onInput: (Double) -> Unit) {
  Input(
    caption,
    initValue.toString(),
    onInput = {
      val num = it.toDoubleOrNull()
      if (num != null) {
        onInput(num)
        true
      } else {
        false
      }
    }
  )
}

@Composable
fun IntInput(caption: String, initValue: Int, onInput: (Int) -> Unit) {
  Input(
    caption,
    initValue.toString(),
    onInput = {
      val num = it.toIntOrNull()
      if (num != null) {
        onInput(num)
        true
      } else {
        false
      }
    }
  )
}

@Composable
fun Input(caption: String, initValue: String, onInput: (String) -> Boolean) {
  var prevValue by remember { mutableStateOf(initValue) }
  var value by remember { mutableStateOf(initValue) }
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      modifier = Modifier.padding(end = 10.dp),
      text = caption
    )
    TextField(
      value = value,
      modifier = Modifier.width(250.dp),
      onValueChange = {
        if (onInput(it)) {
          value = it
          prevValue = it
        } else {
          value = prevValue
        }
      })
  }
}