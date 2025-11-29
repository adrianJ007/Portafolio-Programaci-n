import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

public class Problema2 {

    private static final DateTimeFormatter FORMATO_ENTRADA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static void main(String[] args) {
        
        LocalDate fechaNacimiento = solicitarFecha("Introduce fecha de nacimiento (ej:10/07/2025):");
        if (fechaNacimiento == null) return;

        LocalDate fechaActual = solicitarFecha("Introduce fecha actual:");
        if (fechaActual == null) return;
        

        if (fechaNacimiento.isAfter(fechaActual)) {
            JOptionPane.showMessageDialog(
                null, 
                "Error: La fecha de nacimiento no puede ser posterior a la fecha actual.", 
                "Error Lógico", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        Period periodo = Period.between(fechaNacimiento, fechaActual);
        String resultado;
        if (periodo.getYears() < 1) {
            int totalMeses = periodo.getMonths();
            int totalDias = periodo.getDays();
        
            resultado = String.format(
                "El individuo es un bebe (menor de 1 año).\nEdad: %d meses y %d días.", 
                totalMeses, 
                totalDias
            );
        } else {
            int edadEnAnios = periodo.getYears();
            resultado = String.format("Edad: %d años.", edadEnAnios);
        }
        JOptionPane.showMessageDialog(
            null,
            "Fecha de Nacimiento: " + fechaNacimiento.format(FORMATO_ENTRADA) +
            "\nFecha Actual: " + fechaActual.format(FORMATO_ENTRADA) +
            "\n\n" + resultado,
            "Cálculo de Edad",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    private static LocalDate solicitarFecha(String mensaje) {
        String fechaStr;
        LocalDate fecha = null;

        while (fecha == null) {
            fechaStr = JOptionPane.showInputDialog(null, mensaje, "Entrada de Fecha", JOptionPane.QUESTION_MESSAGE);
            if (fechaStr == null) return null; 
            try {
                fecha = LocalDate.parse(fechaStr, FORMATO_ENTRADA);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Error de formato. Usa dd/MM/yyyy (ej: 01/01/2025).", 
                    "Error de Formato", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return fecha;
    }
}