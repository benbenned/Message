package de.ben.lukasderpeter.main;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RespondCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }
        Player sender = (Player) commandSender;

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /r [Message]");
            return true;
        }
        if (!Main.getMessageMap().containsKey(sender) || Main.getMessageMap().get(sender).isEmpty()) {
            sender.sendMessage("no-respondable");
            return true;
        }
        StringBuilder message = new StringBuilder();
        message.append(" ");

        for (String arg : args) {
            message.append(arg).append(" ");
        }
        if (Main.getMessageMap().get(sender).size() < 2) {
            Player target = Main.getMessageMap().get(sender).get(0);
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
        } else {
            List<Player> list = Main.getMessageMap().get(sender);

            Bukkit.getScheduler().cancelTask(Main.getTaskIDs().get(sender));

            sender.sendMessage("reply-to-whom");
            TextComponent empty = new TextComponent();

            for (Player p : list) {
                String commandConfirm = "/msg " + p.getName() + message;
                TextComponent messageConfirm = new TextComponent(p.getDisplayName());
                messageConfirm.setClickEvent(new ClickEvent(Action.RUN_COMMAND, commandConfirm));
                messageConfirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("message-to-that-guy").create()));

                empty.addExtra(messageConfirm);
            }
            sender.spigot().sendMessage(empty);
        }
        return true;

    }
}
