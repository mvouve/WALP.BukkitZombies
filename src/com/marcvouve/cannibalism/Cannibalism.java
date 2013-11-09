package com.marcvouve.cannibalism;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Cannibalism extends JavaPlugin {
	private final PlayerListener playerListener = new PlayerListener();

	@Override
	public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

}
