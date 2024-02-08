package com.example.mylime.domain.mapper

import com.example.mylime.domain.model.Beer
import com.example.mylime.model.ResponseItem

fun ResponseItem.toModel() = Beer(
    description = description,
    id = id,
    imageUrl = imageUrl,
    name = name
    )

