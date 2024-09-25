package com.example.connections.api
import com.example.connections.category.CategoryModel
import retrofit.Call
import retrofit.http.GET

interface ApiService {

    @GET("data")
    fun getWords(): Call<List<CategoryModel>>

}