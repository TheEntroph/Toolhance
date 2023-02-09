package com.github.devlaq.toolhance.ui

import io.github.monun.invfx.InvFX
import io.github.monun.invfx.openFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ToolhanceUI {

    fun showUI(player: Player, selectedItemSlot: Int? = null) {
        val frame = InvFX.frame(3, Component.text("Toolhance")) {
            // UI Background
            for (x in 0..8) {
                for(y in 0..2) {
                    item(x, y, ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                }
            }

            // Tool Slot
            if(selectedItemSlot != null) {
                val itemStack = player.inventory.getItem(selectedItemSlot)
                slot(4, 1) {
                    item = itemStack
                    onClick {
                        showUI(player)
                        val existingItemStack = player.inventory.getItem(selectedItemSlot)
                        if(existingItemStack != null) {
                            player.location.world.dropItemNaturally(player.location, existingItemStack)
                        }
                        player.inventory.setItem(selectedItemSlot, itemStack)
                    }
                }
            } else {
                item(4, 1, ItemStack(Material.AIR))
            }

            onClickBottom {
                if(it.currentItem != null) {
                    it.inventory.setItem(it.slot, null)
                    showUI(player, it.slot)
                }
            }
        }

        player.openFrame(frame)
    }

}
