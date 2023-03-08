package com.vote.data.schema

import com.vote.data.util.cascadeReferences
import org.jetbrains.exposed.sql.Table

object SelectedVoteSchema: Table("selected_vote") {

    val id = long("id").autoIncrement()
    val voteChosen = long("vote_chosen") cascadeReferences VoteChoseSchema.id
    val user = long("user") cascadeReferences UserSchema.id
    val timestamp = long("timestamp").default(System.currentTimeMillis())

    override val primaryKey = PrimaryKey(id)

}