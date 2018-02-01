package nl.TheChaosCode.EmpireCore.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import nl.TheChaosCode.EmpireCore.Main;

public class Dead_Listener implements Listener 
{
	private HashMap<Player, Player> lastDamage = new HashMap<Player, Player>();
	
	@EventHandler
	public void dead(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player)
		{
			Player p = ((Player)e.getEntity());
			Player damager = (Player)e.getDamager();
			
			if(!Main.r.getInWitchKingdomAPlayerIs(p).equals(Main.r.getInWitchKingdomAPlayerIs(damager)))
			{
				lastDamage.put(p, damager);
			}
			else
			{
				e.setCancelled(true);
			}
		}
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow)
		{
			Player damager = (Player) ((Arrow)e.getDamager()).getShooter();
			Player p = (Player)e.getEntity();
			
			if(!Main.r.getInWitchKingdomAPlayerIs(p).equals(Main.r.getInWitchKingdomAPlayerIs(damager)))
			{
				lastDamage.put(p, damager);
			}
			else
			{
				e.setCancelled(true);
			}
		}
		if(e.getEntity() instanceof Player && e.getDamager() instanceof FishHook)
		{
			Player damager = (Player) ((FishHook)e.getDamager()).getShooter();
			Player p = (Player)e.getEntity();
			
			if(!Main.r.getInWitchKingdomAPlayerIs(p).equals(Main.r.getInWitchKingdomAPlayerIs(damager)))
			{
				lastDamage.put(p, damager);
			}
			else
			{
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void deadEvent(PlayerDeathEvent e)
	{
		Player p = ((Player)e.getEntity());
		if(lastDamage.containsKey(p) && Main.r.war == true)
		{
			Player killer = lastDamage.get(p);
			e.setDeathMessage(null);
			
			String RankK = ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + killer.getUniqueId().toString() + ".rank")) + ".prefix"));
			RankK = (ChatColor.stripColor(RankK).length() == 0 ? RankK : RankK + " ");
			
			String RankP = ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + p.getUniqueId().toString() + ".rank")) + ".prefix"));
			RankP = (ChatColor.stripColor(RankP).length() == 0 ? RankP : RankP + " ");
			
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("messages.kill")
					.replace("%player_rank%", RankP)
					.replace("%player_kingdom%", Main.r.getInWitchKingdomAPlayerIs(p))
					.replace("%player%", (p).getName())
					.replace("%killer_rank%", RankK)
					.replace("%killer_kingdom%", Main.r.getInWitchKingdomAPlayerIs(killer))
					.replace("%killer%", (killer).getName())
					));
			Main.r.killsPerKD.put(Main.r.getInWitchKingdomAPlayerIs(killer), (Main.r.killsPerKD.containsKey(Main.r.getInWitchKingdomAPlayerIs(killer)) ? Main.r.killsPerKD.get(Main.r.getInWitchKingdomAPlayerIs(killer)) : 0)+1);
			lastDamage.remove(p);
		}
		else if(lastDamage.containsKey(p))
		{
			Player killer = lastDamage.get(p);
			e.setDeathMessage(null);
			String RankK = ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + killer.getUniqueId().toString() + ".rank")) + ".prefix"));
			RankK = (ChatColor.stripColor(RankK).length() == 0 ? RankK : RankK + " ");
			
			String RankP = ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("rank." + Main.r.c.getStringList("ranks").get(Main.r.c.getInt("users." + p.getUniqueId().toString() + ".rank")) + ".prefix"));
			RankP = (ChatColor.stripColor(RankP).length() == 0 ? RankP : RankP + " ");
			
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.r.c.getString("messages.kill")
					.replace("%player_rank%", RankP)
					.replace("%player_kingdom%", Main.r.getInWitchKingdomAPlayerIs(p))
					.replace("%player%", (p).getName())
					.replace("%killer_rank%", RankK)
					.replace("%killer_kingdom%", Main.r.getInWitchKingdomAPlayerIs(killer))
					.replace("%killer%", (killer).getName())
					));
		}
	}
}
