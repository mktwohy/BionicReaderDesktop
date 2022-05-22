import okhttp3.Response
import org.jsoup.Connection
import org.jsoup.Jsoup

typealias Html = String


fun getHtml(url: String): Html =
    Jsoup.connect(url).method(Connection.Method.GET).execute().body()

fun Response.toHtml(): Html =
    this.body?.string() ?: ""