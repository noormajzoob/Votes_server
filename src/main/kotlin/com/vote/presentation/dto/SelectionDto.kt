package com.vote.presentation.dto

import com.vote.domain.model.User
import com.vote.domain.model.VoteChoose

data class SelectionDto(
    val user: User,
    val choose: VoteChoose
)
