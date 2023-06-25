package com.miempresa.googlemap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val values = arrayOf(
            "PLAZA DE ARMAS",
            "CHARACATO",
            "UNSA",
            "ZOOLOGICO",
            "MIRADOR SACHACA"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, android.R.id.text1,
            values
        )
        lstDestinos.adapter = adapter
        lstDestinos.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val itemValue =
                    lstDestinos.getItemAtPosition(i) as String
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                intent.putExtra("destino", itemValue.toLowerCase())
                startActivity(intent)
            }
    }
}