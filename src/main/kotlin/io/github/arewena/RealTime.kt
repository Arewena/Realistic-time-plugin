package io.github.arewena

import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class RealTime : JavaPlugin() {
    override fun onEnable() {
        val timeInformation: LocalDateTime = LocalDateTime.now()
        var second = timeInformation.format(DateTimeFormatter.ofPattern("ss")).toInt()
        var kommandboolean = false

        logger.info("Running!")

        kommand {
            register("realtime") {
                then("bool" to bool()) {
                    executes { context ->
                        val bool: Boolean by context
                        kommandboolean = bool
                        second = timeInformation.format(DateTimeFormatter.ofPattern("ss")).toInt()

                        }


                    }
                }
            }
        val task = object : BukkitRunnable() {
            override fun run() {
                if (kommandboolean){
                    Bukkit.getWorld("world")?.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                    val localtimeInformation: LocalDateTime = LocalDateTime.now()
                    val hour = localtimeInformation.format(DateTimeFormatter.ofPattern("HH")).toInt()
                    val time = localtimeInformation.format(DateTimeFormatter.ofPattern("mm")).toInt()
                    if (hour < 6) {
                        Bukkit.getWorld("world")?.time = (((18 - hour) * 1000) + (16.66 * time)).toLong()
                        logger.info("$hour.시 $time.분")
                    }
                    else {
                        Bukkit.getWorld("world")?.time = (((hour - 6) * 1000) + (16.66 * time)).toLong()
                        logger.info("$hour.시 $time.분")
                    }

                }
                else {
                    Bukkit.getWorld("world")?.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
                }
            }
        }.runTaskTimer(this@RealTime, (60 - second) * 20.toLong(), 1200)
    }

}




