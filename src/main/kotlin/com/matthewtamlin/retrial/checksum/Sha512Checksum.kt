package com.matthewtamlin.retrial.checksum

/**
 * A SHA2-512 hash of some data.
 *
 * @property value the literal value of the hash
 */
data class Sha512Checksum(val value: String)