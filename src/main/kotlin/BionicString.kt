class BionicString(val text: String, boldRange: IntRange? = null) {
    val boldRange = boldRange?.let {
        boldRange.first.coerceAtLeast(0)..boldRange.last.coerceAtMost(text.lastIndex)
    }

    fun isBold(index: Int): Boolean =
        boldRange?.let { index in it } ?: false

}