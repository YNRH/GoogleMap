package com.miempresa.googlemap

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.miempresa.googlemap.databinding.ActivityMapsBinding
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
AdapterView.OnItemSelectedListener{

    private var destino = ""
    private var imgUrl = ""
    private var descripcion = ""
    var marcadorDestino: Marker? = null
    var coordenada = LatLng(0.0, 0.0)
    private val Plaza = LatLng(-16.39880006619331, -71.53690534409907)

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val tipos_mapas = intArrayOf(
        GoogleMap.MAP_TYPE_NONE,
        GoogleMap.MAP_TYPE_NORMAL,
        GoogleMap.MAP_TYPE_SATELLITE,
        GoogleMap.MAP_TYPE_HYBRID,
        GoogleMap.MAP_TYPE_TERRAIN
    )

    private var start:String = ""
    private var end:String = ""
    var poly: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        spnTipoMapa.onItemSelectedListener = this

        val recibidos = this.intent.extras
        if (recibidos != null) {
            destino = intent.extras!!.getString("destino")!!
        }

        //Rutas
        btnRoute.setOnClickListener {
            start = ""
            end = ""
            poly?.remove()
            poly = null
            if(::mMap.isInitialized){
                mMap.setOnMapClickListener{
                    if(start.isEmpty()){
                        start = "${it.longitude},${it.latitude}"
                    }else if(end.isEmpty()){
                        end = "${it.longitude},${it.latitude}"
                        createRoute()
                    }
                }
            }
        }
    }

    fun moverCamara(view: View){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Plaza, 18f))
    }

    fun agregarMarcador(view: View?){
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    mMap.cameraPosition.target.latitude,
                    mMap.cameraPosition.target.longitude
                )
            )
                .title("Estas en la plaza de Armas")
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123);
        }

        when (destino) {
            "plaza de armas" -> {
                coordenada = LatLng(-16.3988031, -71.5374435)
                imgUrl = "imageUrl"
                marcadorDestino = mMap.addMarker( MarkerOptions().position(coordenada).title("Conoce: $destino") )
                imgUrl = "https://lh5.googleusercontent.com/p/AF1QipM6gwEi0w6tkUol06TSj2DCl6vJJlSThoWdwG98=w408-h306-k-no"
                descripcion = "La plaza Mayor o plaza de Armas de Arequipa, es uno de los principales espacios públicos de Arequipa y el lugar de fundación de la ciudad."
            }
            "characato" -> {
                coordenada = LatLng(-16.4177279, -71.5066574)
                marcadorDestino = mMap.addMarker( MarkerOptions().position(coordenada).title("Conoce: $destino") )
                imgUrl = "https://lh5.googleusercontent.com/p/AF1QipPiFnuTt1TzphVZRiJeezwNS75eAnnMqGD2n6rc=w548-h318-n-k-no"
                descripcion = "El distrito de Characato es uno de los 29 distritos que conforman la provincia de Arequipa en el departamento de Arequipa, bajo la administración del Gobierno regional de Arequipa, en el sur del Perú."
            }
            "unsa" -> {
                coordenada = LatLng(-16.4046919, -71.5265450)
                marcadorDestino = mMap.addMarker( MarkerOptions().position(coordenada).title("Conoce: $destino") )
                imgUrl = "https://lh5.googleusercontent.com/p/AF1QipOmNm3M2KDZAMdV_YJnSv3GZzyny9djan4Jxx4E=w408-h271-k-no"
                descripcion = "La Universidad Nacional de San Agustín de Arequipa es una universidad pública peruana ubicada en la ciudad de Arequipa. Fue fundada el 11 de noviembre de 1828, siendo la quinta universidad más antigua del país y la segunda fundada en la época republicana."
            }
            "zoologico" -> {
                coordenada = LatLng(-16.4200989, -71.4754636)
                marcadorDestino = mMap.addMarker( MarkerOptions().position(coordenada).title("Conoce: $destino") )
                imgUrl = "https://lh5.googleusercontent.com/p/AF1QipNSou1CAUvyPQC17x0Vw80oDBK4mXDusYyk4hEg=w426-h240-k-no"
                descripcion = "Hogar pequeño de grandes felinos y cóndores andinos, con acuario y puestos de recuerdos y bocadillos."
            }
            "mirador sachaca" -> {
                coordenada = LatLng(-16.4260858, -71.5674416)
                marcadorDestino = mMap.addMarker( MarkerOptions().position(coordenada).title("Conoce: $destino") )
                imgUrl = "https://lh5.googleusercontent.com/p/AF1QipN2oD5gx496Qyj3WLcGHf6N051WQjM1KyfoVfX4=w408-h272-k-no"
                descripcion = "El mirador tiene cinco pisos. Desde su terraza se puede apreciar a los volcanes Misti, Chachani y Pichu Pichu. En el tercer piso se encuentra la estatua de un Cristo Redentor Blanco hecho de mármol de 2.5 metros de altura, que fue enviada desde Francia en el año 1969."
            }
            else -> {
                Toast.makeText(this, "No se encontro destino turistico", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        marcadorDestino?.showInfoWindow()
        // Cámara
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenada, 15f))
        // Eventos
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        if (p0!!.equals(marcadorDestino)) {
            val intent = Intent(this, destinos::class.java)
            intent.putExtra("destino", destino)
            intent.putExtra("latitud", p0.getPosition().latitude.toString() + "")
            intent.putExtra("longitud", p0.getPosition().longitude.toString() + "")
            intent.putExtra("info", descripcion)
            intent.putExtra("imageUrl", imgUrl)

            startActivity(intent)
            return true
        }
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mMap.mapType = tipos_mapas[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun createRoute(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getRoute("5b3ce3597851110001cf62486bdf3c5167c047fe834080d8498e94e6", start, end)
            if(call.isSuccessful){
                drawRoute(call.body())
            }
        }
    }

    private fun drawRoute(body: RouteResponse?){
        val polyLineOptions = PolylineOptions()
        body?.features?.first()?.geometry?.coordinates?.forEach{
            polyLineOptions.add(LatLng(it[1], it[0]))
        }
        runOnUiThread {
            poly = mMap.addPolyline(polyLineOptions)
        }
    }
//
    fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}