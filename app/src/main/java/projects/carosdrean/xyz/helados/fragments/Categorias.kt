package projects.carosdrean.xyz.helados.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.adapters.AdaptadorCategorias
import projects.carosdrean.xyz.helados.entidad.Producto

/**
 * Created by josue on 25/11/2017.
 */
class Categorias : Fragment() {

    private var reciclador: RecyclerView? = null
    private var layoutManager: GridLayoutManager? = null
    private var adaptador: AdaptadorCategorias? = null

    var listaPost: RecyclerView? = null
    var productos: ArrayList<Producto>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_grupo_items, container, false)

        inicializar()
        reciclador = view.findViewById(R.id.reciclador)
        layoutManager = GridLayoutManager(activity, 2)
        reciclador!!.layoutManager = layoutManager

        return view
    }

    override fun onResume() {
        cargarDatos()
        actualizar()
        super.onResume()
    }

    fun cargarDatos(){
        val indiceSeccion = arguments.getInt(INDICE_SECCION)
        when (indiceSeccion) {
            0 -> {
                datos("Pequeño")
                actualizar()
            }
            1 -> {
                datos("Grande")
                actualizar()
            }
        }
    }

    fun actualizar(){
        adaptador = AdaptadorCategorias(productos!!, activity)
        reciclador!!.adapter = adaptador
    }

    fun inicializar(){
        productos = ArrayList()
    }

    fun datos(categoria: String){
        productos?.clear()
        val dbProductos = FirebaseDatabase.getInstance().reference
                .child("Productos")
                .child(categoria)
        dbProductos.addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val Producto = Producto(
                        p0?.child("ids")?.value.toString(),
                        p0?.child("portada")?.value.toString(),
                        p0?.child("nombre")?.value.toString(),
                        p0?.child("precio")?.value.toString(),
                        p0?.child("descripcion")?.value.toString(),
                        p0?.child("categoria")?.value.toString()
                )
                productos?.add(Producto)
                actualizar()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onCancelled(p0: DatabaseError?) {
            }

        })
    }

    companion object {

        private val INDICE_SECCION = "com.restaurantericoparico.FragmentoCategoriasTab.extra.INDICE_SECCION"

        fun nuevaInstancia(indiceSeccion: Int): Categorias {
            val fragment = Categorias()
            val args = Bundle()
            args.putInt(INDICE_SECCION, indiceSeccion)
            fragment.arguments = args
            return fragment
        }
    }


}