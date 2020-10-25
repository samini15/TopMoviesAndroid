package com.shayan.movies.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class TrailersResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("results") val results: ArrayList<Trailer>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.createTypedArrayList(Trailer)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrailersResponse> {
        override fun createFromParcel(parcel: Parcel): TrailersResponse {
            return TrailersResponse(parcel)
        }

        override fun newArray(size: Int): Array<TrailersResponse?> {
            return arrayOfNulls(size)
        }
    }

}