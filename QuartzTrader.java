package de.quartztrader;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class QuartzTrader extends JavaPlugin {

    private final Set<Villager.Profession> professions = new HashSet<>();
    private double chance;
    private int emeralds, quartz, maxUses, villagerXp;
    private NamespacedKey rolledKey;

    @Override
    public void onEnable() {
        rolledKey = new NamespacedKey(this, "quartz_rolled");
        saveDefaultConfig();
        loadSettings();
        getServer().getPluginManager().registerEvents(new TradeListener(this), this);
    }

    public void loadSettings() {
        reloadConfig();
        chance = getConfig().getDouble("chance", 30.0);
        emeralds = Math.max(1, Math.min(64, getConfig().getInt("trade.emeralds", 3)));
        quartz = Math.max(1, Math.min(64, getConfig().getInt("trade.quartz", 1)));
        maxUses = Math.max(1, getConfig().getInt("trade.max-uses", 12));
        villagerXp = Math.max(0, getConfig().getInt("trade.villager-xp", 5));

        professions.clear();
        for (String name : getConfig().getStringList("professions")) {
            NamespacedKey key = NamespacedKey.fromString(name.toLowerCase());
            Villager.Profession p = key == null ? null : Registry.VILLAGER_PROFESSION.get(key);
            if (p != null) {
                professions.add(p);
            } else {
                getLogger().warning("Unbekannter Beruf in config.yml: " + name);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            loadSettings();
            sender.sendMessage("§aQuartzTrader neu geladen.");
            return true;
        }
        sender.sendMessage("§e/quartztrader reload");
        return true;
    }

    public Set<Villager.Profession> getProfessions() { return professions; }
    public double getChance() { return chance; }
    public int getEmeralds() { return emeralds; }
    public int getQuartz() { return quartz; }
    public int getMaxUses() { return maxUses; }
    public int getVillagerXp() { return villagerXp; }
    public NamespacedKey getRolledKey() { return rolledKey; }
}
