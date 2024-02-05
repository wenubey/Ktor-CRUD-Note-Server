package com.wenubey.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Note(
    @BsonId
    val id:String?= null,
    val noteTitle:String,
    val description:String,
    val createdAt:String
)
