package com.griefdefender.hooks.provider;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustTypes;
import com.griefdefender.hooks.GDHooksBootstrap;
import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.api.events.custom_block.NexoBlockBreakEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;

public class NexoProvider implements Listener {
        public NexoProvider() {
		    Bukkit.getPluginManager().registerEvents(this, GDHooksBootstrap.getInstance().getLoader());
    }

    public String getItemId(ItemStack itemstack) {
        final String itemId = NexoItems.idFromItem(itemstack);
        if (itemId == null) {
            return null;
        }

        String name = itemId;
        name = name.replaceAll(" ", "\\_");
        name = name.replaceAll("[^A-Za-z0-9\\_]", "");
        return "nexo:" + name.toLowerCase();
    }

    public String getBlockId(Block block) {
        final CustomBlockMechanic itemId = NexoBlocks.customBlockMechanic(block);
        if (!NexoBlocks.isCustomBlock(block)) {
            return null;
        }

        String name = itemId.getItemID();
        name = name.replaceAll(" ", "\\_");
        name = name.replaceAll("[^A-Za-z0-9\\_]", "");
        return "nexo:" + name.toLowerCase();
    }

    @EventHandler
    public void onNexoBreakFurniture(NexoFurnitureBreakEvent event){
        Claim claim = GriefDefender.getCore().getClaimAt(event.getBaseEntity().getLocation());
        User user = GriefDefender.getCore().getUser(event.getPlayer().getUniqueId());

        if (claim != null && !claim.isWilderness()) {
            if(!user.canBreak(event.getBaseEntity().getLocation())){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onNexoBreakCustomBlock(NexoBlockBreakEvent event){
        Claim claim = GriefDefender.getCore().getClaimAt(event.getBlock().getLocation());
        User user = GriefDefender.getCore().getUser(event.getPlayer().getUniqueId());

        if (claim != null && !claim.isWilderness()) {
            if(!user.canBreak(event.getBlock().getLocation())){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onNexoInteractFurniture(NexoFurnitureInteractEvent event){
        Claim claim = GriefDefender.getCore().getClaimAt(event.getBaseEntity().getLocation());
        User user = GriefDefender.getCore().getUser(event.getPlayer().getUniqueId());

        if (claim != null && !claim.isWilderness()) {
            if(!user.canInteractWithEntity(null, event.getBaseEntity(), TrustTypes.ACCESSOR)){
                event.setCancelled(true);
                return;
            }
        }
    }
}
