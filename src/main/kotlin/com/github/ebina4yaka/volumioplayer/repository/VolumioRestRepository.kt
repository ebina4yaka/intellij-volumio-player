package com.github.ebina4yaka.volumioplayer.repository

import com.github.ebina4yaka.volumioplayer.entity.VolumioCommandResult
import com.github.ebina4yaka.volumioplayer.entity.VolumioState
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
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

        @GET("commands?cmd=toggle")
        fun toggle(): Call<VolumioCommandResult>

        @GET("commands?cmd=next")
        fun next(): Call<VolumioCommandResult>

        @GET("commands?cmd=prev")
        fun prev(): Call<VolumioCommandResult>
    }

    private val volumio = retrofit.create(Volumio::class.java)

    fun fetchState(): Response<VolumioState> {
        return volumio.getState().execute()
    }

    fun toggle(): Response<VolumioCommandResult> {
        return volumio.toggle().execute()
    }

    fun next(): Response<VolumioCommandResult> {
        return volumio.next().execute()
    }

    fun prev(): Response<VolumioCommandResult> {
        return volumio.prev().execute()
    }
}