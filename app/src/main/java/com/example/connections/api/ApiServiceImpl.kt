package com.example.connections.api

import android.content.Context
import android.util.Log
import com.example.connections.R
import com.example.connections.category.CategoryModel
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.Callback
import retrofit.Response
import javax.inject.Inject
import android.widget.Toast


class ApiServiceImpl @Inject constructor() {
    fun getWords(context: Context, onSuccess: (List<CategoryModel>) -> Unit, onFail: () -> Unit, loadingFinished: () -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(
                context.getString(R.string.words_url)
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service = retrofit.create(ApiService::class.java)

        val call = service.getWords()

        call.enqueue(object: Callback<List<CategoryModel>> {
            override fun onResponse(response: Response<List<CategoryModel>>?, retrofit: Retrofit?) {
                loadingFinished();
                if(response?.isSuccess == true) {
                    val categories = response.body();
                    onSuccess(categories);
                }
                else{
                    onFailure(Exception(R.string.getting_error.toString()))
                }
            }
            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, R.string.cant_load_words, Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}

