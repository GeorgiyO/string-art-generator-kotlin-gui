package stringart.gui.desktop.screen.image

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import org.jetbrains.skia.Bitmap
import org.jetbrains.skiko.toImage
import stringart.gui.desktop.screen.croppingResized
import stringart.gui.desktop.screen.savingResized
import java.awt.image.BufferedImage
import kotlin.io.path.Path

@Composable
fun ImageView(img: BufferedImage) {
  var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }
  val dsize = minOf(
    canvasSize.width / img.width,
    canvasSize.height / img.height
  )

  Canvas(
    modifier = Modifier.fillMaxSize().onSizeChanged { canvasSize = it.toSize() }
  ) {
    this.drawContext.canvas.scale(dsize, dsize)
    val bitmap = Bitmap.makeFromImage(img.toImage())
    drawImage(bitmap.asComposeImageBitmap())
  }
}

@Composable
fun ImageView(src: String, imageWidth: Int, imageHeight: Int, orElseShow: @Composable (() -> Unit)) {
  var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }
  var imageSize by remember { mutableStateOf(Size(0f, 0f)) }
  val dsize = minOf(
    canvasSize.width / imageSize.width,
    canvasSize.height / imageSize.height
  )

  val file = Path(src).toFile()
  val imageExists = file.extension == "png"

  if (imageExists) {
    Canvas(
      modifier = Modifier.fillMaxSize().onSizeChanged { canvasSize = it.toSize() },
    ) {
      drawIntoCanvas { canvas ->
        canvas.scale(dsize, dsize)
        canvas.withSave {
          val bitmap = Bitmap.makeFromImage(
            loadImageBitmap(file.inputStream())
              .toAwtImage()
              .croppingResized(imageWidth, imageHeight)
              .toImage()
          ).asComposeImageBitmap()
          imageSize = IntSize(
            width = bitmap.width,
            height = bitmap.height
          ).toSize()
          canvas.drawImage(bitmap, Offset.Zero, Paint())
        }
      }
    }
  } else {
    orElseShow()
  }
}
