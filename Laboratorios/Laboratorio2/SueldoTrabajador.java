import javax.swing.JOptionPane;

public class SueldoTrabajador {
    public static void main(String[] args) {
        double sueldoBase = 13000.00;
        int años = 0;
        double aumento = 0;

       while (true) {
         try {
             años = Integer.parseInt(JOptionPane.showInputDialog("Ingrese los años trabajados en la empresa:"));
            if(años<=0 && años<=60){
                break; 
            } else{
                JOptionPane.showMessageDialog(null, "Este valor "+años+ " no es valido");
            }
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "valor invalido,ingrese un entero positivo");
        }
       }

    if (años > 10) {
        aumento = 0.15;
    } else if (años > 5) {
        aumento = 0.10;
    } else if (años > 3) {
        aumento = 0.08;
    } else if (años > 0) {
        aumento = 0.05;
    } else {
        aumento = 0;
    }

    double nuevoSueldo = sueldoBase + (sueldoBase * aumento);
    double liquidacion = (nuevoSueldo / 12) * años;

    JOptionPane.showMessageDialog(null,
        String.format("Sueldo base anual: B/. %.2f\n" +
                    "Porcentaje de aumento: %.2f%%\n" +
                    "Nuevo sueldo anual: B/. %.2f\n" +
                    "Liquidación estimada: B/. %.2f",
                    sueldoBase, aumento * 100, nuevoSueldo, liquidacion));
    }
}
