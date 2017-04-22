package com.coalesce.uhc;

import com.coalesce.chat.CoFormatter;
import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.command.CommandContext;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.commands.Assign;
import com.coalesce.uhc.commands.GameStart;
import com.coalesce.uhc.commands.Message;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import com.coalesce.uhc.utilities.Conditionals;
import com.coalesce.uhc.utilities.Enums;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.coalesce.uhc.utilities.Statics.colour;

public class CommandHandler {
    public CommandHandler(CoPlugin plugin) {
        List<CoCommand> commands = new ArrayList<>();

        Message message = new Message();
        commands.add(new CommandBuilder(plugin, "Private Message").aliases("pm", "m", "w", "whisper", "msg", "tell").executor
                (message::message).build());
        commands.add(new CommandBuilder(plugin, "Reply").aliases("r", "reply").executor(message::reply).build());

        commands.add(new CommandBuilder(plugin, "Game Start").aliases("start", "begin").executor(new GameStart()::gameStart).build());
        commands.add(new CommandBuilder(plugin, "Assign Participation").aliases("rank", "setrank", "participate").executor(new Assign()::assign)
                .build());

        commands.forEach(plugin::addCommand);
    }

}
