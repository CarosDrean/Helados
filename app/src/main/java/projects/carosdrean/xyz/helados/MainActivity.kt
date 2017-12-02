package projects.carosdrean.xyz.helados

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import projects.carosdrean.xyz.helados.db.BaseDatos
import projects.carosdrean.xyz.helados.db.DataBaseSQL
import projects.carosdrean.xyz.helados.entidad.Identificador
import projects.carosdrean.xyz.helados.entidad.Persona
import projects.carosdrean.xyz.helados.fragments.Inicio
import projects.carosdrean.xyz.helados.fragments.Pedidos
import projects.carosdrean.xyz.helados.fragments.Perfil
import projects.carosdrean.xyz.helados.fragments.Productos

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var vHome: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        if(nav_view != null){
            onNavigationItemSelected(nav_view.menu.getItem(0))
        }

        vHome = nav_view.getHeaderView(0)

        if(detectarUsoApp == 0){
            dialogoBienvenida()
        }else{
            if(comprobarDatosUser())dialogoBienvenida()
        }
        datosCabecera(vHome!!)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_acerca_de -> {
                alertAcercaDe()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        var fragementManager = false

        when (item.itemId) {
            R.id.nav_inicio -> {
                fragementManager = true
                fragment = Inicio()
            }
            R.id.nav_productos -> {
                fragementManager = true
                fragment = Productos()
            }
            R.id.nav_pedidos -> {
                fragementManager = true
                fragment = Pedidos()
            }
            R.id.nav_perfil -> {
                fragementManager = true
                fragment = Perfil()
            }
        }

        if(fragementManager){
            supportFragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
            item.isChecked = true
            supportActionBar?.title = item.title
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun alertAcercaDe(){
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setTitle("Acerca de Frutica")
                .setMessage("Somos una nueva empresa de la ciudad de Ica, que les ofrece un servicio de calidad unica.")
        builder.setPositiveButton("Listo"){ dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun datosCabecera(v: View){
        val fondo: ImageView = v.findViewById(R.id.fondo_cabecera)
        val perfil: CircleImageView = v.findViewById(R.id.perfil_cabecera)

        Glide.with(this).load(resources.getDrawable(R.drawable.portada)).into(fondo)
        Glide.with(this).load(resources.getDrawable(R.drawable.perfil)).into(perfil)
    }

    fun dialogoBienvenida(){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false)
        builder.setTitle("Ingresar Datos")
                .setCancelable(false)
        val v = layoutInflater.inflate(R.layout.ingresar_datos, null)
        val nombre: EditText = v.findViewById(R.id.nombre_i)
        val telefono: EditText = v.findViewById(R.id.telefono_i)
        builder.setView(v)
        builder.setPositiveButton("Guardar"){ dialog, which ->
            if(nombre.text.toString() != "" && telefono.text.toString() != ""){
                guardarDatos(nombre.text.toString(), telefono.text.toString())
                dialog.dismiss()
            }else{
                Toast.makeText(this, "¡Llene todos los campos!", Toast.LENGTH_LONG).show()
            }

        }
        val dialog = builder.create()
        dialog.show()
    }

    fun guardarDatos(nombre: String, telefono: String){
        val db = BaseDatos(this)
        db.guardarDatos(Persona("id", nombre, telefono))
    }

    fun comprobarDatosUser() : Boolean{
        val db = DataBaseSQL(this)
        val identificador = Identificador()
        return db.obtenerRegistro(identificador).identificador == ""
    }

    private val detectarUsoApp: Int
        get() {
            val sp = getSharedPreferences("MYAPP", 0)
            val result: Int
            val currentVersionCode = BuildConfig.VERSION_CODE
            val lastVersionCode = sp.getInt("FIRSTTIMERUN", -1)
            if (lastVersionCode == -1)
                result = 0
            else
                result = if (lastVersionCode == currentVersionCode) 1 else 2
            sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply()
            return result
        }
}
