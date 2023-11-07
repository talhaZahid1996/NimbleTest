package com.nimbletest.app.data.datastore

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class CryptoManager @Inject constructor() {

    private val charset by lazy {
        Charset.forName(CHARACTER_ENCODING)
    }

    private fun getCipher() =
        Cipher.getInstance("${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}")

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getInitializedCipherForEncryption(
        keyAlias: String,
        isUserAuthenticationRequired: Boolean = false
    ): Cipher {
        val cipher = getCipher()
        cipher.init(
            Cipher.ENCRYPT_MODE,
            getOrCreateSecretKey(keyAlias, isUserAuthenticationRequired)
        )
        return cipher
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getInitializedCipherForDecryption(
        keyAlias: String,
        initializationVector: ByteArray,
        isUserAuthenticationRequired: Boolean = false
    ): Cipher {
        val cipher = getCipher()
        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(keyAlias, isUserAuthenticationRequired),
            GCMParameterSpec(SPEC_LEN, initializationVector)
        )
        return cipher
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptData(
        keyAlias: String,
        text: String
    ): CiphertextWrapper {
        val cipher = getInitializedCipherForEncryption(keyAlias)
        return CiphertextWrapper(cipher.doFinal(text.toByteArray(charset)), cipher.iv)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decryptData(
        keyAlias: String, encryptedData: ByteArray,
        initializationVector: ByteArray
    ) =
        getInitializedCipherForDecryption(keyAlias, initializationVector).doFinal(encryptedData)
            .toString(charset)

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getOrCreateSecretKey(
        keyAlias: String,
        isUserAuthenticationRequired: Boolean = false
    ): SecretKey {
        // If Secretkey was previously created for that keyName, then grab and return it.
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(keyAlias, null)?.let { return it as SecretKey }

        // if you reach here, then a new SecretKey must be generated for that keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuilder.apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(isUserAuthenticationRequired)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    companion object {
        private const val CHARACTER_ENCODING = "UTF-8"
        private const val KEY_SIZE = 256
        private const val SPEC_LEN = 128
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }
}

data class CiphertextWrapper(val ciphertext: ByteArray, val initializationVector: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CiphertextWrapper

        if (!ciphertext.contentEquals(other.ciphertext)) return false
        if (!initializationVector.contentEquals(other.initializationVector)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ciphertext.contentHashCode()
        result = HASH_CODE_BASE * result + initializationVector.contentHashCode()
        return result
    }

    companion object {
        const val HASH_CODE_BASE = 31
    }
}