package com.example.myapplication

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MediaPlayerActivity : AppCompatActivity() {

    data class Track(val name: String, val resId: Int)

    private var player: MediaPlayer? = null
    private var index = 0

    private val tracks = mutableListOf(
        Track("1000 – ATL", R.raw.song_1),
        Track("Не надо меня узнавать – Скриптонит", R.raw.song_2),
        Track("Архитектор – ATL", R.raw.song_3),
        Track("Астронавт – ATL", R.raw.song_4),
        Track("В унисон – ATL", R.raw.song_5),
        Track("Вороний грай – ATL", R.raw.song_6),
        Track("Гори ясно – ATL", R.raw.song_7),
        Track("Майк – ATL", R.raw.song_8),
        Track("Обратно – ATL", R.raw.song_9),
        Track("Священный рэйв – ATL", R.raw.song_10),
        Track("Коньяк – Скриптонит", R.raw.song_11),
        Track("Космос – Скриптонит", R.raw.song_12),
        Track("Положение – Скриптонит", R.raw.song_13),
        Track("Чистый – Скриптонит", R.raw.song_14),
        Track("Любовь – Скриптонит", R.raw.song_15),
        Track("Танцуйте – ATL", R.raw.song_16),
        Track("Шаман – ATL", R.raw.song_17)
    )

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val btnBack: Button = findViewById(R.id.btnBackToMenu)
        val btnPlay: Button = findViewById(R.id.btnPlayPause)
        val btnPrev: Button = findViewById(R.id.btnPrevious)
        val btnNext: Button = findViewById(R.id.btnNext)
        val btnSort: Button = findViewById(R.id.btnSort)

        val tvName: TextView = findViewById(R.id.tvTrackName)
        val tvTime: TextView = findViewById(R.id.tvTime)
        val seek: SeekBar = findViewById(R.id.seekBar)
        val vol: SeekBar = findViewById(R.id.volumeSeekBar)
        val list: ListView = findViewById(R.id.lvPlaylist)

        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vol.max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        vol.progress = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        vol.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) {
                if (fromUser) audio.setStreamVolume(AudioManager.STREAM_MUSIC, p, 0)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        val adapter = ArrayAdapter(
            this,
            R.layout.item_track,
            R.id.tvItem,
            tracks.map { it.name }.toMutableList()
        )
        list.adapter = adapter

        fun loadTrack(i: Int, autoplay: Boolean = true) {
            player?.stop()
            player?.release()
            index = ((i % tracks.size) + tracks.size) % tracks.size
            player = MediaPlayer.create(this, tracks[index].resId)
            tvName.text = tracks[index].name
            seek.max = player!!.duration
            seek.progress = 0
            if (autoplay) {
                player!!.start()
                btnPlay.text = "⏸"
            } else {
                btnPlay.text = "▶"
            }
        }

        loadTrack(0, autoplay = false)

        btnBack.setOnClickListener { finish() }

        btnPlay.setOnClickListener {
            val p = player ?: return@setOnClickListener
            if (p.isPlaying) {
                p.pause()
                btnPlay.text = "▶"
            } else {
                p.start()
                btnPlay.text = "⏸"
            }
        }

        btnNext.setOnClickListener { loadTrack(index + 1) }
        btnPrev.setOnClickListener { loadTrack(index - 1) }

        btnSort.setOnClickListener {
            val currentName = tracks.getOrNull(index)?.name
            tracks.sortBy { it.name }
            @Suppress("UNCHECKED_CAST")
            val ad = list.adapter as ArrayAdapter<String>
            ad.clear()
            ad.addAll(tracks.map { it.name })
            ad.notifyDataSetChanged()
            index = tracks.indexOfFirst { it.name == currentName }.coerceAtLeast(0)
        }

        list.setOnItemClickListener { _, _, pos, _ -> loadTrack(pos) }

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) {
                if (fromUser) player?.seekTo(p)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        handler.post(object : Runnable {
            override fun run() {
                player?.let {
                    seek.progress = it.currentPosition
                    val cur = it.currentPosition / 1000
                    val dur = it.duration / 1000
                    tvTime.text = "%d:%02d / %d:%02d".format(cur / 60, cur % 60, dur / 60, dur % 60)
                }
                handler.postDelayed(this, 500)
            }
        })
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        player?.release()
        player = null
        super.onDestroy()
    }
}