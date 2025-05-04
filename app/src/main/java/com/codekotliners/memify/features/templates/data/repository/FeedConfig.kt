package com.codekotliners.memify.features.templates.data.repository

import com.google.firebase.firestore.DocumentSnapshot

class FeedConfig(
    val loop: Boolean = false,
) {
    var scrollState: ScrollState = ScrollState.NONE
    var nextStart: DocumentSnapshot? = null
        private set

    fun setNextStart(newNextStart: DocumentSnapshot?) {
        nextStart = newNextStart
        scrollState =
            if (newNextStart == null && !loop) {
                ScrollState.REACHED_END
            } else {
                ScrollState.STARTED
            }
    }

    fun reset() {
        scrollState = ScrollState.NONE
        nextStart = null
    }
}

enum class ScrollState {
    NONE,
    STARTED,
    REACHED_END,
}
