package com.androidproject.githubuserapp.api

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.androidproject.githubuserapp.model.GithubResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    private val service: GithubApiInterface

    companion object{
        const val BASE_URL = "https://api.github.com"
    }

    init {

        val retrofit: Retrofit =  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(GithubApiInterface::class.java)
    }

    suspend fun getRepositories(query: String, page: Int, perPage: Int): MutableLiveData<GithubResponse> {
        val responseLiveRepoData: MutableLiveData<GithubResponse> = MutableLiveData()
        responseLiveRepoData.value = service.searchRepositories(query, page, perPage)
        return responseLiveRepoData
    }

    suspend fun getRepositories(query: String, page: Int): MutableLiveData<GithubResponse>{
        val responseLiveRepoData: MutableLiveData<GithubResponse> = MutableLiveData()
        responseLiveRepoData.value = service.searchRepositories(query, page)
        return responseLiveRepoData
    }

    suspend fun  getRepositories(query: String) : MutableLiveData<GithubResponse>{
        val responseLiveRepoData: MutableLiveData<GithubResponse> = MutableLiveData()
        responseLiveRepoData.value = service.searchRepositories(query)
        return responseLiveRepoData
    }

}