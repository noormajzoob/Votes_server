package com.vote.presentation.dto

import com.vote.domain.model.Vote
import com.vote.domain.model.VoteChoose

data class VoteDto(
    val vote: Vote,
    val chooses: List<VoteChoose>
)
