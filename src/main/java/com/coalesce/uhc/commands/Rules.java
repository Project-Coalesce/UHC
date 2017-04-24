package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.UHC;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Rules{
    private List<String> strings;

    public Rules() {
        strings = new ArrayList<>();
        strings.add("&e---[ Rules ]---");
        try {
            strings.addAll(new Gson().fromJson(new FileReader(UHC.getInstance().getDataFolder().getAbsolutePath() + File.separatorChar
                    + "rules.json"), strings.getClass()));
        } catch (FileNotFoundException e) {
            UHC.getInstance().getLogger().log(Level.WARNING, "Add a rules.json file to the UHC directory for the /rules command.");
        }
    }

    public void rules(CommandContext context) {
        strings.forEach(cur -> context.send(colour(UHC.getInstance().getFormatter().centerString(cur))));
    }
}
