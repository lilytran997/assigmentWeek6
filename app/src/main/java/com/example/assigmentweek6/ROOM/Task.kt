package com.example.assigmentweek6.ROOM

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var description: String,
    var completed: Boolean,
    var userid: String):Parcelable {
    constructor():this(null,"",false,"")
}