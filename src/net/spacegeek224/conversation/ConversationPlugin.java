
package net.spacegeek224.conversation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

import net.spacegeek224.conversation.command.CommandMessage;

public class ConversationPlugin extends JavaPlugin {
	public String[] configlist = { "config.yml", "db.yml" };

	public Map<String, FileConfiguration> configs;

	@Override
	public void onEnable() {
		
	    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		// this.saveDefaultConfig();

		if (!getDataFolder().exists()) {
			getServer().getLogger().log(Level.WARNING, "Folder does not exist");
			getDataFolder().mkdir();
		}
		for (String name : configlist) {
			File configFile = new File(getDataFolder(), name);
			if (!configFile.exists()) {
				try {
					configFile.createNewFile();
					getServer().getLogger().log(Level.WARNING, "Creating file: " + name);
					try (InputStream is = getResource(name); OutputStream os = new FileOutputStream(configFile)) {
						ByteStreams.copy(is, os);
					}
				} catch (IOException e) {
					getServer().getLogger().log(Level.SEVERE, "Can't create config file");
					throw new RuntimeException("Unable to create configuration file", e);
				}
			}
		}
		registerCommands();
		getServer().getLogger().info("Conversation is up and running!");
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
		// this.saveConfig();
		getServer().getLogger().info("Conversation is shutting down...");
	}

	public void registerCommands() {
		  this.getCommand("message").setExecutor(new CommandMessage(this));
		  this.getCommand("tell").setExecutor(new CommandMessage(this));
		  this.getCommand("msg").setExecutor(new CommandMessage(this));
	}

}
