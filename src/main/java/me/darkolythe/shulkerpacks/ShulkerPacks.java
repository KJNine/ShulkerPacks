package me.darkolythe.shulkerpacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShulkerPacks extends JavaPlugin {

    ShulkerListener shulkerlistener;
    WorldGuardListener compatWorldGuardListener;
    GriefPreventionListener compatGriefPreventionListener;

    String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "ShulkerPacks" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    Map<Player, ItemStack> openshulkers = new HashMap<>();
    Map<Player, Boolean> fromhand = new HashMap<>();
    Map<UUID, Inventory> openinventories = new HashMap<>();
    Map<Player, Inventory> opencontainer = new HashMap<>();
    boolean canopeninchests = true;
    List<String> blacklist = new ArrayList<>();
    String defaultname = ChatColor.BLUE + "Shulker Pack";
    boolean shiftclicktoopen = false;
    boolean canopeninenderchest, canopeninbarrels, canplaceshulker, canopenininventory, canopeninair, openonlyinclaim;
    float volume;

    /*
    Sets up the plugin
     */
    @Override
    public void onEnable() {
        shulkerlistener = new ShulkerListener(this);

        getServer().getPluginManager().registerEvents(shulkerlistener, this);
        
        if(getServer().getPluginManager().getPlugin("WorldGuard") != null) {
        	compatWorldGuardListener = new WorldGuardListener(this);
        }

        saveDefaultConfig();
        canopeninchests = getConfig().getBoolean("canopeninchests");
        canopeninenderchest = getConfig().getBoolean("canopeninenderchest", true);
        canopeninbarrels = getConfig().getBoolean("canopeninbarrels", true);
        canopenininventory = getConfig().getBoolean("canopenininventory", true);
        canplaceshulker = getConfig().getBoolean("canplaceshulker", true);
        blacklist = getConfig().getStringList("blacklistedinventories");
        canopeninair = getConfig().getBoolean("canopeninair", true);
        openonlyinclaim = getConfig().getBoolean("openonlyinclaim", false);
        volume = (float) getConfig().getDouble("shulkervolume", 1.0);
        if (getConfig().getString("defaultname") != null) {
            defaultname = ChatColor.translateAlternateColorCodes('&', getConfig().getString("defaultname"));
        }
        shiftclicktoopen = getConfig().getBoolean("shiftclicktoopen");

        @SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);

        shulkerlistener.checkIfValid();

        System.out.println(prefix + ChatColor.GREEN + "ShulkerPacks has been enabled!");
    }

    /*
    Doesnt do much. Just says a message
     */
    @Override
    public void onDisable() {
        System.out.println(prefix + ChatColor.RED + "ShulkerPacks has been disabled!");
    }
}
