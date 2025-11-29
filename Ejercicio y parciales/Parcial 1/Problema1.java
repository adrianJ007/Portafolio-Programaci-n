import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class Problema1 {

    private static final Map<String, Integer> MAPA_MESES = new HashMap<>();
    static {
        MAPA_MESES.put("enero", 1);
        MAPA_MESES.put("febrero", 2);
        MAPA_MESES.put("marzo", 3);
        MAPA_MESES.put("abril", 4);
        MAPA_MESES.put("mayo", 5);
        MAPA_MESES.put("junio", 6);
        MAPA_MESES.put("julio", 7);
        MAPA_MESES.put("agosto", 8);
        MAPA_MESES.put("septiembre", 9);
        MAPA_MESES.put("octubre", 10);
        MAPA_MESES.put("noviembre", 11);
        MAPA_MESES.put("diciembre", 12);
    }
    public static void main(String[] args) {
        List<String> fechasFormateadas = new ArrayList<>();
        DateTimeFormatter formatoFinal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
         final int NUM_FECHAS = 2;
        for (int i = 1; i <= NUM_FECHAS; i++) {
            LocalDate fecha = obtenerFechaValida("" + i);
            if (fecha== null) {
                JOptionPane.showMessageDialog(null, "Operación de entrada cancelada.", "Fin del Programa", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        fechasFormateadas.add(fecha.format(formatoFinal));
        }
        StringBuilder resultado = new StringBuilder("Las " + NUM_FECHAS + " fechas introducidas son:\n");
        for (int i = 0; i < fechasFormateadas.size(); i++) {
            resultado.append("Fecha ").append(i + 1).append(": ").append(fechasFormateadas.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(
            null,
            resultado.toString(),
            "Fechas Finales",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
     private static LocalDate obtenerFechaValida(String titulo) {
        String diaStr, mesNombre, anioStr;
        int dia, mesNum, anio;
        while (true) { 
            diaStr = JOptionPane.showInputDialog(null, "Introduce el Dia" + titulo + ":", titulo + " - Día", JOptionPane.QUESTION_MESSAGE);
            if (diaStr == null) return null; 

            mesNombre = JOptionPane.showInputDialog(null, "Introduce el Mes (en letras) " + titulo + ":", titulo + " - Mes", JOptionPane.QUESTION_MESSAGE);
            if (mesNombre == null) return null;
            mesNombre = mesNombre.toLowerCase().trim();

            anioStr = JOptionPane.showInputDialog(null, "Introduce el Año " + titulo + ":", titulo + " - Año", JOptionPane.QUESTION_MESSAGE);
            if (anioStr == null) return null; 
            try {
                dia = Integer.parseInt(diaStr);
                anio = Integer.parseInt(anioStr);
                
                if (MAPA_MESES.containsKey(mesNombre)) {
                    mesNum = MAPA_MESES.get(mesNombre);
                } else {
                    throw new IllegalArgumentException("El nombre del mes ('" + mesNombre + "') no es válido.");
                }
                return LocalDate.of(anio, mesNum, dia); 
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: El día o el año deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (java.time.DateTimeException | IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Error: La fecha " + diaStr + " de " + mesNombre + " de " + anioStr + " no es válida.", "Error de Validación de Fecha", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}