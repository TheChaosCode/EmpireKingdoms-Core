package nl.TheChaosCode.EmpireCore.listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.TheChaosCode.EmpireCore.Main;

public class Interact_Listener implements Listener 
{
	@EventHandler
	public void interact(PlayerInteractEvent e)
	{
		ArrayList<Material> allowed = new ArrayList<Material>();
		for(String s : Main.r.getConfig().getStringList("use_materials"))
		{
			allowed.add(Material.valueOf(s.toUpperCase()));
		}
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(allowed.contains(e.getClickedBlock().getType()))
			{
				if(Main.r.checkIfPlayerCanBuild(e.getPlayer(), 2, e.getClickedBlock()))
				{
					e.getPlayer().sendMessage(ChatColor.RED + "Je hebt te weinig influence hiervoor.");
					e.setCancelled(true);
				}
			}
		}
	}
}