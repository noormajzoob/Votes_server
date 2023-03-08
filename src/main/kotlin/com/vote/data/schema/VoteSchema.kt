package com.vote.data.schema

import com.vote.data.util.cascadeReferences
import org.jetbrains.exposed.sql.Table
import java.util.*

object VoteSchema: Table("votes") {

    val id = long("id").autoIncrement()
    val createdBy = long("created_by") cascadeReferences UserSchema.id
    val title = varchar("vote_title",1500)
    val timestamp = long("timestamp").default(System.currentTimeMillis())
    val duration = integer("duration")
    val uuid = varchar("vote_url", 50).default(UUID.randomUUID().toString())
    val status = bool("is_available").default(true)

    override val primaryKey = PrimaryKey(id)

}