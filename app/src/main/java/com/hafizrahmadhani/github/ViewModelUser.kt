package com.hafizrahmadhani.github

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class ViewModelUser : ViewModel(){

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val userList = MutableLiveData<ArrayList<DataModelRv>>()
    private val errorCallback = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()

    fun callUser(username : String){
        errorCallback.value = false

        val userApi = ArrayList<DataModelRv>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"

        client.addHeader("Authorization", "token 829a457cada2f0ee5761b31049d6a8828c3df988")
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try{
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()){
                        val jsonObj = items.getJSONObject(i)
                        userApi.add(
                            DataModelRv(
                                avatar_url = jsonObj.getString("avatar_url"),
                                login = jsonObj.getString("login")
                            )
                        )

                    }
                    userList.value = userApi
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                errorCallback.value = true
                errorMsg.value = errorMessage
            }
        })
    }

    fun takeUser() : LiveData<ArrayList<DataModelRv>> = userList

    fun takeError() : LiveData<Boolean> = errorCallback

    fun takeMsgError() : LiveData<String> = errorMsg
}