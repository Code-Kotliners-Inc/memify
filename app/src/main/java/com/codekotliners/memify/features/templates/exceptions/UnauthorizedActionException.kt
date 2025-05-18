package com.codekotliners.memify.features.templates.exceptions

class UnauthorizedActionException(
    message: String = "User is not authorized to perform this action",
) : IllegalStateException(message)
