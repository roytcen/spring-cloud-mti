
package net.mcpes.hhm.nk.music

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.network.protocol.BlockEventPacket
import cn.nukkit.network.protocol.PlaySoundPacket
import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.Config
import com.xxmicloxx.noteBlockAPI.NBSDecoder
import com.xxmicloxx.noteBlockAPI.Song
import net.mcpes.hhm.nk.music.command.MusicCommand
import net.mcpes.hhm.nk.music.listener.PlayerListener
import net.mcpes.hhm.nk.music.task.MusicPlayingTask
import net.mcpes.summit.hhm.base.utils.language.LanguageUtils
import net.mcpes.summit.hhm.base.utils.language.translate
import java.io.File
import java.util.*

/**
 * SMusic
 *
 * @author hhm Copyright (c)
 * version 1.0
 */

class SMusic : PluginBase() {
    init {
        instance = this
    }

    override fun onLoad() {
        this.dataFolder.mkdirs()
        File(this.dataFolder.absolutePath + "/songs/").mkdirs()
        LanguageUtils.load(this, TITLE)
        LanguageUtils.saveMainConfig(this)
        masterConfig = Config(this.dataFolder.absolutePath + "/config.yml", Config.YAML)
        playMode = masterConfig["PlayMode"].toString().toInt()
        showMusicList = masterConfig["ShowMusicList"].toString().toBoolean()
        this.server.logger.info("smusic.load.info" translate arrayOf())
    }

    override fun onEnable() {
        this.server.pluginManager.registerEvents(PlayerListener(), this)
        this.server.commandMap.register("music", MusicCommand())
        loadAllSong()
        this.server.scheduler.scheduleAsyncTask(this, MusicPlayingTask())
        this.server.logger.info("smusic.enable.info" translate arrayOf())
    }

    companion object {
        const val LIST_LOOP_PLAY = 1
        const val SINGLE_SONG_LOOP_PLAY = 2
        const val RANDOM_PLAY = 3
        const val TITLE = "§l§7|§bS§dM§au§cs§1i§2c§7| §6"
        lateinit var masterConfig: Config
        var playing = true
        lateinit var instance: SMusic
        val songs = arrayListOf<Song>()
        val songPlayers = hashMapOf<String, Player>()
        var playMode = LIST_LOOP_PLAY
        private var song: Song? = null
        private var tick = -1
        private var lastPlayed: Long = 0
        var showMusicList = true

        fun broadcast() {
            if (!playing) return
            if (song == null) this.nextSong()
            if (System.currentTimeMillis() - lastPlayed < 50 * song!!.delay) return
            tick++
            if (tick > song!!.length) {
                next()
            }
            songPlayers.forEach { _, u ->
                if (!u.isOnline) songPlayers.remove(u.name)
                sendSound(u, song!!, tick)
            }
            lastPlayed = System.currentTimeMillis()
        }

        fun nextSong() {
            if (playMode == RANDOM_PLAY) {
                var random = Random().nextInt(songs.size)
                while (songs[random] === song) {
                    random = Random().nextInt(songs.size)
                }
                return
            }
            if (!songs.contains(song)) {
                this.song = songs[0]
                return
            }
            if (songs.indexOf(song) >= songs.size - 1) {
                this.song = songs[0]
                return
            }
            this.song = songs[songs.indexOf(song) + 1]
        }

        fun lastSong() {
            if (!songs.contains(song)) {
                this.song = songs[0]
                return
            }
            if (songs.indexOf(song) < 1) {
                this.song = songs[songs.size - 1]
                return
            }
            this.song = songs[songs.indexOf(song) - 1]
        }

        fun loadAllSong() {
            val lists = File(SMusic.instance.dataFolder.absolutePath + "/songs/").listFiles() ?: return
            lists.filter { it.name.endsWith(".nbs") }.forEach(this::loadSong)
            instance.server.logger.info("smusic.music.load.all.success" translate arrayOf(songs.size.toString()))
        }

        fun loadSong(file: File) {