package io.github.calemyoung.pickupspawners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener 
{
	@Override
	public void onEnable() 
	{
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) 
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(player.getWorld().getName().equalsIgnoreCase("world"))
		{
			if(block.getType() == Material.MOB_SPAWNER)
			{
				if (player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) 
				{
					CreatureSpawner spawner = (CreatureSpawner) block.getState();
					event.setExpToDrop(0);
	                ItemStack drop = new ItemStack(Material.MOB_SPAWNER, 1);
	                ItemMeta itemMeta = drop.getItemMeta();
	                List<String> lore = new ArrayList<>();
	                lore.add(spawner.getCreatureTypeName() + " Spawner");
	                itemMeta.setLore(lore);
	                drop.setItemMeta(itemMeta);
	                World world = player.getWorld();
	                world.dropItem(player.getLocation(), drop);
				}
				else
				{
					player.sendMessage(ChatColor.AQUA + "You must have silkstouch to pickup a spawner!");
					event.setCancelled(true);
				}
			}
		}
		else
		{
			if(block.getType() == Material.MOB_SPAWNER)
			{
				player.sendMessage(ChatColor.AQUA + "You can't pickup spawners in the nether!");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) 
	{
		ItemStack item = event.getItemInHand();
		if (item.getType().equals(Material.MOB_SPAWNER))
		{
			String type = item.getItemMeta().getLore().get(0);
			type = type.substring(0, type.length()-8);
			CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
			System.out.println(type);
			spawner.setCreatureTypeByName(type);
		}
	}
}
