package com.github.devlaq.toolhance.command

import com.github.devlaq.toolhance.ui.ToolhanceUI
import io.github.monun.kommand.PluginKommand
import net.axay.kspigot.gui.openGUI
import org.bukkit.inventory.Inventory

object TestUICommand {

    fun build(kommand: PluginKommand) = kommand.run {
        register("testui") {
            requires { isOp && isPlayer }

            then("toolhance") {
                executes {
                    player.openGUI(ToolhanceUI.gui)
                }
                then("success") {
                    executes {
                        player.openGUI(ToolhanceUI.successGui)
                    }
                }
                then("fail") {
                    executes {
                        player.openGUI(ToolhanceUI.failGui)
                    }
                }
            }
        }
    }

}
