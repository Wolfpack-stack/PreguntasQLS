package pp.wolfpack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PuertaListener implements Listener {
    private final PreguntaManager preguntaManager;
    private final JavaPlugin plugin;
    private final Map<Player, String> awaitingResponses;

    public PuertaListener(PreguntaManager preguntaManager, JavaPlugin plugin) {
        this.preguntaManager = preguntaManager;
        this.plugin = plugin;
        this.awaitingResponses = new HashMap<>();
    }

    @EventHandler
    public void onDoorInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block != null && (block.getType() == Material.DARK_OAK_DOOR  || block.getType() == Material.ACACIA_DOOR ||
                block.getType() == Material.BIRCH_DOOR || block.getType() == Material.CRIMSON_DOOR || block.getType() == Material.IRON_DOOR
                || block.getType() == Material.OAK_DOOR || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.SPRUCE_DOOR
                || block.getType() == Material.WARPED_DOOR || block.getType() == Material.MANGROVE_DOOR|| block.getType() == Material.OAK_TRAPDOOR)) {
            // Cancelar el evento para evitar que la puerta se abra inmediatamente
            event.setCancelled(true);

            // Seleccionar una pregunta al azar
            Map<String, Map<String, List<String>>> preguntas = preguntaManager.cargarPreguntas();
            if (preguntas.isEmpty()) {
                player.sendMessage("No hay preguntas configuradas.");
                return;
            }

            Random random = new Random();
            String[] keys = preguntas.keySet().toArray(new String[0]);
            String pregunta = keys[random.nextInt(keys.length)];
            Map<String, List<String>> data = preguntas.get(pregunta);
            List<String> alternativas = data.values().iterator().next();
            String respuestaCorrecta = data.keySet().iterator().next();

            // Enviar la pregunta al jugador
            player.sendMessage("Pregunta: " + pregunta);
            player.sendMessage("Alternativas: " + String.join(", ", alternativas));

            // Guardar el estado de que el jugador está esperando una respuesta
            awaitingResponses.put(player, respuestaCorrecta);
        }
    }

    // Método para validar la respuesta del jugador (se utilizaría junto con el comando /respuesta)
    public boolean validarRespuesta(Player player, String respuesta) {
        if (awaitingResponses.containsKey(player)) {
            String respuestaCorrecta = awaitingResponses.get(player);
            if (respuesta.equalsIgnoreCase(respuestaCorrecta)) {
                awaitingResponses.remove(player);
                return true;
            }
        }
        return false;
    }

}