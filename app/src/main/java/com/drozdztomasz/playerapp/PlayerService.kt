package com.drozdztomasz.playerapp

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class PlayerService : Service() {
    private lateinit var player: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager

    companion object {
        private const val CHANNEL_ID = "player_app_channel"
        private const val NOTIFICATION_ID = 1234
        private const val REWIND_INCREMENT = 10000L
    }

    inner class PlayerServiceBinder : Binder() {
        val playerService
            get() = this@PlayerService

        val player
            get() = this@PlayerService.player
    }

    override fun onCreate() {
        super.onCreate()
        player = SimpleExoPlayer.Builder(this).build()

        // pause when disconnecting headphones
        player.setHandleAudioBecomingNoisy(true)

        val mediaItems = createMediaItems(Track.SAMPLE_TRACKS)
        for (item in mediaItems)
            player.addMediaItem(item)

        player.prepare()

        val outsideThis = this

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            CHANNEL_ID,
            R.string.channel_name,
            R.string.channel_desc,
            NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return PendingIntent.getActivity(
                        outsideThis,
                        0,
                        Intent(outsideThis, PlayerActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return player.currentMediaItem?.mediaId.toString()
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    return null
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return null
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    if (ongoing)
                        startForeground(notificationId, notification)
                    else
                        stopForeground(false)
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                }
            }
        )

        playerNotificationManager.setControlDispatcher(
            DefaultControlDispatcher(
                REWIND_INCREMENT,
                REWIND_INCREMENT
            )
        )
        playerNotificationManager.setPlayer(player)
    }

    override fun onBind(intent: Intent?): IBinder {
        return PlayerServiceBinder()
    }

    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        player.release()
        super.onDestroy()
    }

    private fun createMediaItems(tracks: List<Track>): List<MediaItem> {
        val list = mutableListOf<MediaItem>()
        for (track in tracks) {
            list.add(
                MediaItem.Builder()
                    .setMediaId(track.title)
                    .setUri(track.uri)
                    .build()
            )
        }
        return list
    }
}
