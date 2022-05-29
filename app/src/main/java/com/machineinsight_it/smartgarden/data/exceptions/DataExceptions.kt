package com.machineinsight_it.smartgarden.data.exceptions

class NetworkException(cause: Throwable) : Exception(cause)

class InvalidCredentialsException(cause: Throwable) : Exception(cause)

class UnknownException(cause: Throwable) : Exception(cause)