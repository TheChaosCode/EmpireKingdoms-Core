package nl.TheChaosCode.RealmBuild.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.TheChaosCode.RealmBuild.Main;
import nl.TheChaosCode.RealmBuild.events.CustomScoreBoard;

public class Join_Listener implements Listener
{
	@EventHandler
	public void join(PlayerJoinEvent e)
	{
		CustomScoreBoard csb = new CustomScoreBoard();
		csb.setPrefix(e.getPlayer(), Main.r.getColorWithSpecials(e.getPlayer()));
		csb.setScoreboard(e.getPlayer());
		
		if(!Main.r.c.contains("users." + e.getPlayer().getUniqueId().toString()))
		{
			Main.r.c.set("users."  + e.getPlayer().getUniqueId().toString() + ".rank", 0);
			Main.r.saveConfig();
		}
	}
}