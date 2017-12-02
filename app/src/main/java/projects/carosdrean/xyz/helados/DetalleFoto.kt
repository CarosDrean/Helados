package projects.carosdrean.xyz.helados

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_detalle_foto.*
import java.io.File

class DetalleFoto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_foto)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.mipmap.ic_close)

        val foto: ImageView = findViewById(R.id.det_portada)
        obtenerPortada(intent.extras.getString("foto"), foto)
    }

    fun obtenerPortada(nombre: String, portada: ImageView){
        val stReferebce = FirebaseStorage.getInstance().reference
                .child("Imagenes")
                .child(nombre)

        try {
            val file = File.createTempFile("images", "jpg")
            stReferebce.getFile(file).addOnSuccessListener {
                Glide.with(this).load(file).into(portada)
            }
        }catch (e:Exception){

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == android.R.id.home){
            onBackPressed()
            return true
        }else{
            return super.onOptionsItemSelected(item)
        }
    }

}

