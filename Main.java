package de.ben.lukasderpeter.main;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    private static Plugin instance;
    private static HashMap<Player, List<Player>> messageMap;
    private static HashMap<Player, Integer> taskIDs;

    public void onEnable() {

        System.out.println("HI");
        instance = this;
        messageMap = new HashMap<>();
        taskIDs = new HashMap<>();
        getCommand("msg").setExecutor(new MessageCommand());
        getCommand("r").setExecutor(new RespondCommand());
    }

    public void onDisable() {

    }

    public static Plugin getInstance() {
        return instance;
    }

    public static HashMap<Player, List<Player>> getMessageMap() {
        return messageMap;
    }

    public static HashMap<Player, Integer> getTaskIDs() {
        return taskIDs;
    }
}
