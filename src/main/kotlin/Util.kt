import java.util.*

fun <T> List<T>.toQueue(): Queue<T> {
    val queue: Queue<T> = LinkedList()
    queue.addAll(this)
    return queue
}

fun String.nonAlphaNumericCharPositions(): List<Pair<Int, Char>> =
    this.mapIndexed { i, c ->
        if (!c.isLetterOrDigit()) i to c else null
    }.filterNotNull()


fun String.indexRangeOf(subString: String): IntRange? {
    val start = this.indexOf(subString)

    return if (start != -1)
        start until start + subString.length
    else
        null
}