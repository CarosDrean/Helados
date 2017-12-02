package projects.carosdrean.xyz.helados.fragments


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.adapters.AdapterPedidos
import projects.carosdrean.xyz.helados.db.DataBaseSQL
import projects.carosdrean.xyz.helados.entidad.Identificador
import projects.carosdrean.xyz.helados.entidad.Pedido
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pedidos.*
import projects.carosdrean.xyz.helados.adapters.RecyclerItemTouchHelper
import projects.carosdrean.xyz.helados.db.BaseDatos
import projects.carosdrean.xyz.helados.entidad.Peticiones


/**
 * A simple [Fragment] subclass.
 */
class Pedidos : Fragment() , RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    var listaPedido: RecyclerView? = null
    var btnUbicacion: CardView? = null
    var total: TextView? = null
    var lista: ArrayList<Pedido>? = null
    var adaptadorFavorito: AdapterPedidos? = null
    var contexto: Context? = null
    var fab: FloatingActionButton? = null
    var lt = ""
    var ln = ""

    var nombre = ""
    var telefono = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_pedidos, container, false)

        inicializar(v)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        listaPedido!!.setHasFixedSize(true)
        listaPedido!!.layoutManager = llm
        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listaPedido)
        btnUbicacion?.setOnClickListener { seleccionarUbicacion() }
        datoUbicacion(v)
        userdata()
        fab?.setOnClickListener {
            alertPeticion()
        }
        return v
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is AdapterPedidos.ViewHolder) {
            adaptadorFavorito?.removeItem(viewHolder.adapterPosition)
            total?.text = "S/." + calcularTotal(lista).toString()
        }
    }

    override fun onResume() {
        super.onResume()
        datos(obtenerId())
    }

    fun inicializar(v: View){
        listaPedido = v.findViewById(R.id.lista_pedido)
        btnUbicacion = v.findViewById(R.id.seleccionar_ubicacion_p)
        total = v.findViewById(R.id.total_p)
        fab = v.findViewById(R.id.fab_peticion)
        contexto = context
        lista = ArrayList()
    }

    fun alertPeticion(){
        val builder = android.support.v7.app.AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle("Enviar Peticion")
                .setMessage("En breves momentos nos pondremos en contacto contigo.")
        builder.setPositiveButton("Aceptar"){ dialog, which ->
            realizarPeticion()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun calcularTotal(lista: ArrayList<Pedido>?): Double{
        var total = 0.0
        for(i in lista!!){
            try{
                total += (i.cantidad.toInt() * i.precio.toDouble())
            }catch (e: Exception){
                total = 0.0
            }

        }
        return total
    }

    fun datoUbicacion(v: View){
        val ubicacion: TextView = v.findViewById(R.id.direccion_p)
        val dbDireccion = FirebaseDatabase.getInstance().reference
                .child("Direcciones")
                .child(obtenerId())
                .child("posicion")
        dbDireccion.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0?.child("latitude")?.value != null){
                    lt = p0?.child("latitude")?.value.toString()
                    ln = p0?.child("longitude")?.value.toString()
                }else{
                    lt = (-14.063585).toString()
                    ln = (-75.729179).toString()
                }

                ubicacion.text = "Lat/Lng: " + lt + "," + ln
            }

        })
    }

    fun realizarPeticion(){
        val db = BaseDatos(context)
        db.guardarPeticion(Peticiones("id", nombre, telefono, LatLng(lt.toDouble(), ln.toDouble()), ids(), lista!![0].portada, productos()), obtenerId())
    }

    fun ids(): ArrayList<String>{
        val os = ArrayList<String>()
        for (i in lista!!){
            os.add(i.ids)
        }
        return os
    }

    fun productos(): String{
        var os = ""
        for (i in lista!!){
            os = os + i.cantidad + " " + i.nombre + " - "
        }
        return os
    }

    fun userdata(){
        val dbPersona = FirebaseDatabase.getInstance().reference
                .child("Personas")
                .child(obtenerId())
        dbPersona.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                nombre = p0?.child("nombre")?.value.toString()
                telefono = p0?.child("telefono")?.value.toString()
            }

        })
    }

    fun seleccionarUbicacion(){
        val builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = layoutInflater
        val v = inflater.inflate(R.layout.seleccionar_ubicacion, null)

        builder.setView(v)
                .setTitle("Seleccionar Ubicación")
                .setPositiveButton("Aceptar"){ dialog, which ->

                }
                .setNegativeButton("Cancelar"){ dialog, whitc->
                    dialog.dismiss()
                }
        val alert = builder.create()
        alert.show()
    }

    fun inicializarAdaptador() {
        adaptadorFavorito = AdapterPedidos(lista!!, contexto!!)
        listaPedido?.adapter = adaptadorFavorito
        total?.text = "S/." + calcularTotal(lista).toString()
    }

    fun obtenerId(): String{
        val db = DataBaseSQL(context)
        val identificador = Identificador()
        return db.obtenerRegistro(identificador).identificador
    }

    fun datos(id: String){
        lista?.clear()
        //val stReference = FirebaseStorage.getInstance().reference
        val dbPedido = FirebaseDatabase.getInstance().reference
                .child("Pedidos")
                .child(id)

        dbPedido.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val pedido = Pedido(p0?.child("ids")?.value.toString(),
                        p0?.child("portada")?.value.toString(),
                        p0?.child("nombre")?.value.toString(),
                        p0?.child("precio")?.value.toString(),
                        p0?.child("cantidad")?.value.toString())
                lista!!.add(pedido)
                inicializarAdaptador()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
    }


}// Required empty public constructor
