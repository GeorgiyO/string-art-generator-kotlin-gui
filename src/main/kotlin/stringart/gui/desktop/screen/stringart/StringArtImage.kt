package stringart.gui.desktop.screen.stringart

import androidx.compose.runtime.*
import nekogochan.stringart.generator.nail.Nail
import nekogochan.stringart.graphics.StringArtGraphicsExtension
import nekogochan.stringart.math.linesegment.LineSegment
import stringart.gui.desktop.screen.image.ImageView
import stringart.gui.desktop.screen.resized
import stringart.gui.desktop.screen.stringart.StrokeSizeApplyRule.AlphaChannel
import stringart.gui.desktop.screen.stringart.StrokeSizeApplyRule.ResizeImage
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.stream.StreamSupport.stream
import kotlin.streams.toList

enum class StrokeSizeApplyRule {
  ResizeImage,
  AlphaChannel
}

@Composable
fun StringArtImage(
  segments: Iterable<LineSegment>, nails: Iterable<Nail>,
  width: Int, height: Int,
  strokeWidth: Double,
  lastLineColor: Color? = null,
  sizeRule: StrokeSizeApplyRule = AlphaChannel
) {
  when (sizeRule) {
    ResizeImage -> {
      val multiplier = 1.0 / strokeWidth
      val img = BufferedImage(
        (width * multiplier).toInt(),
        (height * multiplier).toInt(),
        BufferedImage.TYPE_4BYTE_ABGR
      )
      val gx = StringArtGraphicsExtension(img)
      gx.fill(Color.WHITE)
      gx.color = Color.BLACK

      val _segments = stream(segments.spliterator(), false)
        .map {
          LineSegment(
            it.a.clone().multiply(multiplier),
            it.b.clone().multiply(multiplier)
          )
        }
        .toList()

      val _nails = stream(nails.spliterator(), false)
        .map {
          Nail(
            it.idx,
            it.center.clone().multiply(multiplier),
            it.r * multiplier
          )
        }
        .toList()

      gx.setStroke(multiplier.toFloat())
      _nails.forEach(gx::draw)
      gx.setStroke(1.0f)
      if (lastLineColor != null) {
        _segments.forEachExpectLast(gx::draw)
        gx.draw(_segments.last(), lastLineColor)
      } else {
        _segments.forEach(gx::draw)
      }

      ImageView(img.resized(width, height))
    }
    AlphaChannel -> {
      val img = BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR)
      val gx = StringArtGraphicsExtension(img)
      gx.fill(Color.WHITE)
      gx.color = Color(0f, 0f, 0f, strokeWidth.toFloat())
      gx.setStroke(1f)

      nails.forEach { gx.draw(it, Color.BLACK) }
      gx.setStroke(1.0f)
      if (lastLineColor != null) {
        val _segments = segments.toList()
        _segments.forEachExpectLast(gx::draw)
        val last = _segments.lastOrNull()
        if (last != null) {
          gx.draw(last, lastLineColor)
        }
      } else {
        segments.forEach(gx::draw)
      }

      ImageView(img)
    }
  }
}


private fun <T> List<T>.forEachExpectLast(fn: (T) -> Unit) {
  for (i in 0 until size - 1) {
    fn(get(i))
  }
}