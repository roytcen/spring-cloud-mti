
package net.mcpes.hhm.nk.music.command

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.hhm.nk.music.SMusic
import net.mcpes.hhm.nk.music.SMusic.Companion.RANDOM_PLAY
import net.mcpes.summit.hhm.base.utils.language.translate
import java.io.File

/**
 * SMusic-New
 *
 * Package: net.mcpes.hhm.nk.music.command
 * @author hhm Copyright (c) 2019/2/19 15:47
 * version 1.0
 */
class MusicCommand : Command("music", "SMusic Master Command") {
    override fun execute(sender: CommandSender, s: String, strings: Array<out String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("smusic.command.notFound" translate arrayOf())
            return false
        }
        when (strings[0]) {
            "开始", "start" -> {
                if (sender !is Player) {
                    sender.sendMessage("smusic.command.notPlayer" translate arrayOf())
                    return true
                }
                SMusic.songPlayers[sender.name] = sender
                sender.sendMessage("smusic.command.start.success" translate arrayOf())
            }
            "暂停", "pause" -> {
                if (sender !is Player) {
                    sender.sendMessage("smusic.command.notPlayer" translate arrayOf())
                    return true
                }