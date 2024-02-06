package com.example.mylime.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.mylime.model.ResponseItem

class BeerProvider: PreviewParameterProvider<ResponseItem> {

    override val values: Sequence<ResponseItem>
        get() = sequenceOf(
            ResponseItem(
            id = 1,
                description = "Birra alla spina",
                imageUrl = "hhtps",
                name = "Ceres"
            )
        )
}