package com.github.devlaq.toolhance

import com.github.devlaq.toolhance.command.Commands
import com.github.devlaq.toolhance.ui.ToolhanceUI
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.ForInventory
import net.axay.kspigot.gui.GUIHolder
import net.axay.kspigot.gui.GUIInstance
import net.axay.kspigot.main.KSpigot
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class Toolhance: KSpigot() {

    companion object {
        lateinit var instance: Toolhance
    }

    override fun load() {
        instance = this
    }

    override fun startup() {
        GUIHolderWrapper.init()

        ToolhanceUI.init()

        Commands.init()
    }

    override fun shutdown() {

    }

}
