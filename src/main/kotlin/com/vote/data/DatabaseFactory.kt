package com.vote.data

import com.vote.data.schema.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(){

        val jdbcUrl = System.getenv("jdbcUrl")
        val driver = System.getenv("postgresDriver")

        val db = Database.connect(createHikariDataSource(jdbcUrl, driver))

        transaction(db){
            SchemaUtils.create(
                UserSchema,
                VerificationSchema,
                VoteSchema,
                VoteChoseSchema,
                SelectedVoteSchema
            )
        }

    }

    private fun createHikariDataSource(url: String, driver: String) = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}