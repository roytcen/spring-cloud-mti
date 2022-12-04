
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