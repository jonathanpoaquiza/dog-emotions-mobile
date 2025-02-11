package com.example.dog_emotions_mobile

import android.os.Parcel
import android.os.Parcelable

class Mascota(
    val id: Int,
    val nombre: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val tamaño: String,
    val color: String,
    val latitud: Double,  // Nueva propiedad latitud
    val longitud: Double  // Nueva propiedad longitud
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),  // Leer latitud
        parcel.readDouble()   // Leer longitud
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(raza)
        parcel.writeInt(edad)
        parcel.writeDouble(peso)
        parcel.writeString(tamaño)
        parcel.writeString(color)
        parcel.writeDouble(latitud)  // Escribir latitud
        parcel.writeDouble(longitud) // Escribir longitud
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Mascota> {
        override fun createFromParcel(parcel: Parcel): Mascota {
            return Mascota(parcel)
        }

        override fun newArray(size: Int): Array<Mascota?> {
            return arrayOfNulls(size)
        }
    }
}
