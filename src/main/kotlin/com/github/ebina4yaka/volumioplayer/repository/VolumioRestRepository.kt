package com.github.ebina4yaka.volumioplayer.repository

import com.github.ebina4yaka.volumioplayer.entity.VolumioState
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class VolumioRestRepository (volumioHost: String = "http://volumio.local") {
    private val endpoint = "$volumioHost/api/v1/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(endpoint)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    interface Volumio {
        @GET("getState")
        fun getState(): Call<VolumioState>
    }

    private val volumio = retrofit.create(Volumio::class.java)

    fun fetchState(): Call<VolumioState> {
        return volumio.getState()
    }
}