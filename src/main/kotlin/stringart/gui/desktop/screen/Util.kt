package stringart.gui.desktop.screen

import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.res.loadImageBitmap
import stringart.gui.desktop.screen.stringart.RGB
import java.awt.Image
import java.awt.image.BufferedImage
import kotlin.io.path.Path

fun BufferedImage.resized(width: Int, height: Int): BufferedImage {
  println("<top>.resized")
  val scaled = this.getScaledInstance(width, height, Image.SCALE_SMOOTH)
  val scaledBuff = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
  scaledBuff.graphics.drawImage(scaled, 0, 0, null)
  return scaledBuff
}

fun BufferedImage.savingResized(width: Int, height: Int): BufferedImage {
  println("<top>.savingResized")
  return __resize(width, height, Math::min) { a, b -> a > b }
}

fun BufferedImage.croppingResized(width: Int, height: Int): BufferedImage {
  println("<top>.croppingResized")
  return __resize(width, height, Math::max) { a, b -> a < b }
}

private fun BufferedImage.__resize(
  width: Int,
  height: Int,
  mainRatioFunction: (Double, Double) -> Double,
  conditionPredicate: (Double, Double) -> Boolean
): BufferedImage {
  val ws = this.width
  val hs = this.height
  val x1: Int
  val x2: Int
  val y1: Int
  val y2: Int
  val wRatio = width.toDouble() / ws.toDouble()
  val hRatio = height.toDouble() / hs.toDouble()
  val mainRatio = mainRatioFunction(wRatio, hRatio)
  if (conditionPredicate(wRatio, hRatio)) {
    val wp: Double = width / mainRatio
    y1 = 0
    y2 = hs
    x1 = (ws - wp).toInt() / 2
    x2 = (ws - x1)
  } else {
    val hp: Double = height / mainRatio
    x1 = 0
    x2 = ws
    y1 = (hs - hp).toInt() / 2
    y2 = (hs - y1)
  }
  return move(
    0, 0, width, height,
    x1, y1, x2, y2
  )
}

private fun BufferedImage.move(
  dx1: Int, dy1: Int, dx2: Int, dy2: Int,
  sx1: Int, sy1: Int, sx2: Int, sy2: Int
): BufferedImage {
  val scaledImg = BufferedImage(dx2 - dx1, dy2 - dy1, BufferedImage.TYPE_INT_RGB)
  scaledImg.graphics.drawImage(
    this,
    dx1, dy1, dx2, dy2,
    sx1, sy1, sx2, sy2,
    null
  )
  return scaledImg
}

fun convertImageToDoubleArray(path: String, width: Int, height: Int): Array<DoubleArray> {
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