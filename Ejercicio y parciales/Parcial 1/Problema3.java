import javax.swing.JOptionPane;

public class Problema3 {

    public static void main(String[] args) {
        String anioStr;
        int anio = 1;
        boolean entradaValida = false;

        while (!entradaValida) {
            anioStr = JOptionPane.showInputDialog(
                null, 
                "Introduce un año para saber si es bisiesto :", 
                "Verificador de Año Bisiesto", 
                JOptionPane.QUESTION_MESSAGE
            );
            if (anioStr == null) return;
            try {
    
                anio = Integer.parseInt(anioStr.trim());
                entradaValida = true;
         
                boolean esBisiesto = esAnioBisiesto(anio);
                
                String resultado;
                if (esBisiesto) {
                    resultado = "✅ El año " + anio + " es bisiesto";
                } else {
                    resultado = "❌ El año " + anio + "no es bisiesto.";
                }

                JOptionPane.showMessageDialog(
                    null,
                    resultado,
                    "Resultado Bisiesto",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: escribir correctamente el año", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private static boolean esAnioBisiesto(int anio) {
        if (anio % 400 == 0) {
            return true; 
        }
        if (anio % 100 == 0) {
            return false; 
        }
        if (anio % 4 == 0) {
            return true; 
        }
        return false; 
    }
}