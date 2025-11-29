import javax.swing.JOptionPane;

public class ConversorTemperatura {

    public static double convertirTemperatura(double valor, String unidad) {
        if (unidad.equalsIgnoreCase("C")) {
            return (9.0 / 5.0) * valor + 32.0;
        } else if (unidad.equalsIgnoreCase("F")) {
            return (valor - 32.0) * (5.0 / 9.0);
        } else {
            return Double.NaN; 
        }
    }
    public static void main(String[] args) {
        String inputVeces = JOptionPane.showInputDialog(null, 
            "¿Cuántas veces desea realizar la conversión?");
        int numVeces = 0;
        try {
            numVeces = Integer.parseInt(inputVeces);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Entrada inválida. Se ejecutará una vez por defecto.");
            numVeces = 1;
        }
        for (int i = 0; i < numVeces; i++) {
            String input = JOptionPane.showInputDialog(null, 
                "Ingrese el valor y la unidad (ej. 25 C o 77 F):");
            if (input == null || input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación cancelada.");
                break; 
            }
            String[] partes = input.split(" ");
            if (partes.length < 2) {
                JOptionPane.showMessageDialog(null, "Formato inválido. Use 'valor unidad'.");
                continue; 
            }
            try {
                double valor = Double.parseDouble(partes[0]);
                String unidad = partes[1].toUpperCase();
                String unidadDestino = "";
                String mensaje = "";
                double resultado = convertirTemperatura(valor, unidad);
                if (unidad.equals("C")) {
                    unidadDestino = "F";
                    mensaje = String.format("%.2f °C es igual a %.2f °F.", valor, resultado);
                } else if (unidad.equals("F")) {
                    unidadDestino = "C";
                    mensaje = String.format("%.2f °F es igual a %.2f °C.", valor, resultado);
                } else {
                    mensaje = "Unidad no reconocida. Por favor, use 'C' o 'F'.";
                }
                JOptionPane.showMessageDialog(null, mensaje);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor de temperatura inválido.");
            }
        }
    }
}