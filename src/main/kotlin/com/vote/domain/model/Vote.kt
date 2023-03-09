package com.vote.domain.model


data class Vote(
    val id: Long?,
    val user: Long?,
    val title: String?,
    val timestamp: Long?,
    val duration: Int?,
    val views: Int?,
    val urlId: String?,
    val status: Boolean?,
)
