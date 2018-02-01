package nl.TheChaosCode.EmpireCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import nl.TheChaosCode.EmpireCore.Main;

public class Chat_Listener implements Listener 
{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void chat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		
		String Rank = ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + p.getUniqueId().toString() + ".rank")) + ".prefix"));
		Rank = (ChatColor.stripColor(Rank).length() == 0 ? Rank : Rank + " ");
		String Kingdom = ChatColor.GRAY + "[" + Main.r.getInWitchKingdomAPlayerIs(p)  + ChatColor.GRAY + "] ";
		if(e.getMessage().startsWith("!"))
		{
			e.setCancelled(true);
			for(Player wp : Bukkit.getOnlinePlayers())
			{
				String chanel = ChatColor.GRAY + "{" + ChatColor.WHITE + "Roleplay" + ChatColor.GRAY + "} ";
				wp.sendMessage(chanel + Kingdom + Rank + p.getDisplayName() + ": " + (p.hasPermission("kingdom.coloredchat") ? ChatColor.translateAlternateColorCodes('&', e.getMessage().substring(1)) : e.getMessage().substring(1)));
			}
		}
		else if(e.getMessage().startsWith("$"))
		{
			e.setCancelled(true);
			for(Player wp : Bukkit.getOnlinePlayers())
			{
				String chanel = ChatColor.GRAY + "{" + ChatColor.BLUE + "Trade" + ChatColor.GRAY + "} ";
				wp.sendMessage(chanel + Kingdom + Rank + p.getDisplayName() + ": " + (p.hasPermission("kingdom.coloredchat") ? ChatColor.translateAlternateColorCodes('&', e.getMessage().substring(1)) : e.getMessage().substring(1)));
			}
		}
		else if(e.getMessage().startsWith("%"))
		{
			e.setCancelled(true);
			if(p.hasPermission("kingdom.hkm"))
			{
				for(Player wp : Bukkit.getOnlinePlayers())
				{
					if(wp.hasPermission("kingdombuild.hkm"))
					{
						String chanel = ChatColor.GRAY + "{" + ChatColor.GOLD + "HKM" + ChatColor.GRAY + "} ";
						wp.sendMessage(chanel + Kingdom + Rank + p.getDisplayName() + ": " + (p.hasPermission("kingdom.coloredchat") ? ChatColor.translateAlternateColorCodes('&', e.getMessage().substring(1)) : e.getMessage().substring(1)));
					}
				}
			}
			else
			{
				p.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
			}
		}
		else if(e.getMessage().startsWith("*"))
		{
			e.setCancelled(true);
			if(p.hasPermission("kingdom.staff"))
			{
				for(Player wp : Bukkit.getOnlinePlayers())
				{
					String chanel = ChatColor.WHITE + "{" + ChatColor.DARK_AQUA + "Staff" + ChatColor.WHITE + "} ";
					wp.sendMessage(chanel + Kingdom + Rank + p.getDisplayName() + ": " + (p.hasPermission("kingdom.coloredchat") ? ChatColor.translateAlternateColorCodes('&', e.getMessage().substring(1)) : e.getMessage().substring(1)));
				}
			}
			else
			{
				p.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
			}
		}
		else
		{
			e.setCancelled(true);
			if(Main.r.getInWitchKingdomAPlayerIs(p).equals("geen")) return;
			for(Player wp : Bukkit.getOnlinePlayers())
			{
				if((Main.permission.getPlayerGroups(wp.getWorld(), wp.getName())[0] != null ? Main.r.getTextColor(String.valueOf(Main.permission.getPlayerGroups(wp.getWorld(), wp.getName())[0])) : "Onbekend").equals(Main.r.getInWitchKingdomAPlayerIs(p)) || Main.r.spy.contains(wp))
				{
					wp.sendMessage(Kingdom + Rank + p.getDisplayName() + ": " + (p.hasPermission("kingdom.coloredchat") ? ChatColor.translateAlternateColorCodes('&', e.getMessage()) : e.getMessage()));
				}
			}	
		}
	}
}
