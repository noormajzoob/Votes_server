package com.vote.domain.dao

import com.vote.data.DatabaseFactory.dbQuery
import com.vote.data.schema.UserSchema
import com.vote.data.schema.VerificationSchema
import com.vote.domain.model.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserDao {

    suspend fun addUser(name: String, password: String, email: String): User? = dbQuery{
        UserSchema.insert { table ->
            table[UserSchema.name] = name
            table[UserSchema.email] = email
            table[UserSchema.password] = password
        }.resultedValues?.map { result ->
            User(
                id = result[UserSchema.id],
                name = result[UserSchema.name],
                password = result[UserSchema.password],
                email = result[UserSchema.email]
            )
        }?.singleOrNull()
    }

    suspend fun activateAccount(attemptCode: String, email: String): Boolean = dbQuery{
        val code = VerificationSchema.select {
            VerificationSchema.email eq email
        }.map {
            it[VerificationSchema.code]
        }.singleOrNull()

        if (attemptCode == code){
            UserSchema.update(
                where = {
                    UserSchema.email eq email
                },
                body = {
                    it[isActive] = true
                }
            )
            true
        }else false
    }

    suspend fun getUser(id: Long): User? = dbQuery{
        UserSchema.select {
            UserSchema.id eq id
        }.map { result ->
            User(
                id = result[UserSchema.id],
                name = result[UserSchema.name],
                password = result[UserSchema.password],
                email = result[UserSchema.email]
            )
        }.singleOrNull()
    }

    suspend fun login(email: String, password: String): Boolean = dbQuery {
        val user = UserSchema.select {
            UserSchema.email eq email
        }.map { result ->
            User(
                id = result[UserSchema.id],
                name = result[UserSchema.name],
                password = result[UserSchema.password],
                email = result[UserSchema.email]
            )
        }.singleOrNull()

        user?.password == password
    }


}