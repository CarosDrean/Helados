package projects.carosdrean.xyz.helados.entidad

import com.google.android.gms.maps.model.LatLng

/**
 * Created by josue on 25/11/2017.
 */
class Producto{
    var ids: String = ""
    var portada: String = ""
    var nombre: String = ""
    var precio: String = ""
    var descripcion: String = ""
    var categoria: String = ""

    constructor(ids: String, portada: String, nombre: String, precio: String, descripcion: String, categoria: String){
        this.ids = ids
        this.portada = portada
        this.nombre = nombre
        this.precio = precio
        this.descripcion = descripcion
        this.categoria = categoria
    }

    constructor()
}

class Pedido{
    var ids: String = ""
    var portada: String = ""
    var nombre: String = ""
    var precio: String = ""
    var cantidad: String = ""

    constructor(id:String,portada: String, nombre: String, precio: String, cantidad: String){
        this.ids = id
        this.portada = portada
        this.nombre = nombre
        this.precio = precio
        this.cantidad = cantidad
    }

    constructor()
}

class Persona{
    var ids = ""
    var nombre = ""
    var telefono = ""

    constructor(ids: String, nombre: String, telefono: String){
        this.ids = ids
        this.nombre = nombre
        this.telefono = telefono
    }

    constructor()
}

class Peticiones{
    var id = ""
    var nombre = ""
    var telefono = ""
    var portada = ""
    var productos = ""
    var direccion: LatLng? = null
    var ids:ArrayList<String>? = null

    constructor(id: String,nombre: String, telefono: String, direccion: LatLng, ids: ArrayList<String>, portada: String, productos:String){
        this.id = id
        this.nombre = nombre
        this.telefono = telefono
        this.direccion = direccion
        this.ids = ids
        this.portada = portada
        this.productos = productos
    }

    constructor()
}

class Identificador{
    var id = ""
    var identificador = ""

    constructor(id: String, identificador: String){
        this.id = id
        this.identificador = identificador
    }

    constructor()
}