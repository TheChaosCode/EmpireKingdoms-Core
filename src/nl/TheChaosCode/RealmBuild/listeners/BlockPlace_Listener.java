package nl.TheChaosCode.RealmBuild.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import nl.TheChaosCode.RealmBuild.Main;

public class BlockPlace_Listener implements Listener 
{
	@EventHandler
	public void blockPlace(BlockPlaceEvent e)
	{
		if(Main.r.checkIfPlayerCanBuild(e.getPlayer(), 0, e.getBlock()))
		{
			e.getPlayer().sendMessage(ChatColor.RED + "Je hebt te weinig influence hiervoor.");
			e.setCancelled(true);
		}
	}
}
