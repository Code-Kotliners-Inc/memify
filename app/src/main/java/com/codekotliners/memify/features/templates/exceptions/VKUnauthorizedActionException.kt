package com.codekotliners.memify.features.templates.exceptions

class VKUnauthorizedActionException(
    message: String = "VK account was not linked",
) : IllegalStateException(message)
