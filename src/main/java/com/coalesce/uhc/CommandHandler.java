package com.coalesce.uhc;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CommandHandler {
    public CommandHandler(CoPlugin plugin) {
        List<CoCommand> commands = new ArrayList<>();

        Message message = new Message();
        commands.add(new CommandBuilder(plugin, "Private Message").description("Private message someone").usage("/pm <person> <message>").aliases("pm", "m", "w", "whisper",
                "msg", "tell").executor(message::message).build());
        commands.add(new CommandBuilder(plugin, "Reply").description("Replies to the last messaged usr").usage("/r <message>").aliases("r", "reply").executor(message::reply).build());

        commands.add(new CommandBuilder(plugin, "Game Start").description("Starts the game").usage("/start").aliases("start", "begin").executor(new GameStart()::gameStart).build());
        commands.add(new CommandBuilder(plugin, "Assign Participation").description("Sets the participation of a player").usage("/setrank <person> <participation>")
                .aliases("assign", "participation", "rank", "setrank").executor(new Assign()::assign).build());
        commands.add(new CommandBuilder(plugin, "Spectate").description("Sets participation to spectator").usage("/spec").aliases("spec").executor(new Spectate()::spectate).build());
        commands.add(new CommandBuilder(plugin, "Rules").description("Shows the rules specified in rules.json.").usage("/rules").aliases("rules").executor(new Rules()::rules).
                build());

        commands.forEach(cur -> {
            try{
                plugin.addCommand(cur);
                plugin.getLogger().log(Level.INFO, "Registered " + cur.getName());
            }catch(Exception e){
                plugin.getLogger().log(Level.INFO, "Failed to register " + cur.getName());
                e.printStackTrace();
            }
        });
    }

}
