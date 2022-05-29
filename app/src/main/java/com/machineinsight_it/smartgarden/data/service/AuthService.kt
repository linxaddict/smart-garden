package com.machineinsight_it.smartgarden.data.service

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.machineinsight_it.smartgarden.data.backend.SmartGardenService
import com.machineinsight_it.smartgarden.data.exceptions.InvalidCredentialsException
import com.machineinsight_it.smartgarden.data.exceptions.NetworkException
import com.machineinsight_it.smartgarden.data.exceptions.UnknownException
import com.machineinsight_it.smartgarden.data.model.AuthData
import com.machineinsight_it.smartgarden.data.model.AuthResponseData
import com.machineinsight_it.smartgarden.data.model.RefreshTokenData
import com.machineinsight_it.smartgarden.data.model.RefreshTokenResponseData
import com.machineinsight_it.smartgarden.domain.Session
import retrofit2.HttpException
import java.net.SocketException
import javax.inject.Inject


private const val SHARED_PREFS = "auth.prefs"
private const val KEY_EMAIL = "email"
private const val KEY_ACCESS_TOKEN = "access"
private const val KEY_REFRESH_TOKEN = "refresh"

suspend fun <T> mapExceptions(func: suspend () -> T): T {
    try {
        return func()
    } catch (httpException: HttpException) {
        if (httpException.code() == 401) {
            throw InvalidCredentialsException(httpException)
        }
        throw NetworkException(httpException)
    } catch (socketException: SocketException) {
        throw NetworkException(socketException)
    } catch (exception: Exception) {
        throw UnknownException(exception)
    }
}

class AuthService @Inject constructor(
    private val smartGardenService: SmartGardenService,
    private val context: Context
) {
    private suspend fun authenticate(email: String, password: String): AuthResponseData =
        mapExceptions {
            return@mapExceptions smartGardenService.authenticate(AuthData(email, password))
        }

    private suspend fun refresh(token: String): RefreshTokenResponseData =
        mapExceptions {
            return@mapExceptions smartGardenService.refreshToken(RefreshTokenData(token))
        }

    private fun storeSession(session: Session) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            SHARED_PREFS,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, session.email)
        editor.putString(KEY_ACCESS_TOKEN, session.token)
        editor.putString(KEY_REFRESH_TOKEN, session.refreshToken)
        editor.apply()
    }

    suspend fun createSession(email: String, password: String): Session {
        val authData = authenticate(email, password)
        val session = Session(email, authData.access, authData.refresh)

        storeSession(session)

        return session
    }

    fun loadSession(): Session? {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            SHARED_PREFS,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val access = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        val refresh = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)

        if (email == null || access == null || refresh == null) {
            return null
        }

        return Session(email, access, refresh)
    }

    suspend fun refreshSession(session: Session): Session {
        val authData = refresh(session.refreshToken)
        val refreshedSession = Session(session.email, authData.access, authData.refresh)

        storeSession(refreshedSession)

        return refreshedSession
    }
}