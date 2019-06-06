package com.raasahsan.mustang.ca

import java.io.File
import java.lang.RuntimeException
import java.util.*

const val userKeyDirectory = "/home/horizon/Desktop/projects/mustang/keys"
const val caKey = "/home/horizon/Desktop/projects/mustang/keys/ca"

fun generateKeys(userId: String): KeyGeneration {
    generate(userId)
    sign(userId)

    val publicKeyFile = File("$userKeyDirectory/$userId.pub")
    val publicKeyCertFile = File("$userKeyDirectory/$userId-cert.pub")
    val privateKeyFile = File("$userKeyDirectory/$userId")
    val publicKey = publicKeyCertFile.readBytes()
    val privateKey = privateKeyFile.readBytes()
    val publicKeyEncoded = Base64.getEncoder().encodeToString(publicKey)
    val privateKeyEncoded = Base64.getEncoder().encodeToString(privateKey)

    // TODO: Verify that the files have been deleted by inspecting the return value
    publicKeyFile.delete()
    publicKeyCertFile.delete()
    privateKeyFile.delete()

    return KeyGeneration(publicKeyEncoded, privateKeyEncoded)
}

private fun generate(userId: String) {
    val process = ProcessBuilder()
            .command("ssh-keygen", "-f", "$userKeyDirectory/$userId", "-t", "rsa", "-b", "4096", "-C", userId, "-N", "")
            .start()

    val status = process.waitFor()
    if (status == 0) {
        return
    } else {
        val outputText = process.inputStream.bufferedReader().use { it.readText() }
        val errorText = process.errorStream.bufferedReader().use { it.readText() }

        throw RuntimeException("Failed to generate keys: STDOUT: $outputText, STDERR: $errorText")
    }
}

private fun sign(userId: String) {
    val process = ProcessBuilder()
            .command("ssh-keygen", "-s", caKey, "-I", userId, "-V", "-5m:+5m", "$userKeyDirectory/$userId")
            .start()

    val status = process.waitFor()
    if (status == 0) {
        return
    } else {
        val outputText = process.inputStream.bufferedReader().use { it.readText() }
        val errorText = process.errorStream.bufferedReader().use { it.readText() }

        throw RuntimeException("Failed to sign keys: STDOUT: $outputText, STDERR: $errorText")
    }
}
