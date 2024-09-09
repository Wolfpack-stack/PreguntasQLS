package pp.wolfpack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoAñadirPregunta implements CommandExecutor {

    private final PreguntaManager preguntaManager;

    public ComandoAñadirPregunta(PreguntaManager preguntaManager) {

        this.preguntaManager = preguntaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Asegurarse de que el comando solo lo pueda usar un jugador
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;

        // Verificar si el jugador tiene el permiso necesario
        if (!player.hasPermission("prueba.preguntas.admin")) {
            player.sendMessage("No tienes permiso para usar este comando.");
            return true;
        }

        // Verificar que se han dado los argumentos suficientes
        if (args.length < 5) {
            player.sendMessage("Uso incorrecto. Debes ingresar: /añadirpregunta <pregunta> <alternativa1> <alternativa2> <alternativa3> <respuestaCorrecta>");
            return true;
        }

        // Extraer los argumentos
        String pregunta = args[0];
        String alternativa1 = args[1];
        String alternativa2 = args[2];
        String alternativa3 = args[3];
        String respuestaCorrecta = args[4];

        // Añadir la pregunta usando el PreguntaManager
        preguntaManager.añadirPregunta(pregunta, alternativa1, alternativa2, alternativa3, respuestaCorrecta);
        player.sendMessage("Pregunta añadida exitosamente.");

        return true;
    }
}
