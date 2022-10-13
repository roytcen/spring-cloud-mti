package com.xxmicloxx.noteBlockAPI

import java.io.*
import java.util.*

object NBSDecoder {
    fun parse(decodeFile: File, title: String): Song {
        return parse(FileInputStream(decodeFile), decodeFile, title)
    }

    private fun parse(inputStream: InputStream, decodeFile: File, title2: String): Song {
        val layerHashMap = HashMap<Int, Layer>()
        val dis = DataInputStream(inputStream)
        val length = readShort(dis)
        val songHeight = readShort(dis)
        var title = readString(dis)
        if (title == "") {
            title = title2
        }
        val author = readString(dis)
        readString(dis)
        val description = readString(dis)
        val speed = readShort(dis) / 100f
        dis.readBoo