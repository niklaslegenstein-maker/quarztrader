package de.quartztrader;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TradeListener implements Listener {

    private final QuartzTrader plugin;

    public TradeListener(QuartzTrader plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Villager villager)) return;
        if (!plugin.getProfessions().contains(villager.getProfession())) return;

        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        if (pdc.has(plugin.getRolledKey(), PersistentDataType.BYTE)) return; // schon gewürfelt

        // Nur einmal pro Villager würfeln (wird gespeichert)
        pdc.set(plugin.getRolledKey(), PersistentDataType.BYTE, (byte) 1);

        if (ThreadLocalRandom.current().nextDouble() * 100.0 >= plugin.getChance()) return;

        MerchantRecipe recipe = new MerchantRecipe(
                new ItemStack(Material.QUARTZ, plugin.getQuartz()),
                0,
                plugin.getMaxUses(),
                true,
                plugin.getVillagerXp(),
                0.05f);
        recipe.addIngredient(new ItemStack(Material.EMERALD, plugin.getEmeralds()));

        List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());
        recipes.add(recipe);
        villager.setRecipes(recipes);
    }
}
