import androidx.compose.ui.text.AnnotatedString
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
