package com.androidproject.githubuserapp.model

data class GithubResponse(
    val incomplete_results: Boolean,
    val items: ArrayList<RepoGithub>,
    val total_count: Int
)
