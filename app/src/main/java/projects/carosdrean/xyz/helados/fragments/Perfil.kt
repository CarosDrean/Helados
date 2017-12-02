package projects.carosdrean.xyz.helados.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.db.DataBaseSQL
import projects.carosdrean.xyz.helados.entidad.Identificador


/**
 * A simple [Fragment] subclass.
 */
class Perfil : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_perfil, container, false)
        llenarDatos(v)
        return v
    }

    fun llenarDatos(v: View){
        val nombre: TextView = v.findViewById(R.id.p_nombre)
        val telefono: TextView = v.findViewById(R.id.p_telefono)

        val dbPersona = FirebaseDatabase.getInstance().reference
                .child("Personas")
                .child(obtenerId())
        dbPersona.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                nombre.text = p0?.child("nombre")?.value.toString()
                telefono.text = p0?.child("telefono")?.value.toString()
            }

        })
    }

    fun obtenerId(): String{
        val db = DataBaseSQL(context)
        val identificador = Identificador()
        return db.obtenerRegistro(identificador).identificador
    }

}// Required empty public constructor
