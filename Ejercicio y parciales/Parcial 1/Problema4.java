import javax.swing.JOptionPane;

public class Problema4 {

    public static void main(String[] args) {
        String horasStr, tasaStr;
        double horasTrabajadas = 0;
        double tasaOrdinaria = 0;
        boolean entradaValida = false;
        while (!entradaValida) {
            horasStr = JOptionPane.showInputDialog(null, "Introduce las HORAS semanales trabajadas:", "Entrada de Datos", JOptionPane.QUESTION_MESSAGE);
            if (horasStr == null) return;
            
            tasaStr = JOptionPane.showInputDialog(null, "Introduce la TASA horaria ordinaria (ej: 10.50 Balboas):", "Entrada de Datos", JOptionPane.QUESTION_MESSAGE);
            if (tasaStr == null) return;
            
            try {
    
                horasTrabajadas = Double.parseDouble(horasStr);
                tasaOrdinaria = Double.parseDouble(tasaStr);

                if (horasTrabajadas < 0 || tasaOrdinaria <= 0) {
                    JOptionPane.showMessageDialog(null, "Las horas y la tasa deben ser valores positivos válidos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    continue; 
                }
                entradaValida = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Debes introducir valores numéricos válidos para horas y tasa.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
        double salarioBruto = calcularSalarioBruto(horasTrabajadas, tasaOrdinaria);
        
        double impuesto;
        double tasaImpuesto;
        
        if (salarioBruto <= 750.00) {
            tasaImpuesto = 0.00; 
            impuesto = 0.00;
        } else {
            tasaImpuesto = 0.10; 
            impuesto = salarioBruto * tasaImpuesto;
        }
        
        double salarioNeto = salarioBruto - impuesto;
        
        String mensajeSalida = String.format(
            "Cálculo de Salario Semanal\n\n" +
            "Horas Trabajadas: %.2f\n" +
            "Tasa Ordinaria: %.2f Balboas/hr\n" +
            "-----------------------------------\n" +
            "Salario BRUTO: **%.2f Balboas**\n" +
            "Impuesto (%.0f%%): %.2f Balboas\n" +
            "Salario NETO: **%.2f Balboas**",
            horasTrabajadas,
            tasaOrdinaria,
            salarioBruto,
            tasaImpuesto * 100,
            impuesto,
            salarioNeto
        );
        JOptionPane.showMessageDialog(
            null,
            mensajeSalida,
            "Salario Neto Semanal",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    private static double calcularSalarioBruto(double horas, double tasa) {
        final double HORAS_ORDINARIAS = 40.0;
        double salarioOrdinario;
        double salarioExtra=0 ;
        if (horas <= HORAS_ORDINARIAS) {
            salarioOrdinario = horas * tasa;
        } else {
            salarioOrdinario = HORAS_ORDINARIAS * tasa;
            double horasExtras = horas - HORAS_ORDINARIAS;
            double tasaExtra = tasa * 1.50; // 
            
            salarioExtra = horasExtras * tasaExtra;
        }
        return salarioOrdinario + salarioExtra;
    }
}