package com.go0ose.cryptocurrencyapp.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto")
data class CryptoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "currentPrice")
    val currentPrice: Double,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "marketCap")
    val marketCap: Long
)