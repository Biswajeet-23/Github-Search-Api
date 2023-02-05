package com.androidproject.githubuserapp.service

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.androidproject.githubuserapp.api.GithubApiInterface
import com.androidproject.githubuserapp.api.RetrofitInstance
import com.androidproject.githubuserapp.model.GithubResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoService {

    fun getRepository(query: String, page: Int, perPage: Int) : MutableLiveData<GithubResponse> {

        val responseLiveRepoData: MutableLiveData<GithubResponse> = MutableLiveData()
                val repoService = RetrofitInstance.getInstance().create(GithubApiInterface::class.java)
                repoService.searchRepositories(query, page, perPage)
                    .enqueue(object : Callback<GithubResponse> {
                        override fun onResponse(
                            call: Call<GithubResponse>,
                            response: Response<GithubResponse>
                        ) {
                            if (response.code() == 200) {
                                Log.d(TAG, "Response: ${response.body()}")
                                responseLiveRepoData.value = response.body() as GithubResponse
                            } else {
                                Log.d(TAG, "Error: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                            Log.e(TAG, "Failed to load")
                        }

                    })

        return responseLiveRepoData
    }

    fun getRepository(query: String) : MutableLiveData<GithubResponse> {

        val responseLiveRepoData: MutableLiveData<GithubResponse> = MutableLiveData()

        val repoService = RetrofitInstance.getInstance().create(GithubApiInterface::class.java)
        repoService.searchRepositories(query)
            .enqueue(object : Callback<GithubResponse>{
                override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                    if(response.code() == 200){
                        Log.d(TAG, "Response: ${response.body()}")
                        responseLiveRepoData.value = response.body() as GithubResponse
                    } else {
                        Log.d(TAG, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.e(TAG, "Failed to load")
                }

            })
        return responseLiveRepoData
    }

}