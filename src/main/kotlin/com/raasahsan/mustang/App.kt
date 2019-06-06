package com.raasahsan.mustang

import com.beust.klaxon.Klaxon
import com.raasahsan.mustang.ca.generateKeys
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 7000, module = Application::module)
    server.start(wait = true)
}

// TODO: Catch exceptions and perform proper error handling

fun Application.module() {
    routing {
        get("/") {
            call.respondText("OK")
        }
        post("/keys") {
            val keyGeneration = generateKeys("raas")
            call.respondText(Klaxon().toJsonString(keyGeneration))
        }
    }
}
