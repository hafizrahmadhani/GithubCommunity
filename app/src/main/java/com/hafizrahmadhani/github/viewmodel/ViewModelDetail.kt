package com.hafizrahmadhani.github.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafizrahmadhani.github.BuildConfig
import com.hafizrahmadhani.github.datamodel.DataModelUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ViewModelDetail : ViewModel() {

    private val detailUser = MutableLiveData<DataModelUser>()
    private val errorCallback: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()

    fun callDetailUser(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        client.addHeader("Authorization", "token ${BuildConfig.API_KEY}")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    val items = result?.let { JSONObject(result) }

                    if (items != null) {
                        val detailApi = DataModelUser(
                            name = items.getString("name"),
                            login = items.getString("login"),
                            id = items.getInt("id"),
                            avatar_url = items.getString("avatar_url"),
                            following = items.getInt("following"),
                            followers = items.getInt("followers"),
                            followers_url = items.getString("followers_url"),
                            following_url = items.getString("following_url"),
                            repo = items.getInt("public_repos")
                        )
                        detailUser.value = detailApi
                    }
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

    fun takeDetailUser(): LiveData<DataModelUser> = detailUser

    fun takeError(): LiveData<Boolean> = errorCallback

    fun takeMsgError(): LiveData<String> = errorMsg
}