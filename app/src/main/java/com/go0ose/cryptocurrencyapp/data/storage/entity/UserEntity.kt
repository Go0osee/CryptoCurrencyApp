package com.go0ose.cryptocurrencyapp.data.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id:Int = 0,
    var avatar: String,
    var firstName: String,
    var lastName: String,
    var dayOfBirth: String
)