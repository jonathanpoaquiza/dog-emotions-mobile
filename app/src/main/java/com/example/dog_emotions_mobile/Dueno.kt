package com.example.dog_emotions_mobile

import android.os.Parcel
import android.os.Parcelable

class Dueno(
    val id: Int,
    val nombre: String,
    val correo: String,
    val celular: String,
    val latitud: Double,
    val longitud: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(correo)
        parcel.writeString(celular)
        parcel.writeDouble(latitud)
        parcel.writeDouble(longitud)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Dueno> {
        override fun createFromParcel(parcel: Parcel): Dueno {
            return Dueno(parcel)
        }

        override fun newArray(size: Int): Array<Dueno?> {
            return arrayOfNulls(size)
        }
    }
}
