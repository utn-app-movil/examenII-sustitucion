package model

class Product {
    private var id: Int=0
    private var nombre: String=""
    private var categoria: String= ""
    private var precio: Float=0.0f
    private var cantidad: Int=0
    private var fotoPath: String=""

    constructor()

    constructor(id: Int, nombre: String, categoria: String, precio: Float, cantidad: Int, fotoPath: String){
        this.id=id
        this.nombre=nombre
        this.categoria=categoria
        this.precio=precio
        this.cantidad=cantidad
        this.fotoPath=fotoPath
    }

    var Id: Int
        get() = this.id
        set(value) {this.id=value}

    var Nombre: String
        get() = this.nombre
        set(value) {this.nombre=value}

    var Categoria: String
        get() = this.categoria
        set(value) {this.categoria=value}

    var Precio: Float
        get() = this.precio
        set(value) {this.precio=value}

    var Cantidad: Int
        get() = this.cantidad
        set(value) {this.cantidad=value}

    var FotoPath: String
        get() = this.fotoPath
        set(value) {this.fotoPath=value}


}