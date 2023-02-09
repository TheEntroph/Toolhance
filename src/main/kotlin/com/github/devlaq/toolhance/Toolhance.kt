package com.github.devlaq.toolhance

import com.github.devlaq.toolhance.command.Commands
import org.bukkit.plugin.java.JavaPlugin

class Toolhance: JavaPlugin() {

    companion object {
        lateinit var instance: Toolhance
    }

    init {
        instance = this
    }

    override fun onEnable() {
        Commands.init()
    }

    override fun onDisable() {

    }

}
