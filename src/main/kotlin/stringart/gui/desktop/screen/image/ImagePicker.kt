package stringart.gui.desktop.screen.image

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun ImagePicker(initValue: String, onImagePick: (String) -> Unit) {

  var showDialog by remember { mutableStateOf(false) }
  var label by remember { mutableStateOf(initValue) }

  if (showDialog) {
    openFilepickDialog(allowedExtensions = listOf("png"),
                       onCloseRequest = {
                     showDialog = false
                     label = it.orEmpty()
                     onImagePick(label)
                   })
  }

  Row {
    Button(
      onClick = { showDialog = true },
      enabled = showDialog.not()
    ) {
      Text("Pick an image")
    }
  }
}

@Composable
private fun openFilepickDialog(
  parent: Frame? = null,
  title: String = "Pick an image",
  allowMultiSelection: Boolean = false,
  allowedExtensions: List<String>? = null,
  onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
  create = {
    object : FileDialog(parent, title, LOAD) {
      override fun setVisible(visible: Boolean) {
        if (visible) {
          this.isMultipleMode = allowMultiSelection
          if (allowedExtensions != null) {
            // windows
            this.file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

            // linux
            this.setFilenameFilter { _, name ->
              allowedExtensions.any {
                name.endsWith(it)
              }
            }
          }
        }
        super.setVisible(visible)
        if (visible) {
          onCloseRequest("$directory/$file")
        }
      }
    }
  },
  dispose = FileDialog::dispose
)