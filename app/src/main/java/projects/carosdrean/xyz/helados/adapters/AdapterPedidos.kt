package projects.carosdrean.xyz.helados.adapters

import android.app.Activity
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.db.BaseDatos
import projects.carosdrean.xyz.helados.db.DataBaseSQL
import projects.carosdrean.xyz.helados.entidad.Identificador
import projects.carosdrean.xyz.helados.entidad.Pedido
import java.io.File
import java.io.IOException

/**
 * Created by josue on 26/11/2017.
 */
class AdapterPedidos(private val items: MutableList<Pedido>, private val activity: Context): RecyclerView.Adapter<AdapterPedidos.ViewHolder>() {
    var id = ""
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val storageReference = FirebaseStorage.getInstance().reference
        val pedido = items[position]
        holder?.nombre?.text = pedido.nombre
        holder?.cantidad?.text = "Cantidad: " + pedido.cantidad
        holder?.precio?.text = "Precio: S/." + pedido.precio.toDouble() * pedido.cantidad.toInt()
        id = pedido.ids
        val portada = storageReference.child("Imagenes").child(pedido.portada)
        try {
            val localFile = File.createTempFile("images", "jpg")
            portada.getFile(localFile).addOnSuccessListener {
                Glide.with(activity.applicationContext).load(localFile).into(holder?.imagen) }.addOnFailureListener {
                // Handle any errors
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun obtenerId(): String{
        val db = DataBaseSQL(activity)
        val identificador = Identificador()
        return db.obtenerRegistro(identificador).identificador
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_pedidos, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // Campos respectivos de un item
        var nombre: TextView
        var cantidad: TextView
        var precio: TextView
        var imagen: CircleImageView
        var contenedor: CardView

        init {
            contenedor = v.findViewById(R.id.contenedor_pedido)
            nombre = v.findViewById(R.id.nombre_p)
            cantidad = v.findViewById(R.id.cantidad_p)
            precio = v.findViewById(R.id.precio_p)
            imagen = v.findViewById(R.id.portada_p)
        }
    }

    fun removeItem(position: Int){
        val id = items.get(position).ids
        items.removeAt(position)
        notifyItemRemoved(position)
        val db = BaseDatos(activity)
        db.eliminarPedido(id, obtenerId())
    }
}