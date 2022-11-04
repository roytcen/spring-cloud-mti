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
        dis.readBoolean()
        dis.readByte()
        dis.readByte()
        readInt(dis)
        readInt(dis)
        readInt(dis)
        readInt(dis)
        readInt(dis)
        readString(dis)
        var tick: Int = -1
        while (true) {
            val jumpTicks = readShort(dis)
            if (jumpTicks.toInt() == 0) {
                break
            }
            tick += jumpTicks
            var layer: Int = -1
            while (true) {
                val jumpLayers = readShort(dis)
                if (jumpLayers.toInt() == 0) {
                    break
                }
                layer += jumpLayers
                setNote(layer, tick, dis.readByte().toInt(), dis.readByte(), layerHashMap)
            }
        }
        return Song(speed, layerHashMap, songHeight, length, title, author, description, decodeFile)
    }

    private fun setNote(layer: Int, ticks: Int, instrument: Int, key: Byte, layerHashMap: HashMap<Int, Layer>) {
        var l: Layer? = layerHashMap[layer]
        if (l == null) {
            l = Layer()
        