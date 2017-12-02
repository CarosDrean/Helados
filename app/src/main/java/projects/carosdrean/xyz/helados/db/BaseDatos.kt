package projects.carosdrean.xyz.helados.db

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import projects.carosdrean.xyz.helados.entidad.Pedido
import projects.carosdrean.xyz.helados.entidad.Persona
import projects.carosdrean.xyz.helados.entidad.Producto
import com.google.firebase.database.DatabaseReference
import com.google.android.gms.maps.model.LatLng
import projects.carosdrean.xyz.helados.entidad.Peticiones


/**
 * Created by josue on 25/11/2017.
 */
class BaseDatos(context: Context) {
    var context: Context? = context

    fun subirPosicionMarcador(posicion: LatLng, id: String) {
        val dbmarcador = FirebaseDatabase.getInstance().reference
                .child("Direcciones")
                .child(id)
        dbmarcador.child("posicion").setValue(posicion)
    }

    fun guardarPeticion(peticiones: Peticiones, idUser: String){
        val dbPedidos = FirebaseDatabase.getInstance().reference
                .child("Peticiones")
                .child(idUser)
        val key = dbPedidos.push().key
        peticiones.id = key
        dbPedidos.child(key).setValue(peticiones)
    }

    fun subirProducto(producto: Producto){
        val dbProduct = FirebaseDatabase.getInstance().reference
                .child("Productos")
                .child(producto.categoria)
        val key = dbProduct.push().key
        producto.ids = key
        dbProduct.child(key).setValue(producto)
    }

    fun guardarDatos(persona: Persona){
        val dbPersona = FirebaseDatabase.getInstance().reference
                .child("Personas")
        val key = dbPersona.push().key
        persona.ids = key
        dbPersona.child(key).setValue(persona)
        guardarId(key)
    }

    fun guardarId(id: String){
        val db = DataBaseSQL(context)
        val content = ContentValues()
        content.put("id", id)
        content.put("ids", id)
        db.ingresarRegistro(content)
    }

    fun guardarPedido(pedido: Pedido, id: String){
        val dbpedido = FirebaseDatabase.getInstance().reference
                .child("Pedidos")
                .child(id)
        val key = dbpedido.push().key
        pedido.ids = key
        dbpedido.child(key).setValue(pedido)
    }

    fun actualizarProducto(producto: Producto){
        val dbProduct = FirebaseDatabase.getInstance().reference
                .child("Productos")
                .child(producto.categoria)
                .child(producto.ids)
        dbProduct.child("nombre").setValue(producto.nombre)
        dbProduct.child("precio").setValue(producto.precio)
        dbProduct.child("descripcion").setValue(producto.descripcion)
        dbProduct.child("categoria").setValue(producto.categoria)
        dbProduct.child("portada").setValue(producto.portada)
    }

    fun eliminarPedido(id: String, idUser: String){
        val dbPedido = FirebaseDatabase.getInstance().reference
                .child("Pedidos")
                .child(idUser)
                .child(id)
        dbPedido.removeValue()
    }

    fun eliminarProducto(id: String, categoria: String){
        val dbProducto = FirebaseDatabase.getInstance().reference
                .child("Productos")
                .child(categoria)
                .child(id)
        dbProducto.removeValue()
    }

    fun subirFoto(uri: Uri){
        val stReference = FirebaseStorage.getInstance().reference
                .child("Imagenes")
                .child(uri.lastPathSegment)
        stReference.putFile(uri).addOnSuccessListener {
            //Toast.makeText(activity, "¡La imagen se subio con exito!", Toast.LENGTH_SHORT).show()
        }
    }
}