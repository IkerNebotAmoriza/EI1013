package practica6;

import java.io.IOException;
import java.util.Scanner;

public class Extractor {
    public static void main(String [ ] args) throws IOException {
        Scanner input = new Scanner(System.in);
        String binario;

        System.out.print("Escribe el nombre del fichero binario: " );
        binario = input.nextLine().trim();

        Binarizador.infoImagenes info = new Binarizador.infoImagenes(binario);

        System.out.println(info);

        System.out.print("Indica el numero de la  imagen que quieres extraer: ");
        int im = input.nextInt();
        input.nextLine();

        System.out.print("Indica el nombre del fichero en el que la quieres guardar: ");
        String nombre = input.nextLine();

        ImagenPGM imagen = Binarizador.desbinarizar(binario, im);

        imagen.guardarPGM(nombre);
    }
}
