package com.shayan.movies.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Trailer(
        @NonNull @SerializedName("id") val id: String?,
        @NonNull @SerializedName("iso_639_1") val iso_639_1: String?,
        @NonNull @SerializedName("iso_3166_1") val iso_3166_1: String?,
        @NonNull @SerializedName("key") val key: String?,
        @NonNull @SerializedName("name") val name: String?,
        @NonNull @SerializedName("site") val site: String?,
        @NonNull @SerializedName("size") val size: Int,
        @NonNull @SerializedName("type") val type: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(iso_639_1)
        parcel.writeString(iso_3166_1)
        parcel.writeString(key)
        parcel.writeString(name)
        parcel.writeString(site)
        parcel.writeInt(size)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trailer> {
        override fun createFromParcel(parcel: Parcel): Trailer {
            return Trailer(parcel)
        }

        override fun newArray(size: Int): Array<Trailer?> {
            return arrayOfNulls(size)
        }
    }

}