import okhttp3.Response
import org.jsoup.Connection
import org.jsoup.Jsoup

fun getHtml(url: String): String =
    Jsoup.connect(url).method(Connection.Method.GET).execute().body()

fun Response.toHtml(): String =
    this.body?.string() ?: ""