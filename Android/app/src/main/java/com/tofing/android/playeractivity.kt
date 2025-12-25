package com.tofing.android

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class playeractivity : AppCompatActivity() {

    private lateinit var mediaplayer: MediaPlayer
    private lateinit var seekbar: SeekBar
    private lateinit var tvcurrenttime: TextView
    private lateinit var tvtotaltime: TextView
    private lateinit var btnplay: Button
    private lateinit var btnpause: Button
    private lateinit var btnstop: Button
    private lateinit var btnback: Button
    private lateinit var listview: ListView
    private lateinit var volumeseekbar: SeekBar
    private var currentfilepath: String? = null
    private val musicfiles = mutableListOf<String>()
    private var isplaying = false
    private val log_tag = "PlayerActivity"

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            loadmusicfiles()
        } else {
            Toast.makeText(this, "Разрешение необходимо для работы", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mediaplayer = MediaPlayer()
        seekbar = findViewById(R.id.seekbar)
        tvcurrenttime = findViewById(R.id.tvcurrenttime)
        tvtotaltime = findViewById(R.id.tvtotaltime)
        btnplay = findViewById(R.id.btnplay)
        btnpause = findViewById(R.id.btnpause)
        btnstop = findViewById(R.id.btnstop)
        btnback = findViewById(R.id.btnback)
        listview = findViewById(R.id.listview)
        volumeseekbar = findViewById(R.id.volumeseekbar)

        checkpermissions()

        btnplay.setOnClickListener { playmusic() }
        btnpause.setOnClickListener { pausemusic() }
        btnstop.setOnClickListener { stopmusic() }
        btnback.setOnClickListener { finish() }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser && mediaplayer.isPlaying) {
                    mediaplayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        volumeseekbar.progress = 50
        volumeseekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val volume = progress / 100.0f
                mediaplayer.setVolume(volume, volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            currentfilepath = musicfiles[position]
            playselectedmusic(currentfilepath!!)
        }

        updatesseekbar()
    }

    private fun checkpermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadmusicfiles()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun loadmusicfiles() {
        musicfiles.clear()

        val musicPath = Environment.getExternalStorageDirectory().path + "/Music"
        Log.d(log_tag, "PATH: " + musicPath)
        val directory = File(musicPath)

        if (directory.exists() && directory.isDirectory) {
            scanformusicfiles(directory)
        } else {
            Toast.makeText(this, "Папка Music не найдена", Toast.LENGTH_SHORT).show()
        }

        if (musicfiles.isEmpty()) {
            musicfiles.add("Музыка не найдена")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, musicfiles.map { File(it).name })
        listview.adapter = adapter
    }

    private fun scanformusicfiles(directory: File) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                scanformusicfiles(file)
            } else if (file.name.lowercase().endsWith(".mp3") ||
                file.name.lowercase().endsWith(".wav") ||
                file.name.lowercase().endsWith(".ogg")) {
                musicfiles.add(file.absolutePath)
                Log.d(log_tag, "Найден файл: ${file.name}")
            }
        }
    }

    private fun playselectedmusic(filepath: String) {
        try {
            if (mediaplayer.isPlaying) {
                mediaplayer.stop()
            }
            mediaplayer.reset()
            mediaplayer.setDataSource(filepath)
            mediaplayer.prepare()
            mediaplayer.start()

            seekbar.max = mediaplayer.duration
            tvtotaltime.text = formattime(mediaplayer.duration)
            isplaying = true
            btnplay.isEnabled = false
            btnpause.isEnabled = true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка воспроизведения", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playmusic() {
        if (currentfilepath != null) {
            if (!mediaplayer.isPlaying) {
                mediaplayer.start()
                isplaying = true
                btnplay.isEnabled = false
                btnpause.isEnabled = true
            }
        } else {
            Toast.makeText(this, "Выберите трек", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pausemusic() {
        if (mediaplayer.isPlaying) {
            mediaplayer.pause()
            isplaying = false
            btnplay.isEnabled = true
            btnpause.isEnabled = false
        }
    }

    private fun stopmusic() {
        if (mediaplayer.isPlaying) {
            mediaplayer.stop()
            mediaplayer.reset()
            isplaying = false
            seekbar.progress = 0
            tvcurrenttime.text = "00:00"
            btnplay.isEnabled = true
            btnpause.isEnabled = false
        }
    }

    private fun formattime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updatesseekbar() {
        Thread {
            while (true) {
                if (mediaplayer.isPlaying) {
                    runOnUiThread {
                        seekbar.progress = mediaplayer.currentPosition
                        tvcurrenttime.text = formattime(mediaplayer.currentPosition)
                    }
                }
                Thread.sleep(1000)
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        if (mediaplayer.isPlaying) {
            mediaplayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaplayer.isPlaying) {
            mediaplayer.stop()
        }
        mediaplayer.release()
    }
}