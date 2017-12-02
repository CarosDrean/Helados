package projects.carosdrean.xyz.helados

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detalle_producto.*
import projects.carosdrean.xyz.helados.db.BaseDatos
import projects.carosdrean.xyz.helados.db.DataBaseSQL
import projects.carosdrean.xyz.helados.entidad.Identificador
import projects.carosdrean.xyz.helados.entidad.Pedido
import java.io.File
import java.io.IOException

class DetalleProducto : AppCompatActivity() {

    var portada: ImageView? = null
    var descripcion: TextView? = null
    var precio: TextView? = null
    var pedido: Pedido? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            agregarPedidoDialogo()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("nombre")
        inicializar()
        reemplazar()
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle_producto, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            /*R.id.action_eliminar -> {
                val db = BaseDatos(this)
                db.eliminarProducto(intent.getStringExtra("ids"), intent.getStringExtra("categoria"))
                finish()
                return true
            }*/
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun agregarPedidoDialogo(){
        val builder = AlertDialog.Builder(this@DetalleProducto)
        val v = layoutInflater.inflate(R.layout.agregar_compra, null)
        datosPedido(v)
        builder.setView(v)
        builder.setPositiveButton("Continuar"){ dialog, which ->
            agregarPedido(pedido!!)
            dialog.dismiss()
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun datosPedido(v: View){
        val total: TextView = v.findViewById(R.id.total_c)
        val cantidad: TextView = v.findViewById(R.id.cantidad_c)
        val btnMenos: Button = v.findViewById(R.id.btn_menos_c)
        val btnMas: Button = v.findViewById(R.id.btn_mas_c)
        val toolbar: Toolbar = v.findViewById(R.id.toolbar_compra)

        val precio = intent.getStringExtra("precio")
        val nombre = intent.getStringExtra("nombre")

        total.text = (cantidad.text.toString().toInt() * precio.toDouble()).toString()
        toolbar.title = nombre

        btnMenos.setOnClickListener{
            val cant = cantidad.text.toString()
            if(cant.toInt() > 1) {
                cantidad.text = (cant.toInt() - 1).toString()
                total.text = (cantidad.text.toString().toInt() * precio.toDouble()).toString()
                pedido?.cantidad = cantidad.text.toString()
            }
        }
        btnMas.setOnClickListener{
            cantidad.text = (cantidad.text.toString().toInt() + 1).toString()
            total.text = (cantidad.text.toString().toInt() * precio.toDouble()).toString()
            pedido?.cantidad = cantidad.text.toString()
        }

        pedido = Pedido("id", intent.getStringExtra("portada"), nombre,
                precio, cantidad.text.toString())
    }

    fun agregarPedido(pedido: Pedido){
        val db = BaseDatos(this)
        val dbSql = DataBaseSQL(this)
        val identificador = Identificador()
        val idUser = dbSql.obtenerRegistro(identificador).identificador
        db.guardarPedido(pedido, idUser)
    }

    fun detalleFoto(v: View){
        val i = Intent(this, DetalleFoto::class.java)
        i.putExtra("foto", intent.getStringExtra("portada"))
        startActivity(i)
    }

    fun inicializar(){
        portada = findViewById(R.id.portada_dp)
        descripcion = findViewById(R.id.descripcion_dp)
        precio = findViewById(R.id.precio_dp)
    }

    fun reemplazar(){
        descripcion?.text = intent.getStringExtra("descripcion")
        precio?.text = "S/." + intent.getStringExtra("precio")
        obtenerPortada()
    }

    fun obtenerPortada(){
        val storageReference = FirebaseStorage.getInstance().reference
        val portada = storageReference.child("Imagenes").child(intent.getStringExtra("portada"))
        try {
            val localFile = File.createTempFile("images", "jpg")
            portada.getFile(localFile).addOnSuccessListener {
                Glide.with(this).load(localFile).into(portada_dp)
                portada_dp.setOnClickListener { view ->
                    val i = Intent(this, DetalleFoto::class.java)
                    i.putExtra("foto", intent.extras.getString("portada"))
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        val explode = Explode()
                        explode.duration = 300
                        window.exitTransition = explode
                        startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this, view, getString(R.string.trancicionFoto)).toBundle()
                        )
                    }else{
                        startActivity(i)
                    }
                }
            }.addOnFailureListener {
                // Handle any errors
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
