package com.example.mylime.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.mylime.domain.model.Beer
import com.example.mylime.model.ResponseItem

class BeerProvider: PreviewParameterProvider<Beer> {

    override val values: Sequence<Beer>
        get() = sequenceOf(
            Beer(
            id = 1,
                description = "Birra alla spina",
                imageUrl = "hhtps",
                name = "Ceres"
            )
        )
}