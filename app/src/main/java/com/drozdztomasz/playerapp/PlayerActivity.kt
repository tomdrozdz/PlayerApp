// Tomasz Drozdz, 246718
// Testowane na emulatorze + Samsung Galaxy Note 9

package com.drozdztomasz.playerapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.drozdztomasz.playerapp.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var playerService: PlayerService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        playerService ?: run {
            Intent(this, PlayerService::class.java).also {
                bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
        }
        binding.playerView.showController()
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerServiceBinder
            playerService = binder.playerService

            binding.playerView.player = binder.player
            binding.titleTV.text =
                binder.player.currentMediaItem?.mediaId ?: getString(R.string.empty_title)

            binder.player.addListener(object : Player.EventListener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    binding.titleTV.text = mediaItem?.mediaId ?: getString(R.string.empty_title)
                }

                // override artwork from uri because it doesn't fit in
                override fun onTracksChanged(
                    trackGroups: TrackGroupArray,
                    trackSelections: TrackSelectionArray
                ) {
                    val artWork =
                        BitmapFactory.decodeResource(resources, R.drawable.background_gradient)
                    val aw: ImageView = findViewById<View>(R.id.exo_artwork) as ImageView
                    aw.setImageBitmap(artWork)
                    aw.visibility = View.VISIBLE
                }
            })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerService = null
        }
    }
}
