import khttp.get
import org.json.JSONObject

class VKAnalyze(private val url: String) {
    private val access_token =
        "f73bb4e619df678f84bd0566d1e70e95b6d86bed743e128ae8fc17e4c860aa817a572de41f330ed7b4e99"

    private fun hourToSeconds(hour: Int) = hour * 60 * 60

    fun getPostsForHour(unixTime: Long, hour: Int, payload: MutableMap<String, String>): JSONObject {
        payload["start_time"] = (unixTime - hourToSeconds(hour)).toString()
        payload["end_time"] = (unixTime - hourToSeconds(hour - 1)).toString()
        return get(url, params = payload).jsonObject
    }

    fun frequencyByTag(tag: String, hours: Int): List<Int> {
        val currentUnixTime = System.currentTimeMillis() / 1000
        val payload = mutableMapOf(
            "q" to "#$tag",
            "access_token" to access_token,
            "v" to "5.124"
        )
        val frequency = ArrayList<Int>()
        for (i in 1..hours) {
            val posts = getPostsForHour(currentUnixTime, i, payload)
            val totalCount = posts.getJSONObject("response").getInt("total_count")
            frequency.add(totalCount)
        }
        return frequency
    }

}