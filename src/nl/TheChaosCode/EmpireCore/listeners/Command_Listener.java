package nl.TheChaosCode.EmpireCore.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nl.TheChaosCode.EmpireCore.Main;

public class Command_Listener implements CommandExecutor 
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(cmd.getName().equalsIgnoreCase("oorlog"))
		{
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(!p.hasPermission("oorlog.oorlog")){
					p.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
					return false;
				}
			}
			if(args.length > 0)
			{
				if(args[0].equals("stop"))
				{
					if(Main.r.war == false) return false;
					Main.r.stop_war();
					return false;
				}
				if(args[0].equals("start"))
				{
					if(Main.r.war == true) return false;
					Main.r.start_war();
					return false;
				}
				if(args.length > 1)
				{
					if(args[0].equals("addtime"))
					{
						if(Main.r.war == false) return false;
						Main.r.wartime = Main.r.wartime + Integer.parseInt(args[1]);
					}
					if(args[0].equals("deltime"))
					{
						if(Main.r.war == false) return false;
						Main.r.wartime = Main.r.wartime - Integer.parseInt(args[1]);
					}
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Niet alle values zijn gegeven.");
			}
		}
		if(cmd.getName().equalsIgnoreCase("kspy"))
		{
			if(sender instanceof Player)
			{
				Player p = (Player)sender;
				if(p.hasPermission("kingdom.spy"))
				{
					if(Main.r.spy.contains(p))
					{
						Main.r.spy.remove(p);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Je hebt de channel &7[&cSpy&7] geleft."));
						return false;
					}
					else
					{
						Main.r.spy.add(p);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Je hebt de channel &7[&cSpy&7] gejoined."));
						return false;
					}
				}
				else
				{
					p.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
					return false;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("k") || cmd.getName().equalsIgnoreCase("kingdom"))
		{
			if(args.length > 1)
			{
				if(Bukkit.getPlayer(args[1]) != null)
				{
					Player player = Bukkit.getPlayer(args[1]);
					if(args[0].equalsIgnoreCase("promote"))
					{
						if(sender instanceof Player){
							if(!((Player)sender).hasPermission("kingdom.promote"))
							{
								sender.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
								return false;
							}
						}
						List<String> ranks = Main.r.c.getStringList("ranks");
						if(ranks.size()-1 > Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank"))
						{
							Main.r.c.set("users." + player.getUniqueId().toString() + ".rank", Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank")+1);
							Main.r.saveConfig();
							if(Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank")>=Main.r.c.getInt("head"))
							{
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.r.c.getString("high_rank_command").replace("%player%", player.getName()));
							}
							return false;
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "Deze speler heeft al het hoogste rank.");
						}
					}
					else if(args[0].equalsIgnoreCase("demote"))
					{
						if(sender instanceof Player){
							if(!((Player)sender).hasPermission("kingdom.demote"))
							{
								sender.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
								return false;
							}
						}
						if(0 < Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank"))
						{
							Main.r.c.set("users." + player.getUniqueId().toString() + ".rank", Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank")-1);
							Main.r.saveConfig();
							if(Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank")>=Main.r.c.getInt("head"))
							{
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.r.c.getString("high_rank_command").replace("%player%", player.getName()));
							}
							return false;
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "Deze speler heeft al de laagste rank.");
						}
					}
					else if(args.length > 2)
					{
						if(args[0].equalsIgnoreCase("setrank"))
						{
							if(sender instanceof Player){
								if(!((Player)sender).hasPermission("kingdom.setrank"))
								{
									sender.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
									return false;
								}
							}
							List<String> ranks = Main.r.c.getStringList("ranks");
							if(ranks.contains(args[2]))
							{
								for(int i = 0; i < ranks.size(); i++)
								{
									if(ranks.get(i).equals(args[2]))
									{
										Main.r.c.set("users." + player.getUniqueId().toString() + ".rank", i);
										Main.r.saveConfig();
										if(Main.r.c.getInt("users." + player.getUniqueId().toString() + ".rank")>=Main.r.c.getInt("head"))
										{
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.r.c.getString("high_rank_command").replace("%player%", player.getName()));
										}
										return false;
									}
								}
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Die rank bestaat niet.");
							}
						}
						else if(args[0].equalsIgnoreCase("setkingdom"))
						{
							if(sender instanceof Player){
								if(!((Player)sender).hasPermission("kingdom.setkingdom"))
								{
									sender.sendMessage(ChatColor.RED + "Je hebt hier geen permissions voor.");
									return false;
								}
							}
							List<String> kingdoms = Main.r.getAllKingdoms();
							if(kingdoms.contains(args[2]))
							{
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("pex user %s group set %s", player.getName(), args[2]));
								sender.sendMessage("speler " + player.getName() + " is verzet naar " + Main.r.getInWitchKingdomAPlayerIs(player));
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Dat rijk bestaat niet.");
							}
						}
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Niet alle values zijn gegeven.");
					}
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Deze speler is niet online.");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Niet alle values zijn gegeven.");
			}
		}
		return false;
	}
}
