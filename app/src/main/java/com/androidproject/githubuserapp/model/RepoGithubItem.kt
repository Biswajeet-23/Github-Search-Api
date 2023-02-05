package com.androidproject.githubuserapp.model

data class RepoGithub(
    val id: Number,
    val html_url: String,
    val url: String,
    val owner: Owner,
    val full_name: String,
    val name: String,
    val description: String,
    val size: Int,
    val stargazers_count: Int,
    val watchers_count: Int,
    val forks_count: Int,
    val subscribers_count: Int,
    val open_issues_count: Int,
    val network_count: Int
)

data class Owner(
    val id : Number,
    val avatar_url: String,
    val login: String,
    val followers_url: String,
    val following_url: String,
    val repos_url: String
)
