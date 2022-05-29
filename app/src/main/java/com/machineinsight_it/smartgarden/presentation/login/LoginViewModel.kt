package com.machineinsight_it.smartgarden.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.data.exceptions.InvalidCredentialsException
import com.machineinsight_it.smartgarden.data.exceptions.NetworkException
import com.machineinsight_it.smartgarden.data.exceptions.UnknownException
import com.machineinsight_it.smartgarden.data.service.AuthService
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.base.SingleLiveEvent
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val resourceLocator: ResourceLocator
) : BaseViewModel() {

    val userLoggedInEvent = SingleLiveEvent<Void?>()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginEnabled = MutableLiveData<Boolean>()

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    private val _loginInProgress = MutableLiveData<Boolean>()
    val loginInProgress: LiveData<Boolean> = _loginInProgress

    init {
        viewModelScope.launch {
            email
                .asFlow()
                .combine(password.asFlow()) { email, password -> Pair(email, password) }
                .map { pair -> pair.first.isNotEmpty() && pair.second.isNotEmpty() }
                .map { credentialsPresent ->
                    credentialsPresent && !(loginInProgress.value ?: false)
                }
                .collect { enabled -> loginEnabled.postValue(enabled) }
        }
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loginInProgress.postValue(true)

                val session = authService.createSession(
//                    "rpi_tomatoes@machineinsight-it.com2",
//                    "Tomatoes!x01!sn"
                    email = email.value ?: "",
                    password = password.value ?: ""
                )

                _loginInProgress.postValue(false)
                val refreshedSession = authService.refreshSession(session)
                val loadedSession = authService.loadSession()
                userLoggedInEvent.postValue(null)
            } catch (exception: InvalidCredentialsException) {
                _loginInProgress.postValue(false)
                _loginError.postValue(resourceLocator.label(R.string.invalid_credentials_error))
            } catch (exception: NetworkException) {
                _loginInProgress.postValue(false)
                _loginError.postValue(resourceLocator.label(R.string.network_error))
            } catch (exception: UnknownException) {
                _loginInProgress.postValue(false)
                _loginError.postValue(resourceLocator.label(R.string.unknown_error))
            }
        }
    }
}