import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.jsoup.Jsoup


object BionicReader {
    private const val API_KEY = "9524d12ec6msh566e8fe138f876ep18b69cjsn490f4aabfe74"
    private const val DEFAULT_FIXATION: Int = 1
    private const val DEFAULT_SACCADE: Int = 10
    private val okHttpClient = OkHttpClient()

    fun read(
        text: String,
        fixation: Int = DEFAULT_FIXATION,
        saccade: Int = DEFAULT_SACCADE
    ): AnnotatedString =
        if (text.isEmpty())
            buildAnnotatedString {  }
        else
            post(text, fixation = fixation, saccade = saccade)
                .toHtml()
                .parseBionicWords()
                .toAnnotatedString()


    private fun List<BionicString>.toAnnotatedString(): AnnotatedString {
        return buildAnnotatedString {
            for (bionicString in this@toAnnotatedString) {
                bionicString.text.forEachIndexed { i, c ->
                    if (bionicString.isBold(i)) {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(c)
                        }
                    }
                    else {
                        append(c)
                    }
                }
                append(' ')
            }
        }
    }



    private fun Html.parseBionicWords(): List<BionicString> {
        val body = Jsoup.parse(this)

        val plainWords = body.text().split(' ', '-').toMutableList()

        val boldPartialWords = body
            .toList()
            .filter { it.hasClass("b bionic") }
            .map { it.text() }
            .toMutableList()


        return plainWords.map { plainWord ->
            val boldPartialWord = boldPartialWords.firstOrNull()
            val boldIndexRange = boldPartialWord?.let { plainWord.indexRangeOf(it) }
            if (boldIndexRange != null)
                boldPartialWords.removeFirst()
            println("$plainWord $boldPartialWord")
            BionicString(plainWord, boldIndexRange)
        }
    }


    /**
     * @param content plain text, html string, or the URL to a not password protected page
     * @param responseType You can choose between "html" and "page". "html" returns HTML code to embed,
     * while "page" returns HTML code ready to embed in your application or webpage as iframe.
     * @param requestType Currently only html supported.
     * @param fixation the expression of the letter combinations.
     * You can choose a value between 1 and 5 (1,2,3, 4 or 5);
     * @param saccade the visual jumps from Fixation to Fixation.
     * You can choose a value between 10 and 50, in steps of ten (10,20,30, 40 or 50);
     * @return OkHttp Response
     */
    private fun post(
        content: String,
        responseType: String = "html",
        requestType: String = "html",
        fixation: Int = 1,
        saccade: Int = 10
    ): Response {
        val formattedContent = content.replace(" ", "%20")

        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()

        val body = "content=$formattedContent&response_type=$responseType&request_type=$requestType&fixation=$fixation&saccade=$saccade"
            .toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://bionic-reading1.p.rapidapi.com/convert")
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("X-RapidAPI-Host", "bionic-reading1.p.rapidapi.com")
            .addHeader("X-RapidAPI-Key", API_KEY)
            .build()

        return okHttpClient.newCall(request).execute()
    }
}