package com.ashkiano.keepinvstick;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

//TODO toto pouze jako levný placený plugin
//TODO pridat bstats
//TODO pridat preklady
//TODO pridat permisi na použití toho itemu
//TODO udělat možnost dropu z různých mobu s různou pravděpodobností
//TODO přidat možnost custom itemu z configu jako stick
//TODO přidat možnost nastavit popisky a název itemu v configu
public class KeepInvStick extends JavaPlugin implements Listener {

    private ItemStack specialItem;
    private String permissionNode;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 20870);

        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig(); // creates a config.yml if it does not exist

        specialItem = new ItemStack(Material.DIAMOND); // Select any material for the custom item
        ItemMeta meta = specialItem.getItemMeta();

        meta.setDisplayName("One Time Use Item");
        meta.setLore(Arrays.asList("Keeps inventory upon death once"));
        specialItem.setItemMeta(meta);

        permissionNode = this.getConfig().getString("permission-node", "keepinvstick.give");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givekeepinvstick")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission(permissionNode)) {
                    player.getInventory().addItem(specialItem);
                    sender.sendMessage("You've received a One Time Use Item!");
                    return true;
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getInventory().contains(specialItem)) {
            player.getInventory().remove(specialItem);
            event.setKeepInventory(true);
        } else {
            event.setKeepInventory(false);
        }
    }
}
