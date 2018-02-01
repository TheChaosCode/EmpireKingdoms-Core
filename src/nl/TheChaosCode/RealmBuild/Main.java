package nl.TheChaosCode.RealmBuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import nl.TheChaosCode.RealmBuild.events.CustomScoreBoard;
import nl.TheChaosCode.RealmBuild.listeners.BlockBreak_Listener;
import nl.TheChaosCode.RealmBuild.listeners.BlockPlace_Listener;
import nl.TheChaosCode.RealmBuild.listeners.Chat_Listener;
import nl.TheChaosCode.RealmBuild.listeners.Command_Listener;
import nl.TheChaosCode.RealmBuild.listeners.Dead_Listener;
import nl.TheChaosCode.RealmBuild.listeners.Interact_Listener;
import nl.TheChaosCode.RealmBuild.listeners.Join_Listener;
import nl.TheChaosCode.RealmBuild.listeners.Quit_Listener;

public class Main extends JavaPlugin implements Listener
{
	public static Main r;
	public FileConfiguration c;
	private PlayerPoints playerPoints;
	
	public boolean war;
	public int wartime;
	public HashMap<String, Integer> killsPerKD = new HashMap<String, Integer>();
	
	public static Permission permission = null;
	
	public HashMap<Player, String> prefix = new HashMap<Player, String>();
	
	public ArrayList<Player> spy = new ArrayList<Player>();
	
	public void onEnable()
	{
		r = this;
		saveDefaultConfig();
		c = getConfig();
		war = false;
		wartime = 0;
		
		hookPlayerPoints();
		setupPermissions();
		
		Bukkit.getPluginManager().registerEvents(new BlockBreak_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlace_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Interact_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Chat_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Dead_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Join_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Quit_Listener(), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		
		getCommand("oorlog").setExecutor(new Command_Listener());
		getCommand("k").setExecutor(new Command_Listener());
		getCommand("kingdom").setExecutor(new Command_Listener());
		getCommand("kspy").setExecutor(new Command_Listener());
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
				{
					@SuppressWarnings("deprecation")
					@Override
					public void run() 
					{
						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(playerPoints.getAPI().look(p.getName()) < 1000)
							{
								playerPoints.getAPI().give(p.getUniqueId(), 1);
							}
						}
					}
				}, 0, 1200);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run() 
			{
				CustomScoreBoard csb = new CustomScoreBoard();
				for(Player p : Bukkit.getOnlinePlayers())
				{
					csb.setScoreboard(p);
				}
				if(war == true)
				{
					if(wartime-4 > 0)
					{
						wartime = wartime-5;
					}else{
						stop_war();
					}
				}
			}
		}, 0, 100);
	}
	public void start_war()
	{
		Main.r.war = true;
		Main.r.wartime = 60*60*3;
		for(Player wp : Bukkit.getOnlinePlayers())
		{
			Main.r.sendScreenText(wp, ChatColor.RED + "OORLOG!", ChatColor.RED + "Help je land en vecht mee!", 10, 60, 10);
		}
	}
	public void stop_war()
	{
		war = false;
		String winnaar = "";
		int highst = 0;
		for(String str : Main.r.killsPerKD.keySet())
		{
			if(Main.r.killsPerKD.get(str) > highst)
			{
				winnaar = str;
				highst = Main.r.killsPerKD.get(str);
			}
		}
		for(Player p : Bukkit.getOnlinePlayers())
		{
			Main.r.sendScreenText(p, ChatColor.RED + "Einde!", ChatColor.RED + "De oorlog is afgelopen, " + winnaar + ChatColor.RED + " heeft gewonnen.", 10, 60, 10);
		}
		Main.r.killsPerKD.clear();
	}
	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	public PlayerPoints getPlayerPoints()
	{
		return playerPoints;
	}
	private boolean hookPlayerPoints() 
	{
	    final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
	    playerPoints = PlayerPoints.class.cast(plugin);
	    return playerPoints != null; 
	}
	public List<String> getAllKingdoms()
	{
		return c.getStringList("kingdoms");
	}
	public ArrayList<String> getAllKingdomsWithColor()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(String str : getAllKingdoms())
		{
			list.add(Main.r.getTextColor(str));
		}
		
		return list;
	}
	@SuppressWarnings("deprecation")
	public String getInWitchKingdomAPlayerIs(Player p, Block b)
	{
		String inKingdom = "-";
		Location down = b.getLocation();
		down.setY(0);
		b = down.getBlock();
		for(String s : getAllKingdoms())
		{
			if(c.contains("kingdomblocks." + s))
			{
				String item[] = c.getString("kingdomblocks." + s).split(":");
				if(b.getType().toString().equals(item[0].toUpperCase()) && b.getData() == Integer.parseInt(item[1]))
				{
					inKingdom = s;
					break;
				}
			}
		}
		
		return inKingdom;
	}
	@SuppressWarnings("deprecation")
	public String getInWitchKingdomAPlayerIs(Player p)
	{
		return Main.r.getTextColor(String.valueOf(Main.permission.getPlayerGroups(p.getWorld(), p.getName())[0]));
	}
	public boolean checkIfPlayerCanBuild(Player p, int build, Block b)
	{
		/*
		 * 0 build
		 * 1 break
		 * 2 use
		 */
		
		String inKingdom = getInWitchKingdomAPlayerIs(p,b);
		if(inKingdom != null)
		{
			if(inKingdom.equals("-")) return false;
			if(!p.hasPermission("kingdom." + inKingdom))
			{
				//pay
				int price = c.getInt((build == 0 ? "build_cost" : (build == 1 ? "break_cost" : "use_cost")));
				int balance = playerPoints.getAPI().look(p.getUniqueId());
				if(balance >= price)
				{
					playerPoints.getAPI().take(p.getUniqueId(), price);
					if(build == 0)
					{
						p.sendMessage(ChatColor.GREEN + "Je hebt " + price + " betaald voor een blokje te plaatsen hier.");
					}
					else if(build == 1)
					{
						p.sendMessage(ChatColor.GREEN + "Je hebt " + price + " betaald voor een blokje te slopen hier.");
					}
					else if(build == 2)
					{
						p.sendMessage(ChatColor.GREEN + "Je hebt " + price + " betaald voor het gebruiken van dit blokje.");
					}
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}
	public String getColorWithSpecials(Player p)
	{
		String in = getColor(p);
		if(p.hasPermission("scoreboard.tablist.Obfuscated"))
		{
			in = in + "&k";
		}
		if(p.hasPermission("scoreboard.tablist.Bold"))
		{
			in = in + "&l";
		}
		if(p.hasPermission("scoreboard.tablist.Strikethrough"))
		{
			in = in + "&m";
		}
		if(p.hasPermission("scoreboard.tablist.Underline"))
		{
			in = in + "&n";
		}
		if(p.hasPermission("scoreboard.tablist.Italic"))
		{
			in = in + "&o";
		}
		if(p.hasPermission("scoreboard.tablist.Reset"))
		{
			in = in + "&r";
		}
		/*	
		 	§k	Obfuscated
			§l	Bold
			§m	Strikethrough
			§n	Underline
			§o	Italic
			§r	Reset
		*/
		return in;
	}
	public String getColor(Player p)
	{
		if(p.hasPermission("scoreboard.tablist.white"))
		{
			return "&f";
		}
		if(p.hasPermission("scoreboard.tablist.black"))
		{
			return "&0";
		}
		else if(p.hasPermission("scoreboard.tablist.dark_blue"))
		{
			return "&1";
		}
		else if(p.hasPermission("scoreboard.tablist.dark_green"))
		{
			return "&2";
		}
		else if(p.hasPermission("scoreboard.tablist.dark_aqua"))
		{
			return "&3";
		}
		else if(p.hasPermission("scoreboard.tablist.dark_red"))
		{
			return "&4";
		}
		else if(p.hasPermission("scoreboard.tablist.dark_purple"))
		{
			return "&5";
		}
		else if(p.hasPermission("scoreboard.tablist.gold"))
		{
			return "&6";
		}
		else if(p.hasPermission("scoreboard.tablist.gray"))
		{
			return "&7";
		}
		else if(p.hasPermission("scoreboard.tablist.dark_gray"))
		{
			return "&8";
		}
		else if(p.hasPermission("scoreboard.tablist.blue"))
		{
			return "&9";
		}
		else if(p.hasPermission("scoreboard.tablist.green"))
		{
			return "&a";
		}
		else if(p.hasPermission("scoreboard.tablist.aqua"))
		{
			return "&b";
		}
		else if(p.hasPermission("scoreboard.tablist.red"))
		{
			return "&c";
		}
		else if(p.hasPermission("scoreboard.tablist.light_purple"))
		{
			return "&d";
		}
		else if(p.hasPermission("scoreboard.tablist.yellow"))
		{
			return "&e";
		}
		else if(p.hasPermission("scoreboard.tablist.black"))
		{
			return "&0";
		}
		/*
		§0	Black	black	0	0	0	000000	0	0	0	000000
		§1	Dark Blue	dark_blue	0	0	170	0000AA	0	0	42	00002A
		§2	Dark Green	dark_green	0	170	0	00AA00	0	42	0	002A00
		§3	Dark Aqua	dark_aqua	0	170	170	00AAAA	0	42	42	002A2A
		§4	Dark Red	dark_red	170	0	0	AA0000	42	0	0	2A0000
		§5	Dark Purple	dark_purple	170	0	170	AA00AA	42	0	42	2A002A
		§6	Gold	gold	255	170	0	FFAA00	42	42	0	2A2A00
		§7	Gray	gray	170	170	170	AAAAAA	42	42	42	2A2A2A
		§8	Dark Gray	dark_gray	85	85	85	555555	21	21	21	151515
		§9	Blue	blue	85	85	255	5555FF	21	21	63	15153F
		§a	Green	green	85	255	85	55FF55	21	63	21	153F15
		§b	Aqua	aqua	85	255	255	55FFFF	21	63	63	153F3F
		§c	Red	red	255	85	85	FF5555	63	21	21	3F1515
		§d	Light Purple	light_purple	255	85	255	FF55FF	63	21	63	3F153F
		§e	Yellow	yellow	255	255	85	FFFF55	63	63	21	3F3F15
		§f	White	white	255	255	255	FFFFFF	63	63	63	3F3F3F
		*/
		return "&f";
	}
	public String getTextColor(String s)
	{
		if(c.contains("colors." + s))
		{
			return ChatColor.translateAlternateColorCodes('&', c.getString("colors." + s));
		}
		else
		{
			return s;
		}
	}
	public void sendScreenText(Player p, String message, String submessage, int in, int stay, int out)
	{
		PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
	    PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, in, stay, out);
	    connection.sendPacket(packetPlayOutTimes);
	    if (message != null)
	    {
	    	IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', submessage) + "\"}");
	    	PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
	    	connection.sendPacket(packetPlayOutSubTitle);
	    }
	    if (submessage != null)
	    {
	     	IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
	     	PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
	     	connection.sendPacket(packetPlayOutTitle);
	    }
	}
	@EventHandler
	public void command(PlayerCommandPreprocessEvent e)
	{
		if(e.getMessage().equalsIgnoreCase("/list"))
		{
			e.setCancelled(true);
			if(e.getPlayer().hasPermission("kingdom.list"))
			{
				Player p = e.getPlayer();
				p.sendMessage(ChatColor.YELLOW + "Er zijn in totaal " + Bukkit.getOnlinePlayers().size() + " spelers online.");
				for(String str : this.getAllKingdomsWithColor())
				{
					ArrayList<Player> list = new ArrayList<Player>();
					for(Player wp : Bukkit.getOnlinePlayers())
					{
						if(this.getInWitchKingdomAPlayerIs(wp).equals(str))
						{
							list.add(wp);
						}
					}
					String out = ChatColor.GRAY + "[" + str + ChatColor.GRAY + "] (" + list.size() + "): ";
					for(int i = 0; i < list.size(); i++)
					{
						out += ChatColor.WHITE + list.get(i).getName();
						if(i+1 < list.size()) out += ChatColor.GREEN + ", ";
					}
					p.sendMessage(out);
				}
			}
			else
			{
				e.getPlayer().sendMessage(ChatColor.RED + "You don't got the permissions for that command.");
			}
		}
	}
}