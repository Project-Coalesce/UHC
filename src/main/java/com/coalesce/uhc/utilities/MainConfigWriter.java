package com.coalesce.uhc.utilities;

import com.coalesce.uhc.configuration.MainConfiguration;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;

public class MainConfigWriter {
    private MainConfigWriter() {
    }

    public static void writeMainConfig(File dataFolder, MainConfiguration mainConfiguration) {
        try {
            File file = new File(dataFolder.getAbsolutePath() + File.separatorChar + "config.json");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            file.createNewFile();

            String json = new Gson().toJson(mainConfiguration);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
