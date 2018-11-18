package practica6;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class VisorBinario {

	public static void main(String[] args) throws IOException {
        Scanner consola = new Scanner(System.in);

        System.out.print("Escribe el nombre de fichero a visualizar: ");
        String nombre = consola.nextLine();

        Binarizador.infoImagenes info = new Binarizador.infoImagenes(nombre);

        System.out.println(info);
    }
}
