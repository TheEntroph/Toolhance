package com.github.devlaq.toolhance

import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.ForInventory
import net.axay.kspigot.gui.GUIHolder
import net.axay.kspigot.gui.GUIInstance
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object GUIHolderWrapper {

    val registered: HashMap<Inventory, GUIInstance<ForInventory>>
        get() {
            val declaredField = GUIHolder::class.java.getDeclaredField("registered")
            declaredField.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            return declaredField.get(GUIHolder) as HashMap<Inventory, GUIInstance<ForInventory>>
        }

    fun init() {
        listen<InventoryClickEvent> {
            if(registered[it.inventory] != null && it.isShiftClick) {
                it.isCancelled = true
            }
        }
    }

}
