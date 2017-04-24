package com.coalesce.uhc.commands;

import static com.coalesce.uhc.utilities.Statics.colour;
import com.coalesce.command.CommandContext;
import com.coalesce.uhc.UHC;

import java.util.Arrays;

public class Rules{
    public void rules(CommandContext context) {
        Arrays.asList(
                "&e---[ Rules ]---",
                ""
        ).forEach(cur -> context.send(colour(UHC.getInstance().getFormatter().centerString(cur))));
    }
}
