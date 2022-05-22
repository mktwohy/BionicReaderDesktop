import okhttp3.Response
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

typealias Html = String

@OptIn(ExperimentalStdlibApi::class)
fun Element.toList(): List<Element> =
    buildList {
        this@toList.forEach{ add(it) }
    }

fun Response.toHtml(): Html =
    this.body?.string() ?: ""