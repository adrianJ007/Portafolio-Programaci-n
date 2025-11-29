import javax.swing.JOptionPane;

public class LectorDeHora {

    public static void main(String[] args) {

        String hora24 = JOptionPane.showInputDialog("Introduce la hora en formato de 24 horas (HH:MM):");

        if (hora24 == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return; 
        }
        if (hora24.length() != 5 || hora24.charAt(2) != ':') {
            JOptionPane.showMessageDialog(null, "Formato de hora inválido. Asegúrate de usar HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int horas = Integer.parseInt(hora24.substring(0, 2));
                int minutos = Integer.parseInt(hora24.substring(3, 5));

                if (horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59) {
                    
                    String sufijo;
                    int horas12;

                    if (horas < 12) {
                        sufijo = "AM";
                        if (horas == 0) {
                            horas12 = 12; 
                        } else {
                            horas12 = horas;
                        }
                    } else {
                        sufijo = "PM";
                        if (horas == 12) {
                            horas12 = 12; 
                        } else {
                            horas12 = horas - 12;
                        }
                    }

                    String minutosFormato = String.format("%02d", minutos);
                    
                    String mensaje = "La hora en formato de 12 horas es: " + horas12 + ":" + minutosFormato + " " + sufijo;
                    
                    JOptionPane.showMessageDialog(null, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(null, "Valores de horas o minutos fuera de rango.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada no válida. Asegúrate de introducir solo números.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}