package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.UHC;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Rules{
    private List<String> strings;

    public Rules() {
        strings = new ArrayList<>();
        strings.add("&e---[ Rules ]---");
        try (FileReader reader = new FileReader(new File(UHC.getInstance().getDataFolder(), "rules.json"))){
            strings.addAll(UHC.getInstance().getGson().fromJson(reader, strings.getClass()));
        } catch (IOException e) {
            UHC.getInstance().getLogger().log(Level.WARNING, "Add a rules.json file to the UHC directory for the /rules command.");
        }
    }

    public void rules(CommandContext context) {
        strings.forEach(cur -> context.send(colour(UHC.getInstance().getFormatter().centerString(cur))));
    }
}
