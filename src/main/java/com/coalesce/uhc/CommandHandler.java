package com.coalesce.uhc;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.commands.*;

import java.util.ArrayList;
import java.util.List;

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
        commands.add(new CommandBuilder(plugin, "Spectate").aliases("spec").executor(new Spectate()::spectate).build());
        commands.add(new CommandBuilder(plugin, "Rules").aliases("rules").executor(new Rules()::rules).build());

        commands.forEach(plugin::addCommand);
    }

}
