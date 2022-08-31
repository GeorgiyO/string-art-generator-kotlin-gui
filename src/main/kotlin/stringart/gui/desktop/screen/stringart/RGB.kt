package stringart.gui.desktop.screen.stringart

class RGB(private var r: Int,
          private var g: Int,
          private var b: Int) {

  fun asArray(): IntArray {
    return intArrayOf(r, g, b)
  }

  fun grayscale(): Double {
    return (r + g + b) /
      765.0
  }

  fun inverse() {
    r = 255 - r
    g = 255 - g
    b = 255 - b
  }

  fun toPlain(): Int {
    return ((r shl 16) + (g shl 8) + b)
  }

  companion object {
    fun ofPlain(plainRgb: Int): RGB {
      return RGB(
        r = plainRgb and R_MASK shr 16,
        g = plainRgb and G_MASK shr 8,
        b = plainRgb and B_MASK
      )
    }

    private val R_MASK = getMask(24) xor getMask(16)
    private val G_MASK = getMask(16) xor getMask(8)
    private val B_MASK = getMask(8)
    private fun getMask(offset: Int): Int {
      return (2 shl offset - 1) - 1
    }
  }
}