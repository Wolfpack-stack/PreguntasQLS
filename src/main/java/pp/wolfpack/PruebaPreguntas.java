package pp.wolfpack;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PruebaPreguntas extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        this.getCommand("añadirpregunta").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("añadirpregunta")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Verificar si tiene permiso
                if (player.hasPermission("pruebapreguntas.admin")) {
                    if (args.length < 7) {
                        player.sendMessage(ChatColor.RED + "Uso incorrecto: /añadirpregunta <pregunta> <alternativa_1> <alternativa_2> <alternativa_3> <respuesta_correcta> <Curso_de-3ro_a_8vo> <ramo>");
                        return true;
                    }

                    String pregunta = args[0].replace("_", " ");
                    List<String> alternativas = new ArrayList<>();
                    for (int i = 1; i <= 3; i++) {
                        alternativas.add(args[i].replace("_", " ")); // Reemplaza guiones bajos con espacios
                    }

                    String respuestaCorrecta = args[4].replace("_", " ");
                    String curso = args[5];
                    String ramo = args[6];

                    // Guardar la pregunta en el archivo correspondiente
                    String fileName = getFileName(curso, ramo);
                    File questionsFile = new File(getDataFolder(), fileName);
                    FileConfiguration questionsConfig = YamlConfiguration.loadConfiguration(questionsFile);

                    String path = "preguntas." + pregunta;
                    questionsConfig.set(path + ".alternativas", alternativas);
                    questionsConfig.set(path + ".respuesta_correcta", respuestaCorrecta);
                    questionsConfig.set(path + ".curso", curso);
                    questionsConfig.set(path + ".ramo", ramo);

                    try {
                        questionsConfig.save(questionsFile);
                        player.sendMessage("Pregunta añadida correctamente.");
                    } catch (IOException e) {
                        player.sendMessage("No se pudo guardar la pregunta.");
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage("No tienes permiso para usar este comando.");
                }
                return true;
            }
        }
        return false;
    }

    private String getFileName(String curso, String ramo) {
        if (ramo.equalsIgnoreCase("matematicas")) {
            switch (curso) {
                case "3ro":
                case "4to":
                    return "matematicas_3ro_4to.yml";
                case "5to":
                case "6to":
                    return "matematicas_5to_6to.yml";
                case "7mo":
                case "8vo":
                    return "matematicas_7mo_8vo.yml";
            }
        } else {
            switch (curso) {
                case "3ro":
                case "4to":
                case "5to":
                    return "curso_3ro_5to.yml";
                case "6to":
                case "7mo":
                case "8vo":
                    return "curso_6to_8vo.yml";
            }
        }
        return "default.yml";
    }
}

