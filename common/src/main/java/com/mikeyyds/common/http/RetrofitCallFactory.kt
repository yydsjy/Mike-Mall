package com.mikeyyds.common.http

import com.mikeyyds.library.restful.*
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.lang.IllegalStateException

class RetrofitCallFactory(val baseUrl: String) : MikeCall.Factory {


    private var mikeConvert: MikeConvert
    private var apiService: ApiService

    init {
        var retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()

        apiService = retrofit.create(ApiService::class.java)
        mikeConvert = GsonConvert()
    }

    override fun newCall(request: MikeRequest): MikeCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(val request: MikeRequest) : MikeCall<T> {
        override fun execute(): MikeResponse<T> {
            var realCall = createRealCall(request)
            var response = realCall.execute()
            return parseResponse(response)
        }

        private fun parseResponse(response: Response<ResponseBody>): MikeResponse<T> {
            var rawData:String? = null
            if (response.isSuccessful){
                val body = response.body()
                if (body!=null){
                    rawData = body.string()
                }
            } else {
                val body = response.errorBody()
                if (body!=null){
                    rawData = body.string()
                }
            }
            return mikeConvert.convert(rawData!!,request.returnType!!)
        }

        override fun enqueue(callback: MikeCallback<T>) {
            val realCall = createRealCall(request)
            realCall.enqueue(object :Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailed(throwable = t)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val response = parseResponse(response)
                    callback.onSuccess(response)
                }
            })
        }

        private fun createRealCall(request: MikeRequest): Call<ResponseBody> {
            if (request.httpMethod == MikeRequest.METHOD.GET) {
                return apiService.get(request.headers, request.endPointUrl(), request.parameters)
            } else if (request.httpMethod == MikeRequest.METHOD.POST) {
                val parameters = request.parameters
                var builder = FormBody.Builder()
                var requestBody: RequestBody? = null
                var jsonObject = JSONObject()

                for ((key, value) in parameters!!) {
                    if (request.formPost) {
                        builder.add(key, value)
                    } else {
                        jsonObject.put(key, value)
                    }
                }

                if (request.formPost) {
                    requestBody = builder.build()
                } else {
                    requestBody = RequestBody.create(
                        MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString()
                    )
                }
                return apiService.post(request.headers, request.endPointUrl(), requestBody)
            } else{
                throw  IllegalStateException("MikeRestful only support GET POST for now, url = ${request.endPointUrl()}")
            }
        }

    }

    interface ApiService {
        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>
    }
}