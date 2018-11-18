package practica6;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Integer.min;

public class ImagenPGM {
    int maxNivelGris = 0;
    int altura = 0;
    int anchura = 0;
    int pixel[][] = null; // Matriz de [altura][anchura]

    public ImagenPGM() {}

    public ImagenPGM(String nombre) throws IOException {
        Scanner input = null;
        String id;
        int data;

        // Abrimos el fichero pasado como argumento
        try{
            input = new Scanner(new FileInputStream(nombre));
        }catch (IOException ex){
            throw new IOException();
        }
        // Guardamos el id, filas, columnas y el maximo nivel de gris
        id = input.next();
        anchura = input.nextInt();
        altura = input.nextInt();
        maxNivelGris = input.nextInt();

        // Inicializamos la matriz de pixeles
        pixel = new int[altura][anchura];
        // Recorremos la matriz introduciendo los datos
        for (int row = 0; row < altura; row++) {
            for (int col = 0; col < anchura; col++) {
                data = input.nextInt();
                pixel[row][col] = data;
            }
        }
    }

    public void guardarPGM(String nombre) throws FileNotFoundException {
        PrintWriter output;

        try {
            output = new PrintWriter(new FileOutputStream(nombre));
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear/abrir el fichero " + nombre);
            throw e;
        }

        output.println("P2");
        output.println(anchura + " " + altura);
        output.println(maxNivelGris);

        int cont = 0;
        for(int f = 0; f < altura; f++)
            for (int c = 0; c < anchura; c++ ) {
                output.print(pixel[f][c]);
                cont = (cont + 1) % 16;
                output.print(cont == 0 ? '\n' : ' ');
            }

        output.close();
    }

    public void cuadrar () {
        // Identificamos la dimension mas pequeña
        int size = min(anchura, altura);
        // Actualizamos las dimensiones
        anchura = altura = size;
        // Creamos una matriz auxiliar cuadrada
        int aux[][] = new int[size][size];
        int data;

        // Recorremos la matriz introduciendo los datos
        for (int row = 0; row < size; row++){
            for(int col = 0; col < size; col ++){
                data = pixel[row][col];
                aux[row][col] = data;
            }
        }
        // Sustituimos la matriz pixel por la auxiliar
        pixel = aux;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImagenPGM{");
        sb.append(", maxNivelGris=").append(maxNivelGris);
        sb.append(", filas=").append(altura);
        sb.append(", columnas=").append(anchura);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String [ ] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.print("Escribe el nombre del fichero que queries cuadrar: ");
        String nombre = input.next();

        ImagenPGM imagen = new ImagenPGM(nombre);

        System.out.println("Imagen leida correctamente");
        System.out.println(imagen);

        imagen.cuadrar();

        System.out.println("Se ha cuadrado la imagen");
        System.out.println(imagen);

        nombre = "cuadrado_" +  nombre;

        imagen.guardarPGM(nombre);
        System.out.println("La imagen se ha guardado en el fichero " + nombre);

    }

}
