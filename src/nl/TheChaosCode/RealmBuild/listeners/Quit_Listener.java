package nl.TheChaosCode.RealmBuild.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.TheChaosCode.RealmBuild.events.CustomScoreBoard;

public class Quit_Listener implements Listener
{
	@EventHandler
	public void quit(PlayerQuitEvent e)
	{
		new CustomScoreBoard().delPrefix(e.getPlayer());
	}
}
