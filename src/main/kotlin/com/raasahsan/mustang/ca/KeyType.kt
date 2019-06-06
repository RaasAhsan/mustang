package com.raasahsan.mustang.ca

sealed class KeyType

data class Rsa(val bits: Int) : KeyType()

data class Ecdsa(val bits: Int) : KeyType()

data class Ed25519(val bits: Int) : KeyType()
