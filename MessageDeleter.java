package de.ben.lukasderpeter.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageDeleter {

    private int taskID;

    MessageDeleter() {

    }

    public void deleteMessegers(Player target, Player sender) {

        Main.getMessageMap().get(target).forEach((p) -> {
            if (p != sender) {
                Main.getMessageMap().get(target).remove(p);
            }
        });
    }

    void deleteMessegersDelayed(Player target, Player sender) {

        taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {

            Main.getMessageMap().get(target).forEach((p) -> {
                if (p != sender) {
                    Main.getMessageMap().get(target).remove(p);
                }
            });
        }, 20*20);

        Main.getTaskIDs().put(target, taskID);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
