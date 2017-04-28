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
        commands.add(new CommandBuilder(plugin, "Private Message").minArgs(2).maxArgs(Integer.MAX_VALUE).description("Private message someone").usage("/pm <person> <message>").aliases("pm", "m", "w", "whisper",
                "msg", "tell").executor(message::message).build());
        commands.add(new CommandBuilder(plugin, "Reply").minArgs(1).maxArgs(Integer.MAX_VALUE).description("Replies to the last messaged usr").usage("/r <message>").aliases("r", "reply").executor(message::reply).build());

        commands.add(new CommandBuilder(plugin, "Game Start").minArgs(0).maxArgs(0).description("Starts the game").usage("/start").aliases("start", "begin").executor(new GameStart()::gameStart).build());
        commands.add(new CommandBuilder(plugin, "Assign Participation").minArgs(2).maxArgs(2).description("Sets the participation of a player").usage("/setrank <person> <participation>")
                .aliases("assign", "participation", "rank", "setrank").executor(new Assign()::assign).build());
        commands.add(new CommandBuilder(plugin, "Spectate").minArgs(0).maxArgs(0).description("Sets participation to spectator").usage("/spec").aliases("spec").executor(new Spectate()::spectate).build());
        commands.add(new CommandBuilder(plugin, "Rules").minArgs(0).maxArgs(0).description("Shows the rules specified in rules.json.").usage("/rules").aliases("rules").executor(new Rules()::rules).
                build());
        commands.add(new CommandBuilder(plugin, "Pregen").minArgs(0).maxArgs(0).description("Pregenerates the area specified within the worldborder.").usage("/pregen").aliases("pregenerate").executor(new Pregen()::pregen).build());

        commands.forEach(plugin::addCommand);
    }

}
