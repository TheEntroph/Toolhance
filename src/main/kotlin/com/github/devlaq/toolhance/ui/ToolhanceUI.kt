package com.github.devlaq.toolhance.ui

import com.github.devlaq.toolhance.GUIHolderWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.kyori.adventure.sound.Sound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object ToolhanceUI {
    val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {
        title = literalText("Toolhance")

        // Ready
        page(1) {
            placeholder(Slots.All, ItemStack(Material.BLACK_STAINED_GLASS_PANE))

            placeholder(Slots.RowTwoSlotFour, ItemStack(Material.AIR))

            placeholder(Slots.RowTwoSlotSix, ItemStack(Material.BARRIER))
        }

        // Process
        page(2) {
            placeholder(Slots.All, ItemStack(Material.BLACK_STAINED_GLASS_PANE))

            // The item
            button(Slots.RowTwoSlotFour, ItemStack(Material.AIR)) {
                val selectedItemStack = it.bukkitEvent.inventory.getItem(12) ?: ItemStack(Material.AIR)
                val failedToAdd = it.player.inventory.addItem(selectedItemStack).size != 0
                if(failedToAdd) {
                    it.player.world.dropItemNaturally(it.player.location, selectedItemStack)
                }

                it.guiInstance.gotoPage(1)
            }

            // Start button
            button(Slots.RowTwoSlotSix, ItemStack(Material.ANVIL)) { e ->
                val list = listOf(
                    listOf(Slots.ColumnOne),
                    listOf(Slots.ColumnTwo),
                    listOf(Slots.ColumnThree),
                    listOf(Slots.RowOneSlotFour, Slots.RowThreeSlotFour),
                    listOf(Slots.ColumnFive),
                    listOf(Slots.RowOneSlotSix, Slots.RowThreeSlotSix),
                    listOf(Slots.ColumnSeven),
                    listOf(Slots.ColumnEight),
                    listOf(Slots.ColumnNine)
                )

                list.flatten().forEach {
                    e.guiInstance[it] = ItemStack(Material.RED_STAINED_GLASS_PANE)
                }

                val inv = e.bukkitEvent.clickedInventory
                CoroutineScope(Dispatchers.Default).launch {
                    list.forEach {
                        delay(500)
                        it.forEach { lower ->
                            e.guiInstance[lower] = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
                        }
                    }
                }

                e.player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_ANVIL_PLACE.key, Sound.Source.BLOCK, 1f, 1f))
            }
        }

        onClose {
            if(it.guiInstance.currentPageInt == 2) {
                val selectedItemStack = it.bukkitEvent.inventory.getItem(12) ?: ItemStack(Material.AIR)
                val failedToAdd = it.player.inventory.addItem(selectedItemStack).size != 0
                if(failedToAdd) {
                    it.player.world.dropItemNaturally(it.player.location, selectedItemStack)
                }
            }
        }
    }

    val successGui = kSpigotGUI(GUIType.THREE_BY_NINE) {
        page(1) {
            val list = listOf(
                Slots.ColumnOne to Material.DIAMOND,
                Slots.ColumnTwo to Material.EMERALD,
                Slots.ColumnThree to Material.DIAMOND,
                Slots.ColumnFour to Material.EMERALD,
                Slots.ColumnFive to Material.DIAMOND,
                Slots.ColumnSix to Material.EMERALD,
                Slots.ColumnSeven to Material.DIAMOND,
                Slots.ColumnEight to Material.EMERALD,
                Slots.ColumnNine to Material.DIAMOND,

                Slots.RowTwo to Material.EMERALD,
                Slots.RowTwoSlotTwo to Material.DIAMOND,
                Slots.RowTwoSlotFour to Material.DIAMOND,
                Slots.RowTwoSlotFive to Material.AIR,
                Slots.RowTwoSlotSix to Material.DIAMOND,
                Slots.RowTwoSlotEight to Material.DIAMOND,
            )

            list.forEach {
                placeholder(it.first, ItemStack(it.second))
            }
        }
    }

    val failGui = kSpigotGUI(GUIType.THREE_BY_NINE) {
        page(1) {
            val list = listOf(
                Slots.ColumnOne to Material.TNT,
                Slots.ColumnTwo to Material.LAVA_BUCKET,
                Slots.ColumnThree to Material.TNT,
                Slots.ColumnFour to Material.LAVA_BUCKET,
                Slots.ColumnFive to Material.TNT,
                Slots.ColumnSix to Material.LAVA_BUCKET,
                Slots.ColumnSeven to Material.TNT,
                Slots.ColumnEight to Material.LAVA_BUCKET,
                Slots.ColumnNine to Material.TNT,

                Slots.RowTwo to Material.LAVA_BUCKET,
                Slots.RowTwoSlotTwo to Material.TNT,
                Slots.RowTwoSlotFour to Material.TNT,
                Slots.RowTwoSlotFive to Material.AIR,
                Slots.RowTwoSlotSix to Material.TNT,
                Slots.RowTwoSlotEight to Material.TNT,
            )

            list.forEach {
                placeholder(it.first, ItemStack(it.second))
            }
        }
    }

    fun init() {
        listen<InventoryClickEvent> {
            // Click player inventory while gui opened
            val instance = GUIHolderWrapper.registered[it.inventory]
            if(instance?.gui == gui && it.clickedInventory == it.whoClicked.inventory) {
                if(it.currentItem != null) {
                    it.isCancelled = true
                    instance.gotoPage(2, PageChangeEffect.INSTANT)
                    instance[Slots.RowTwoSlotFour] = it.currentItem!!
                    it.clickedInventory?.setItem(it.slot, null)
                }
            }
        }
    }

}
