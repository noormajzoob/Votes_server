package com.vote.presentation.routes

import com.vote.domain.dao.VoteDao
import com.vote.domain.model.SelectedVote
import com.vote.domain.model.Vote
import com.vote.domain.model.VoteChoose
import com.vote.presentation.dto.VoteDto
import com.vote.presentation.endpoints.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.voteRoute(){
    route("/vote"){
        authenticate("auth"){
            getRoute { id ->
                VoteDao.getVote(id)
            }

            getAllRoute(route = "/user/{id}/list"){ offset, limit, id ->
                id?.run {
                    VoteDao.getUserVotes(this, offset, limit)
                }
            }

            postRoute<VoteDto> { body ->
                val vote = VoteDao.postVote(
                    userId = body.vote.user!!,
                    title = body.vote.title!!,
                    duration = body.vote.duration!!
                )

                if (vote != null){
                    val list = mutableListOf<VoteChoose>()
                    body.chooses.forEach {
                        val choose = VoteDao.postVoteChoose(
                            voteId = vote.id!!,
                            content = it.content!!
                        )
                        if (choose != null) list.add(choose)
                    }
                    VoteDto(
                        vote = vote,
                        chooses = list
                    )
                }else null
            }

            deleteRoute { id ->
                if (VoteDao.deleteVote(id)){
                    "vote has been deleted"
                }else null
            }

            putRoute<Vote> { id, data ->
                if (VoteDao.putVote(id, data.title!!, data.status!!, data.duration!!))
                    "vote has been updated"
                else null
            }

            putRoute<Any>(route = "/{id}/active"){ id, data ->
                if (VoteDao.setActive(id))
                    "vote activated"
                else null
            }

            putRoute<Any>(route = "/{id}/inactive"){ id, data ->
                if (VoteDao.setInActive(id))
                    "vote deactivated"
                else null
            }

            postRoute<SelectedVote>(route = "/selection"){ body ->
                VoteDao.postVoteChooseSelection(
                    userId = body.user!!,
                    chooseId = body.choose!!
                )
            }

            getRoute(route = "/{id}/count"){ id ->
                VoteDao.getVotesCount(id)
            }

            getRoute(route = "/user/{id}/active/count"){ id ->
                VoteDao.getActiveVotesCount(id)
            }

            getAllRoute(route = "/selection/{id}") { offset, limit, id ->
                id?.let {
                    VoteDao.getVotesChooseSelections(it, offset, limit)
                }?: throw IllegalArgumentException()
            }

            get(""){
                try {
                    val uuid = call.request.queryParameters["id"]!!
                    val vote = VoteDao.getVoteByUUID(uuid)

                    if (vote != null){
                        VoteDao.incrementView(vote.vote.id!!, vote.vote.views!! + 1)

                        call.respond(HttpStatusCode.OK, vote)
                    }else call.respond(HttpStatusCode.NotFound)

                }catch (e: Exception){
                    e.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
