package com.codekotliners.memify.features.home.mocks

import com.codekotliners.memify.core.models.User

data class MockMeme(
    val id: String,
    val title: String,
    val url: String,
)

val MOCK_MEMES: List<MockMeme> =
    listOf(
        MockMeme(
            "1",
            "Cat 1",
            "https://i.pinimg.com/736x/d3/44/e6/d344e64a5ee02a80315b0320e9702632.jpg",
        ),
        MockMeme(
            "2",
            "Cat 2",
            "https://i.pinimg.com/474x/60/9e/eb/609eeb0aba7a0addae816dca22c12505.jpg",
        ),
        MockMeme(
            "3",
            "Cat 3",
            "https://i.pinimg.com/474x/ff/81/b7/ff81b7b5b8c90905351d7475fdcc3fc3.jpg",
        ),
    )

val mockUser =
    User(
        "",
        "https://media.zenfs.com/en/us.abcnews.go.com/9da71bee32e1aa23075b2c04c38987b3",
        "Name",
    )
