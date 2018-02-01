package nl.TheChaosCode.RealmBuild.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import nl.TheChaosCode.RealmBuild.Main;

public class CustomScoreBoard
{
	@SuppressWarnings("deprecation")
	public void setScoreboard(Player p)
	{
		if(Main.r.war == true)
		{
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
	    
			String ping = (((CraftPlayer)p).getHandle().ping + "").replace("ping", "");
			String inKingdom = Main.r.getInWitchKingdomAPlayerIs(p, p.getLocation().getBlock());
			
			Objective objective = board.registerNewObjective(p.getName(), "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    
			String title = Main.r.getConfig().getString("scoreboard_war.title");
			objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
	    
			int totalSecs = Main.r.wartime;
			if(totalSecs < 0) totalSecs = 0;
			int hours = totalSecs / 3600;
			int minutes = (totalSecs % 3600) / 60;
			int seconds = totalSecs % 60;
			int totalDeads = 0;
			
			for(int val : Main.r.killsPerKD.values())
			{
				totalDeads += val;
			}
			
			String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);	
			
			List<String> Board = Main.r.getConfig().getStringList("scoreboard_war.board");
			for (int i = 0; i < Board.size(); i++) 
			{
				objective.getScore(ChatColor.translateAlternateColorCodes('&', ((String)Board.get(i))
						.replace("%loc%", Main.r.getTextColor(inKingdom).replace("-", ChatColor.WHITE + "Onbekend"))
						.replace("%points%", Main.r.getPlayerPoints().getAPI().look(p.getUniqueId()) + "")
						.replace("%ping%", ping)
						.replace("%duur%", timeString)
						.replace("%deelnemers%", Bukkit.getOnlinePlayers().size() +"")
						.replace("%gesneuveld%", totalDeads + "")
						.replace("%rank%", Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + p.getUniqueId().toString() + ".rank")) + ".prefix"))
						.replace("%kingdomName%", ChatColor.stripColor(Main.permission.getPlayerGroups(p.getWorld(), p.getName())[0] != null ? Main.r.getTextColor(String.valueOf(Main.permission.getPlayerGroups(p.getWorld(), p.getName())[0])) : "Onbekend")))
						.replace("%online%", Bukkit.getOnlinePlayers().size() + "")).setScore(Board.size() + Main.r.getAllKingdoms().size() - i);
			}
			List<String> Kingdoms = Main.r.getAllKingdomsWithColor();
			for (int i = 0; i < Kingdoms.size(); i++) 
			{
				objective.getScore(ChatColor.translateAlternateColorCodes('&', Main.r.getConfig().getString("scoreboard_war.kills")
						.replace("%kingdom%", Kingdoms.get(i))
						.replace("%kills%", (!Main.r.killsPerKD.containsKey(Kingdoms.get(i)) ? "0" : Main.r.killsPerKD.get(Kingdoms.get(i))+"")))).setScore(Main.r.getAllKingdoms().size() - i);
			}
			setPrefix(p, Main.r.getColorWithSpecials(p));
			for (Player wp : Main.r.prefix.keySet())
			{
				Team team = board.getTeam(((String)Main.r.prefix.get(wp))) == null ? board.registerNewTeam(((String)Main.r.prefix.get(wp))) : board.getTeam(((String)Main.r.prefix.get(wp)));
	      
				team.addEntry(wp.getName());
				team.setPrefix(((String)Main.r.prefix.get(wp)).replace("&", "§"));
			}
			p.setScoreboard(board);
		}
		else if(Main.r.war == false)
		{
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
	    
			String ping = (((CraftPlayer)p).getHandle().ping + "").replace("ping", "");
			String inKingdom = Main.r.getInWitchKingdomAPlayerIs(p, p.getLocation().getBlock());
			
			Objective objective = board.registerNewObjective(p.getName(), "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    
			objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.r.getConfig().getString("scoreboard.title")));
	    
			List<String> Board = Main.r.getConfig().getStringList("scoreboard.board");
			for (int i = 0; i < Board.size(); i++) 
			{
				objective.getScore(ChatColor.translateAlternateColorCodes('&', ((String)Board.get(i))
						.replace("%loc%", Main.r.getTextColor(inKingdom).replace("-", ChatColor.WHITE + "Onbekend"))
						.replace("%points%", Main.r.getPlayerPoints().getAPI().look(p.getUniqueId()) + "")
						.replace("%ping%", ping)
						.replace("%rank%", Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + p.getUniqueId().toString() + ".rank")) + ".prefix"))
						.replace("%kingdomName%", ChatColor.stripColor(Main.r.getInWitchKingdomAPlayerIs(p)))
						.replace("%online%", Bukkit.getOnlinePlayers().size() + ""))).setScore(Board.size() - i);
			}
			setPrefix(p, Main.r.getColorWithSpecials(p));
			for (Player wp : Main.r.prefix.keySet())
			{
				Team team = board.getTeam(((String)Main.r.prefix.get(wp))) == null ? board.registerNewTeam(((String)Main.r.prefix.get(wp))) : board.getTeam(((String)Main.r.prefix.get(wp)));
	      
				team.addEntry(wp.getName());
				team.setPrefix(((String)Main.r.prefix.get(wp)).replace("&", "§"));
			}
			p.setScoreboard(board);
		}
	}
  
	public void setPrefix(Player p, String color)
	{
		Main.r.prefix.put(p, color);
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
    
		Team team = board.getTeam(((String)Main.r.prefix.get(p))) == null ? board.registerNewTeam(((String)Main.r.prefix.get(p))) : board.getTeam(((String)Main.r.prefix.get(p)));
	      
		team.addEntry(p.getName());
		team.setPrefix(((String)Main.r.prefix.get(p)).replace("&", "§"));
	}
  
	public void delPrefix(Player p)
	{
		if (Main.r.prefix.containsKey(p)) 
		{
			Main.r.prefix.remove(p);
		}
	}
}