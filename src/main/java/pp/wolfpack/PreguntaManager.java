package pp.wolfpack;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PreguntaManager {

    private final File preguntasFile;
    private final FileConfiguration preguntasConfig;
    private final File dataFolder;

    public PreguntaManager(File dataFolder) {
        preguntasFile = new File(dataFolder, "preguntas.yml");
        preguntasConfig = YamlConfiguration.loadConfiguration(preguntasFile);
        this.dataFolder = dataFolder;
    }

    public Map<String, Map<String, List<String>>> cargarPreguntas() {
        File questionsFile = new File(dataFolder, "questions.yml");
        if (!questionsFile.exists()) {
            return new HashMap<>(); // Si no existe el archivo, devolver un mapa vacío
        }

        FileConfiguration questionsConfig = YamlConfiguration.loadConfiguration(questionsFile);
        Map<String, Map<String, List<String>>> preguntas = new HashMap<>();

        // Cargar preguntas del archivo
        for (String pregunta : questionsConfig.getConfigurationSection("preguntas").getKeys(false)) {
            List<String> alternativas = questionsConfig.getStringList("preguntas." + pregunta + ".alternativas");
            String respuestaCorrecta = questionsConfig.getString("preguntas." + pregunta + ".respuesta_correcta");

            Map<String, List<String>> detallesPregunta = new HashMap<>();
            detallesPregunta.put("alternativas", alternativas);
            detallesPregunta.put("respuesta_correcta", Collections.singletonList(respuestaCorrecta));

            preguntas.put(pregunta, detallesPregunta);
        }

        return preguntas;
    }

    public void añadirPregunta(String pregunta, String alternativa1, String alternativa2, String alternativa3, String respuestaCorrecta) {
        String path = "preguntas." + pregunta;
        preguntasConfig.set(path + ".alternativa1", alternativa1);
        preguntasConfig.set(path + ".alternativa2", alternativa2);
        preguntasConfig.set(path + ".alternativa3", alternativa3);
        preguntasConfig.set(path + ".respuestaCorrecta", respuestaCorrecta);
        guardarConfig();
    }

    private void guardarConfig() {
        try {
            preguntasConfig.save(preguntasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

