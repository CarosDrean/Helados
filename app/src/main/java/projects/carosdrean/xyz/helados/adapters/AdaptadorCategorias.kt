package projects.carosdrean.xyz.helados.adapters

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import projects.carosdrean.xyz.helados.DetalleProducto
import projects.carosdrean.xyz.helados.R
import projects.carosdrean.xyz.helados.entidad.Producto
import java.io.File
import java.io.IOException

/**
 * Created by josue on 25/11/2017.
 */
class AdaptadorCategorias(private val items: List<Producto>, private val activity: Activity) : RecyclerView.Adapter<AdaptadorCategorias.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // Campos respectivos de un item
        var nombre: TextView
        var precio: TextView
        var imagen: ImageView
        var contenedor: LinearLayout

        init {
            contenedor = v.findViewById(R.id.contenedor_producto)
            nombre = v.findViewById(R.id.nombre_prodcuto)
            precio = v.findViewById(R.id.precio_producto)
            imagen = v.findViewById(R.id.miniatura_producto)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_producto, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val storageReference = FirebaseStorage.getInstance().reference
        val producto = items[i]
        holder.nombre.text = producto.nombre
        holder.precio.text = "S/." + producto.precio
        val portada = storageReference.child("Imagenes").child(producto.portada)
        try {
            val localFile = File.createTempFile("images", "jpg")
            portada.getFile(localFile).addOnSuccessListener {
                Glide.with(activity.applicationContext).load(localFile).into(holder.imagen) }.addOnFailureListener {
                // Handle any errors
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        holder.contenedor.setOnClickListener { view ->
            val i = Intent(activity, DetalleProducto::class.java)
            i.putExtra("ids", producto.ids)
            i.putExtra("categoria", producto.categoria)
            i.putExtra("portada", producto.portada)
            i.putExtra("nombre", producto.nombre)
            i.putExtra("precio", producto.precio)
            i.putExtra("descripcion", producto.descripcion)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                activity.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, view, activity.getString(R.string.trancicionFoto)).toBundle()
                )
            }else{
                activity.startActivity(i)
            }
        }

    }


}