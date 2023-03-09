package com.vote.presentation.dto

import com.vote.domain.model.User
data class LoginRepoDto(
    val user: User,
    val token: String
)
