package com.github.devlaq.toolhance.command

import com.github.devlaq.toolhance.Toolhance
import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.kommand

object Commands {

    private val registrar = listOf<PluginKommand.() -> Unit>(
        TestUICommand::build
    )

    fun init() {
        registerCommands()
    }

    private fun registerCommands() {
        Toolhance.instance.apply {
            kommand {
                registrar.forEach { it() }
            }
        }
    }

}
