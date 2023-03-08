package com.vote.data.schema

import com.vote.data.util.cascadeReferences
import org.jetbrains.exposed.sql.Table

object VoteChoseSchema: Table("vote_chose") {

    val id = long("id").autoIncrement()
    val targetVote = long("target_vote") cascadeReferences VoteSchema.id
    val content = varchar("content", 500)

    override val primaryKey = PrimaryKey(id)

}