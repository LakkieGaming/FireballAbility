package com.schiebros.fireball;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public Map<Player, Long> cooldown = new HashMap<Player, Long>();

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STICK))
			return;
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;
		if (cooldown.containsKey(e.getPlayer())) {
			long timeLeft = System.currentTimeMillis() - cooldown.get(e.getPlayer());
			e.getPlayer().sendMessage("§cWait " + timeLeft / 1000 + " seconds.");
			return;
		}
		Fireball f = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Fireball.class);
		f.setIsIncendiary(false);
		f.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2));
		cooldown.put(e.getPlayer(), System.currentTimeMillis());
		e.getPlayer().sendMessage("§9You used §6Fireball§9.");
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> cancel(e.getPlayer()), 40l);
	}

	public void cancel(Player p) {
		cooldown.remove(p);
		p.sendMessage("§9You can now use §6Fireball§9.");
	}

}
