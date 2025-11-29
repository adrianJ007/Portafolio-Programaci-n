import javax.swing.JOptionPane;

public class PersonaEspecial{

    public static void main(String[] args) {
        String nombres = JOptionPane.showInputDialog("¿A quién/es más quieres en este momento? Escribe los nombres separados por comas.");

        String[] listaNombres = nombres.split(",");

        StringBuilder resultado = new StringBuilder();

        for (String nombre : listaNombres) {
            nombre = nombre.trim(); 
            int suma = 0;

            for (int i = 0; i < nombre.length(); i++) {
                suma += (int) nombre.charAt(i);
            }

            resultado.append("Nombre: ").append(nombre)
                     .append(" - Suma de letras: ").append(suma)
                     .append("\n");
        }
        JOptionPane.showMessageDialog(null, resultado.toString());
    }
}
