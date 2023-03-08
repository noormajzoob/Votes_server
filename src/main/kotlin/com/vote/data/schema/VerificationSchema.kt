package com.vote.data.schema

import org.jetbrains.exposed.sql.Table

object VerificationSchema: Table("verification") {

    val id = long("id").autoIncrement()
    val email = varchar("email", 500)
    val code = varchar("code", 25)
    val timestamp = long("timestamp").default(System.currentTimeMillis())

    override val primaryKey = PrimaryKey(id)

}