package com.miempresa.googlemap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_destinos.*

class destinos : AppCompatActivity() {

    var destino = ""
    var latitud = ""
    var longitud = ""
    var info = ""
    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinos)

        val recibidos = this.intent.extras
        if (recibidos != null) {
            destino = intent.extras!!.getString("destino")!!
            latitud = intent.extras!!.getString("latitud")!!
            longitud = intent.extras!!.getString("longitud")!!
            info = intent.extras!!.getString("info")!!
            imageUrl = intent.extras!!.getString("imageUrl")!!
        }
        lblDestino.setText(destino)
        lblCoordenadas.setText("$latitud , $longitud")
        lblInfo.setText(info)
        Picasso.get().load(imageUrl).into(img)

        btnVolver.setOnClickListener {
            volverLista()
        }
    }

    fun volverLista() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}