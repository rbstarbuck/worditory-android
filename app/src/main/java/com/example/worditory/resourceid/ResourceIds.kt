package com.example.worditory.resourceid

import com.example.worditory.R
import java.security.InvalidParameterException

fun getResourceId(persistedId: Int): Int =
    when (persistedId) {
        0 -> R.drawable.player_avatar_placeholder
        1 -> R.drawable.player_avatar_1
        2 -> R.drawable.player_avatar_2
        3 -> R.drawable.player_avatar_3
        4 -> R.drawable.player_avatar_4
        5 -> R.drawable.player_avatar_5
        6 -> R.drawable.player_avatar_6
        7 -> R.drawable.player_avatar_7
        8 -> R.drawable.player_avatar_8
        9 -> R.drawable.player_avatar_9
        10 -> R.drawable.player_avatar_10
        11 -> R.drawable.player_avatar_11
        12 -> R.drawable.player_avatar_12
        13 -> R.drawable.player_avatar_13
        14 -> R.drawable.player_avatar_14
        15 -> R.drawable.player_avatar_15
        16 -> R.drawable.player_avatar_16
        17 -> R.drawable.player_avatar_17
        18 -> R.drawable.player_avatar_18
        19 -> R.drawable.player_avatar_19
        20 -> R.drawable.player_avatar_20
        21 -> R.drawable.player_avatar_21
        22 -> R.drawable.player_avatar_22
        23 -> R.drawable.player_avatar_23
        24 -> R.drawable.player_avatar_24
        25 -> R.drawable.player_avatar_25
        26 -> R.drawable.player_avatar_26
        27 -> R.drawable.player_avatar_27
        28 -> R.drawable.player_avatar_28
        29 -> R.drawable.player_avatar_29
        30 -> R.drawable.player_avatar_30
        31 -> R.drawable.npc_bear
        32 -> R.drawable.npc_fish
        33 -> R.drawable.npc_chicken
        34 -> R.drawable.npc_cat
        35 -> R.drawable.npc_penguin
        36 -> R.drawable.npc_cow
        37 -> R.drawable.npc_sloth
        38 -> R.drawable.npc_owl
        39 -> R.drawable.npc_rabbit
        40 -> R.drawable.npc_elephant
        41 -> R.drawable.npc_monkey
        42 -> R.drawable.npc_cobra
        else -> throw InvalidParameterException("Unrecognized persisted resource ID")
    }