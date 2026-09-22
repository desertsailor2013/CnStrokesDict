package com.cnstrokesdict.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict_payload")
data class DictPayload(
    @PrimaryKey @ColumnInfo(name = "char") val character: String,
    @ColumnInfo(name = "payload_json") val payloadJson: String,
)
