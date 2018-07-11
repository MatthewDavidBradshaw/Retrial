package com.matthewtamlin.retrial.hash

import io.reactivex.Single
import java.io.File
import java.nio.file.Files
import java.security.MessageDigest
import javax.inject.Provider


class Sha512HashGenerator {
  private val sha512DigestProvider: Provider<MessageDigest> = Provider { MessageDigest.getInstance("SHA-512") }

  fun generateChecksum(file: File) = Single.fromCallable {
    if (file.length() == 0L) {
      throw RuntimeException("Cannot calculate hash for empty file.")
    }

    sha512DigestProvider
        .get()
        .digest(Files.readAllBytes(file.toPath()))
        .map { toHexString(it) }
        .map { it.toUpperCase() }
        .joinToString(separator = "")
        .let { Sha512Hash(it) }
  }

  private fun toHexString(number: Byte) = String.format("%02x", number)
}