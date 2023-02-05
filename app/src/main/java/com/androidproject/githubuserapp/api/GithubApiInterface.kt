package com.androidproject.githubuserapp.api

import com.androidproject.githubuserapp.model.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val GET_REPOS: String = "search/repositories"

interface GithubApiInterface {

    @Headers(
        "Accept: application/json"
    )
    @GET(GET_REPOS)
    fun searchRepositories(@Query("q") q: String): Call<GithubResponse>

    @GET(GET_REPOS)
    fun searchRepositories(
        @Query("q") q: String,
        @Query("per_page") perPage: Int
    ): Call<GithubResponse>

    @GET(GET_REPOS)
    fun searchRepositories(
        @Query("q") searchQuery: String,
        @Query("page") pageIndex: Int,
        @Query("per_page") perPage: Int
    ): Call<GithubResponse>
}