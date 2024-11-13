package com.example.worditory.chooser.boardsize

import com.google.gson.annotations.SerializedName

internal data class EnabledBoardSizes(
    @SerializedName("size5x4")
    val size5x4: Boolean,

    @SerializedName("size5x5")
    val size5x5: Boolean,

    @SerializedName("size7x5")
    val size7x5: Boolean,

    @SerializedName("size6x6")
    val size6x6: Boolean,

    @SerializedName("size8x6")
    val size8x6: Boolean,

    @SerializedName("size8x8")
    val size8x8: Boolean
)