package com.drozdztomasz.playerapp

data class Track(
    val title: String,
    val uri: String,
) {
    companion object {
        val SAMPLE_TRACKS = listOf(
            Track(
                "The Rebel Path",
                "https://vgmsite.com/soundtracks/cyberpunk-2077-original-game-score/zalnnwrhwh/1-03%20The%20Rebel%20Path.mp3"
            ),
            Track(
                "Been Good to Know Ya",
                "https://vgmsite.com/soundtracks/cyberpunk-2077-original-game-score/qyiigwzhjb/2-17%20Been%20Good%20To%20Know%20Ya.mp3"
            ),
            Track(
                "Bells of Laguna Bend",
                "https://vgmsite.com/soundtracks/cyberpunk-2077-original-game-score/lkrndjcmiv/2-08%20Bells%20Of%20Laguna%20Bend.mp3"
            ),
            Track(
                "V",
                "https://vgmsite.com/soundtracks/cyberpunk-2077-original-game-score/xpmflnnvtl/1-01%20V.mp3"
            ),
            Track(
                "You Shall Never Have To Forgive Me Again",
                "https://vgmsite.com/soundtracks/cyberpunk-2077-original-game-score/jcynbainmd/1-13%20You%20Shall%20Never%20Have%20To%20Forgive%20Me%20Again.mp3"
            ),
        )
    }
}

