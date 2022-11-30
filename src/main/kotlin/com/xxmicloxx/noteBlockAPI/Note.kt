package com.xxmicloxx.noteBlockAPI

import cn.nukkit.level.Sound

class Note(instrument: Int, val key: Byte) {
    private var instrument = 0
    val sound: Sound
        get() {
            return when (getInstrument()) {
                0 -> Sound.NOTE_H