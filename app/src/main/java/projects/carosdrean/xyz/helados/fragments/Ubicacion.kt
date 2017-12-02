package projects.carosdrean.xyz.helados.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.db.BaseDatos
import projects.carosdrean.xyz.helados.db.DataBaseSQL
import projects.carosdrean.xyz.helados.entidad.Identificador

/**
 * Created by josue on 26/11/2017.
 */

class Ubicacion : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private var mMap: GoogleMap? = null
    var marcador: Marker? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        getMapAsync(this)

        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        map.isMyLocationEnabled = true
        agregarMarcador(-14.063585, -75.729179)
        map.setOnMarkerDragListener(this)

    }

    private fun agregarMarcador(lat: Double, lng: Double) {
        val coordenadas = LatLng(lat, lng)
        val miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16f)
        marcador = mMap!!.addMarker(MarkerOptions()
                .position(coordenadas)
                .title("Mover para seleccionar")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_casa))
        )
        mMap!!.animateCamera(miUbicacion)
    }

    override fun onMarkerDragStart(marker: Marker) {

    }

    override fun onMarkerDrag(marker: Marker) {

    }

    override fun onMarkerDragEnd(marker: Marker) {
        val db = BaseDatos(activity)
        db.subirPosicionMarcador(marker.position, obtenerId())
    }

    fun obtenerId(): String{
        val db = DataBaseSQL(context)
        val identificador = Identificador()
        return db.obtenerRegistro(identificador).identificador
    }
}
