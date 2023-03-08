package com.vote.data.util

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail


object EmailService {

    fun sendMail(email: String){
        SimpleEmail().apply {
            hostName = "smtp.mail.yahoo.com"
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator("alnoor majzoob", "vote2025"))
            setFrom("alnoormajzoob2025@yahoo.com")
            subject = "TestMail"
            setMsg("The verification code is: ${generateCode()}")
            addTo(email)
            send()
        }
    }

    private fun generateCode(): String{
        val builder = StringBuilder()
        (1..5).forEach{
            builder.append((1..9).random())
        }

        return builder.toString()
    }
}