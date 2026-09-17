package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

class SocketsZmqActivity : AppCompatActivity() {

    private lateinit var etServerIp: EditText
    private lateinit var btnSend: Button
    private lateinit var tvLog: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sockets_zmq)

        etServerIp = findViewById(R.id.etServerIp)
        btnSend = findViewById(R.id.btnSend)
        tvLog = findViewById(R.id.tvLog)
        btnBack = findViewById(R.id.btnBackToMenu)

        btnBack.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {
            val ip = etServerIp.text.toString().trim()
            startClientOnce(ip)
        }
    }

    private fun startClientOnce(serverIp: String) {
        Thread {
            val endpoint = "tcp://$serverIp:2222"

            try {
                ZContext().use { ctx ->
                    val sock = ctx.createSocket(SocketType.REQ)
                    sock.connect(endpoint)

                    val request = "My name is Denis"
                    sock.send(request.toByteArray(ZMQ.CHARSET), 0)

                    val replyBytes = sock.recv(0)
                    val reply = String(replyBytes, ZMQ.CHARSET)

                    runOnUiThread {
                        tvLog.text = "Sent: $request\nRecv: $reply\nEndpoint: $endpoint"
                    }

                    sock.close()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvLog.text = "ERROR: ${e.message}\nEndpoint: $endpoint"
                }
            }
        }.start()
    }
}