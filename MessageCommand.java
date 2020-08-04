package de.ben.lukasderpeter.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }
        Player sender = (Player) commandSender;
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /msg [Player] [Message]");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage("not-online");
            return true;
        }
        if (sender == target) {
            sender.sendMessage("not-text-oneself");
            return true;
        }
        StringBuilder message = new StringBuilder();
        message.append(" ");

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        sender.sendMessage("sender-target-message" + message);
        target.sendMessage("target-sender-message" + message);

        // no existing entry for target
        if (!Main.getMessageMap().containsKey(target)) {
            List<Player> list = new ArrayList<>();
            list.add(sender);
            Main.getMessageMap().put(target, list);
            // no further action
            return true;
        } // existing entry, but empty list (can't happen, but ok)
        if (Main.getMessageMap().get(target).isEmpty()) {
            Main.getMessageMap().get(target).add(sender);
            // no further action
            return true;
        } // existing entry, but not in the list
        if (!Main.getMessageMap().get(target).contains(sender)) {
            Main.getMessageMap().get(target).add(0, sender);
            // removing other entries in 20 seconds
            MessageDeleter md = new MessageDeleter();
            md.deleteMessegersDelayed(target, sender);
            return true;
        } // existing entry and in the list
        if (Main.getMessageMap().get(target).contains(sender)) {
            Main.getMessageMap().remove(sender).add(0, sender);
            // removing other entries in 20 seconds
            MessageDeleter md = new MessageDeleter();
            md.deleteMessegersDelayed(target, sender);
            return true;
        }

        return true;
    }
}
