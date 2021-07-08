package com.hafizrahmadhani.github

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class ViewModelFollowing : ViewModel() {

    private val listFollowing = MutableLiveData<ArrayList<DataModelRv>>()
    private val errorCallback: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()

    fun callFollowing(username: String) {
        val userApi = ArrayList<DataModelRv>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token 829a457cada2f0ee5761b31049d6a8828c3df988")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val followersApi = items.getJSONObject(i)
                        userApi.add(
                            DataModelRv(
                                avatar_url = followersApi.getString("avatar_url"),
                                login = followersApi.getString("login")
                            )
                        )
                    }
                    listFollowing.value = userApi

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                errorCallback.value = true
                errorMsg.value = errorMessage
            }

        })
    }

    fun takeFollowing(): LiveData<ArrayList<DataModelRv>> = listFollowing

    fun takeError(): LiveData<Boolean> = errorCallback

    fun takeMsgError(): LiveData<String> = errorMsg
}