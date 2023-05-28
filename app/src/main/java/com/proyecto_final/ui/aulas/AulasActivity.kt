package com.proyecto_final.ui.aulas

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.proyecto_final.R
import com.proyecto_final.databinding.ActivityAulasBinding
import kotlinx.android.synthetic.main.activity_aulas.*

class AulasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAulasBinding
    private var pos1: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAulasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sacarDatos()
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.edi))
        videoView.start()

    }

    private fun sacarDatos() {
        this.pos1 = intent.getIntExtra("position", -1)

    }
}