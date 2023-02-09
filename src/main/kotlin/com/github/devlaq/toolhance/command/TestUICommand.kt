package com.github.devlaq.toolhance.command

import com.github.devlaq.toolhance.ui.ToolhanceUI
import io.github.monun.kommand.PluginKommand
import org.bukkit.inventory.Inventory

object TestUICommand {

    fun build(kommand: PluginKommand) = kommand.run {
        register("testui") {
            requires { isOp && isPlayer }

            then("toolhance") {
                executes {
                    ToolhanceUI.showUI(player)
                }
            }
        }
    }

}
