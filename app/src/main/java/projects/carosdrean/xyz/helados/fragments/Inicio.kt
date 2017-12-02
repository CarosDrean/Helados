package projects.carosdrean.xyz.helados.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.adapters.AdapterInicio
import projects.carosdrean.xyz.helados.entidad.Producto


/**
 * A simple [Fragment] subclass.
 */
class Inicio : Fragment() {

    var lista: ArrayList<Producto>? = null
    var listaProductos: RecyclerView? = null
    var adapter: AdapterInicio? = null
    var contexto: Context? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_inicio, container, false)
        inicializar(v)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        listaProductos!!.setHasFixedSize(true)
        listaProductos!!.layoutManager = llm
        return v
    }

    override fun onResume() {
        super.onResume()
        datos()
    }

    fun inicializar(v: View){
        listaProductos = v.findViewById(R.id.reciclador)
        contexto = context
        lista = ArrayList()
    }

    fun inicializarAdaptador() {
        adapter = AdapterInicio(lista!!, activity)
        listaProductos?.adapter = adapter
    }

    fun datos(){
        lista?.clear()
        val dbProducto = FirebaseDatabase.getInstance().reference
                .child("Productos")
                .child("Seleccion")

        dbProducto.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val producto = Producto(p0?.child("ids")?.value.toString(),
                        p0?.child("portada")?.value.toString(),
                        p0?.child("nombre")?.value.toString(),
                        p0?.child("precio")?.value.toString(),
                        p0?.child("descripcion")?.value.toString(),
                        p0?.child("categoria")?.value.toString())
                lista!!.add(producto)
                inicializarAdaptador()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
    }

}// Required empty public constructor
