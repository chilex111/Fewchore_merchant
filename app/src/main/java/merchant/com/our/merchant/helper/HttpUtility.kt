package merchant.com.our.merchant.helper

import android.util.Log
import com.bumptech.glide.load.Key
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset

object HttpUtility {

    private val TAG = "HttpUtility"

    @Throws(IOException::class)
    fun sendPostRequest(mUrl: String, params: HashMap<String, Any?>): String {
        var `in`: InputStream? = null
        var conn: HttpURLConnection? = null
        val charset = "UTF-8"
        val sbParams: StringBuilder
        val parameters: String
        sbParams = StringBuilder()
        try {
            var i = 0
            for (key in params.keys) {
                if (i != 0) {
                    sbParams.append("&")
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params[key].toString(), charset))

                i++
            }
            parameters = sbParams.toString()
            val postData = parameters.toByteArray(Charset.forName("UTF-8"))
            val postDataLength = postData.size
            val url = URL(mUrl)
            conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.setFixedLengthStreamingMode(postDataLength)//conn.setChunkedStreamingMode(0); for unknown length
            conn.readTimeout = 70000
            conn.connectTimeout = 70000
            conn.requestMethod = "POST"

            System.setProperty("http.keepAlive", "false")
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
            /*conn.addRequestProperty("Authorization",auth);
      conn.addRequestProperty("X-Api-Key",api_Key);*/
            //conn.addRequestProperty("content-type"," multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

            val writer = DataOutputStream(conn.outputStream)
            writer.write(postData)
            val response: String
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                `in` = BufferedInputStream(conn.inputStream)
                response = readStream(`in`)
                // return readStream(in);

            } else {
                `in` = BufferedInputStream(conn.errorStream)
                response = readStream(`in`)
                // return readStream(in);
            }

            Log.i("Response", response)
            return response
        } finally {
            if (`in` != null) {
                `in`.close()
            }
            if (conn != null) {
                conn.disconnect()
            }
        }
    }

    fun sendPaystackPostRequest(mUrl: String, params: HashMap<String, Any?>): String {
        var `in`: InputStream? = null
        var conn: HttpURLConnection? = null
        val charset = "UTF-8"
        val sbParams = StringBuilder()
        val parameters: String
        try {
            var i = 0
            for (key in params.keys) {
                if (i != 0) {
                    sbParams.append("&")
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params[key].toString(), charset))

                i++
            }
            parameters = sbParams.toString()
            val postData = parameters.toByteArray(Charset.forName("UTF-8"))
            val postDataLength = postData.size
            val url = URL(mUrl)
            conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.setFixedLengthStreamingMode(postDataLength)//conn.setChunkedStreamingMode(0); for unknown length
            conn.readTimeout = 70000
            conn.connectTimeout = 70000
            conn.requestMethod = "POST"

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
            conn.addRequestProperty("Authorization", " Bearer sk_live_6lidjttvq8nddypep4h55163ikbqmqlb4mnba48f")
            // conn.addRequestProperty("Authorization", " Bearer sk_test_ce52345c3acbf2e67a9de9de2dd0f40577c9b0ad")

            /*conn.addRequestProperty("Authorization",auth);

     conn.addRequestProperty("X-Api-Key",api_Key);*/
            //conn.addRequestProperty("content-type"," multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

            val writer = DataOutputStream(conn.outputStream)
            writer.write(postData)
            val response: String
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                `in` = BufferedInputStream(conn.inputStream)
                response = readStream(`in`)
                // return readStream(in);

            } else {
                `in` = BufferedInputStream(conn.errorStream)
                response = readStream(`in`)
                // return readStream(in);
            }

            Log.i("Response", response)
            return response
        } finally {
            if (`in` != null) {
                `in`.close()
            }
            if (conn != null) {
                conn.disconnect()
            }
        }
    }

    @Throws(IOException::class)
    private fun readStream(stream: InputStream?): String {
        val bReader = BufferedReader(InputStreamReader(stream, Key.STRING_CHARSET_NAME))
        val out = StringBuilder()
        while (true) {
            val line = bReader.readLine()
            if (line != null) {
                out.append(line)
            } else {
                Log.i(TAG, "HTTP RESPONSE" + out.toString())
                return out.toString()
            }
        }
    }

    @Throws(IOException::class)
    fun getRequest(myUrl: String): String {
        var `is`: InputStream? = null
        try {
            val conn = URL(myUrl).openConnection() as HttpURLConnection
            conn.readTimeout = 40000
            conn.connectTimeout = 35000
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.connect()
            conn.responseCode
            `is` = conn.inputStream
            return readStream(`is`)
        } finally {
            if (`is` != null) {
                `is`.close()
            }
        }
    }

}