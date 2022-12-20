//I may come back to this

package com.google.android.gms.location.sample.foregroundlocation

/*
 * Sending an email using kotlin and javax.mail
 *
 * Usage: java -jar app.jar <user> <password> <from> <to> <cc>
 */
//package main.kotlin.sendmail

import java.util.*
import javax.mail.*
import javax.mail.internet.*

fun main(args: Array<String>) {
    val userName =  args[0]
    val password =  args[1]
    // FYI: passwords as a command arguments isn't safe
    // They go into your bash/zsh history and are visible when running ps

    val emailFrom = args[2]
    val emailTo = args[3]
    val emailCC = args[4]

    val subject = "SMTP Test"
    val text = "Hello Kotlin Mail"

    val props = Properties()
    putIfMissing(props, "mail.smtp.host", "smtp.office365.com")
    putIfMissing(props, "mail.smtp.port", "587")
    putIfMissing(props, "mail.smtp.auth", "true")
    putIfMissing(props, "mail.smtp.starttls.enable", "true")

    val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(userName, password)
        }
    })

    session.debug = true

    try {
        val mimeMessage = MimeMessage(session)
        mimeMessage.setFrom(InternetAddress(emailFrom))
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
        mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailCC, false))
        mimeMessage.setText(text)
        mimeMessage.subject = subject
        mimeMessage.sentDate = Date()

        val smtpTransport = session.getTransport("smtp")
        smtpTransport.connect()
        smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
        smtpTransport.close()
    } catch (messagingException: MessagingException) {
        messagingException.printStackTrace()
    }
}

private fun putIfMissing(props: Properties, key: String, value: String) {
    if (!props.containsKey(key)) {
        props[key] = value
    }
}
