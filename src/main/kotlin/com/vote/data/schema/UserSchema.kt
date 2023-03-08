package com.vote.data.schema

import org.jetbrains.exposed.sql.Table

object UserSchema: Table("users") {

    val id = long("user_id").autoIncrement()
    val name = varchar("user_name", 250)
    val email = varchar("user_email", 500)
    val password = varchar("user_password", 500)
    val isActive = bool("active_account").default(false)

    override val primaryKey = PrimaryKey(id)

}