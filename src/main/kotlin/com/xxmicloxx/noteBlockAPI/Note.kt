package com.xxmicloxx.noteBlockAPI

import cn.nukkit.level.Sound

class Note(instrument: Int, val key: Byte) {
    private var instrument = 0
    val sound: Sound
        get() {
            return when (getInstrument()) {
                0 -> Sound.NOTE_HARP
                1 -> Sound.NOTE_BASS
                2 -> Sound.NOTE_SNARE
                3 -> Sound.NOTE_HAT
                4 -> Sound.NOTE_BASSATTACK
                else -> Sound.NOTE_HARP
            }
        }

    init {
        when (instrument) {
            1 -> this.instrument = 4
            2 -> this.ins