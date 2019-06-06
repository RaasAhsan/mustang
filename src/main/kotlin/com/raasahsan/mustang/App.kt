package com.raasahsan.mustang

import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.json
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 7000, module = Application::module)
    server.start(wait = true)
}

const val userKeyDirectory = "/home/horizon/Desktop/projects/mustang/keys"
const val caKey = "/home/horizon/Desktop/projects/mustang/keys/ca.key"

fun Application.module() {
    routing {
        get("/") {
            call.respondText("OK")
        }
        post("/keys") {
            val process = ProcessBuilder()
                    .command("ssh-keygen", "-f", "$userKeyDirectory/user1", "-t", "rsa", "-b", "4096", "-C", "user1", "-N", "")
                    .start()

            val status = process.waitFor()
            if (status == 0) {
                val publicKeyFile = File("$userKeyDirectory/user1.pub")
                val privateKeyFile = File("$userKeyDirectory/user1")
                val publicKey = publicKeyFile.readBytes()
                val privateKey = privateKeyFile.readBytes()
                val publicKeyEncoded = Base64.getEncoder().encodeToString(publicKey)
                val privateKeyEncoded = Base64.getEncoder().encodeToString(privateKey)

                // TODO: Verify that the files have been deleted by inspecting the return value
                publicKeyFile.delete()
                privateKeyFile.delete()

                val response = KeyGenerationResponse(publicKeyEncoded, privateKeyEncoded)

                call.respondText(Klaxon().toJsonString(response))
            } else {
                val outputText = process.inputStream.bufferedReader().use { it.readText() }
                val errorText = process.errorStream.bufferedReader().use { it.readText() }

                println(outputText)
                println(errorText)

                call.respondText("Failed")
            }
        }
    }
}
