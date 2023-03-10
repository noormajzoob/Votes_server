package com.vote.domain.dao

import com.vote.data.DatabaseFactory.dbQuery
import com.vote.data.schema.SelectedVoteSchema
import com.vote.data.schema.UserSchema
import com.vote.data.schema.VoteChoseSchema
import com.vote.data.schema.VoteSchema
import com.vote.domain.model.SelectedVote
import com.vote.domain.model.User
import com.vote.domain.model.Vote
import com.vote.domain.model.VoteChoose
import com.vote.presentation.dto.SelectionDto
import com.vote.presentation.dto.VoteDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object VoteDao {

    suspend fun getVote(id: Long): VoteDto? = dbQuery{
        (VoteSchema innerJoin VoteChoseSchema).select {
            VoteSchema.id eq id
        }.map {
            Vote(
                id = it[VoteSchema.id],
                user = it[VoteSchema.createdBy],
                title = it[VoteSchema.title],
                timestamp = it[VoteSchema.timestamp],
                duration = it[VoteSchema.duration],
                urlId = it[VoteSchema.uuid],
                status = it[VoteSchema.status],
                views = it[VoteSchema.views],
            ) to VoteChoose(
                id = it[VoteChoseSchema.id],
                vote = it[VoteChoseSchema.targetVote],
                content = it[VoteChoseSchema.content],
            )
        }.groupBy {
            it.first
        }.map {
            VoteDto(
                vote = it.key,
                chooses = it.value.map {
                    it.second
                }
            )
        }.singleOrNull()
    }

    suspend fun postVote(userId: Long, title: String, duration: Int): Vote? = dbQuery {
        VoteSchema.insert { table ->
            table[createdBy] = userId
            table[VoteSchema.title] = title
            table[VoteSchema.duration] = duration
        }.resultedValues?.map {
            Vote(
                id = it[VoteSchema.id],
                user = it[VoteSchema.createdBy],
                title = it[VoteSchema.title],
                timestamp = it[VoteSchema.timestamp],
                duration = it[VoteSchema.duration],
                urlId = it[VoteSchema.uuid],
                views = it[VoteSchema.views],
                status = it[VoteSchema.status]
            )
        }?.firstOrNull()
    }

    suspend fun postVoteChoose(voteId: Long, content: String): VoteChoose? = dbQuery {
        VoteChoseSchema.insert { table ->
            table[targetVote] = voteId
            table[VoteChoseSchema.content] = content
        }.resultedValues?.map {
            VoteChoose(
                id = it[VoteChoseSchema.id],
                vote = it[VoteChoseSchema.targetVote],
                content = it[VoteChoseSchema.content],
            )
        }?.singleOrNull()
    }

    suspend fun postVoteChooseSelection(userId: Long, chooseId: Long): SelectedVote? = dbQuery{
        SelectedVoteSchema.insert { table ->
            table[voteChosen] = chooseId
            table[user] = userId
        }.resultedValues?.map {
            SelectedVote(
                id = it[SelectedVoteSchema.id],
                choose = it[SelectedVoteSchema.voteChosen],
                user = it[SelectedVoteSchema.user],
                timestamp = it[SelectedVoteSchema.timestamp]
            )
        }?.singleOrNull()
    }

    suspend fun deleteVote(id: Long): Boolean = dbQuery {
        VoteSchema.deleteWhere {
            VoteSchema.id eq id
        } > 0
    }

    suspend fun putVote(id: Long, title: String, status: Boolean, duration: Int): Boolean = dbQuery{
        VoteSchema.update(
            where = {
                VoteSchema.id eq id
            },
            body = { table ->
                table[VoteSchema.status] = status
                table[VoteSchema.duration] = duration
                table[VoteSchema.title] = title
            }
        ) > 0
    }

    suspend fun setActive(id: Long): Boolean = dbQuery {
        VoteSchema.update(
            where = {
                VoteSchema.id eq id
            },
            body = { table ->
                table[VoteSchema.status] = true
            }
        ) > 0
    }

    suspend fun setInActive(id: Long): Boolean = dbQuery {
        VoteSchema.update(
            where = {
                VoteSchema.id eq id
            },
            body = { table ->
                table[VoteSchema.status] = false
            }
        ) > 0
    }

    suspend fun getVoteByUUID(uuid: String): VoteDto? = dbQuery {
        (VoteSchema innerJoin VoteChoseSchema).select {
            VoteSchema.uuid eq uuid
        }.map {
            Vote(
                id = it[VoteSchema.id],
                user = it[VoteSchema.createdBy],
                title = it[VoteSchema.title],
                timestamp = it[VoteSchema.timestamp],
                duration = it[VoteSchema.duration],
                urlId = it[VoteSchema.uuid],
                status = it[VoteSchema.status],
                views = it[VoteSchema.views],
            ) to VoteChoose(
                id = it[VoteChoseSchema.id],
                vote = it[VoteChoseSchema.targetVote],
                content = it[VoteChoseSchema.content],
            )
        }.groupBy {
            it.first
        }.map {
            VoteDto(
                vote = it.key,
                chooses = it.value.map {
                    it.second
                }
            )
        }.singleOrNull()
    }
    
    suspend fun getUserActiveVotes(id: Long, offset: Long, limit: Int): List<VoteDto> = dbQuery{
        (VoteSchema innerJoin VoteChoseSchema).select {
            (VoteSchema.createdBy eq id) and (VoteSchema.status eq true)
        }.limit(limit, offset).map {
            Vote(
                id = it[VoteSchema.id],
                user = it[VoteSchema.createdBy],
                title = it[VoteSchema.title],
                timestamp = it[VoteSchema.timestamp],
                duration = it[VoteSchema.duration],
                urlId = it[VoteSchema.uuid],
                status = it[VoteSchema.status],
                views = it[VoteSchema.views],
            ) to VoteChoose(
                id = it[VoteChoseSchema.id],
                vote = it[VoteChoseSchema.targetVote],
                content = it[VoteChoseSchema.content],
            )
        }.groupBy {
            it.first
        }.map {
            VoteDto(
                vote = it.key,
                chooses = it.value.map {
                    it.second
                }
            )
        }
    }

    suspend fun getUserVotes(id: Long, offset: Long, limit: Int): List<VoteDto> = dbQuery{
        (VoteSchema innerJoin VoteChoseSchema).select {
            VoteSchema.createdBy eq id
        }.limit(limit, offset).map {
            Vote(
                id = it[VoteSchema.id],
                user = it[VoteSchema.createdBy],
                title = it[VoteSchema.title],
                timestamp = it[VoteSchema.timestamp],
                duration = it[VoteSchema.duration],
                urlId = it[VoteSchema.uuid],
                status = it[VoteSchema.status],
                views = it[VoteSchema.views],
            ) to VoteChoose(
                id = it[VoteChoseSchema.id],
                vote = it[VoteChoseSchema.targetVote],
                content = it[VoteChoseSchema.content],
            )
        }.groupBy {
            it.first
        }.map {
            VoteDto(
                vote = it.key,
                chooses = it.value.map {
                    it.second
                }
            )
        }
    }

    suspend fun getActiveVotesCount(id: Long): Int = dbQuery {
        VoteSchema.select {
            (VoteSchema.createdBy eq id) and (VoteSchema.status eq true)
        }.count().toInt()
    }

    suspend fun getVotesChooseSelections(id: Long, offset: Long, limit: Int): List<SelectionDto> = dbQuery {
        (SelectedVoteSchema innerJoin VoteChoseSchema innerJoin UserSchema).select {
            VoteChoseSchema.targetVote eq id
        }.limit(
            n = limit,
            offset = offset
        ).map { result ->
            val choose = VoteChoose(
                id = result[VoteChoseSchema.id],
                vote = result[VoteChoseSchema.targetVote],
                content = result[VoteChoseSchema.content],
            )

            val user = User(
                id = result[UserSchema.id],
                name = result[UserSchema.name],
                password = null,
                email = result[UserSchema.email]
            )

            SelectionDto(
                user = user,
                choose = choose
            )
        }
    }

    suspend fun getVotesCount(id: Long): Int = dbQuery{
        (SelectedVoteSchema innerJoin VoteChoseSchema).select {
            VoteChoseSchema.targetVote eq id
        }.count().toInt()
    }

    suspend fun incrementView(id: Long, views: Int): Boolean = dbQuery{
        VoteSchema.update(
            where = {
                VoteSchema.id eq id
            },
            body = {
                it[VoteSchema.views] = views
            }
        ) > 0
    }

}
