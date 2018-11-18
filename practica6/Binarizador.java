package practica6;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Binarizador {


    /**
     * Crea un fichero en formato binario a partir de las imagenes PGM indicadas.
     * @param destino   Nombre del fichero binario
     * @param pgms      Lista con los nombres de los ficheros con las imágener PGM.
     * @throws IOException  En el caso de que alguna de los fichero no se pueda, leer escribir o leer.
     */
    static void binarizar( String destino, List<String> pgms) throws IOException {
        RandomAccessFile file = null;
        ArrayList<ImagenPGM> imagenes = new ArrayList<ImagenPGM>();

        // Creamos las imagenes y las introducimos en la lista
        for ( int i = 0; i < pgms.size(); i++) {
            imagenes.add(new ImagenPGM(pgms.get(i)));
        }

        try {
            file = new RandomAccessFile(destino, "rw");
            // Escribimos la cantidad de imagenes
            file.writeInt(imagenes.size());

            // Escribimos donde empieza cada imagen en el archivo binario
            long pointer = file.getFilePointer() + (imagenes.size() * Long.BYTES);
            long imgSize;
            for (int i = 0; i < imagenes.size(); i++) {
                file.writeLong(pointer);
                pointer += (3 * Integer.BYTES + ((imagenes.get(i).anchura * imagenes.get(i).altura) * Integer.BYTES));
            }

            // Escribimos cada ImagenPGM en el fichero binario
            for (int i = 0; i < imagenes.size(); i++) {
                ImagenPGM auxIMG = imagenes.get(i);
                // Escribimos el header de la imagen
                file.writeInt(auxIMG.anchura);
                file.writeInt(auxIMG.altura);
                file.writeInt(auxIMG.maxNivelGris);

                // Escribimos todos los pixeles
                for (int row = 0; row < auxIMG.altura; row++) {
                    for (int col = 0; col < auxIMG.anchura; col++) {
                        file.writeInt(auxIMG.pixel[row][col]);
                    }
                }
            }
        }catch (IOException ex){
            throw new IOException();
        }
    }

    static ImagenPGM desbinarizar(String nombre, int imagenID) throws IOException {
        RandomAccessFile ioFile;

        try {
            ioFile = new RandomAccessFile(nombre, "r");
        }  catch (FileNotFoundException e) {
            System.err.println("ERROR: no se pudo abrir el fichero" + nombre);
            throw e;
        }

        ImagenPGM pgm = new ImagenPGM();

        try {
            int num = ioFile.readInt();

            if (imagenID < 1 || imagenID > num)
                throw new IllegalArgumentException("La imagen solicitada (" +imagenID+ ") no está incluida en el " +
                        "fichero");

            ioFile.seek(Integer.BYTES + (imagenID-1)*Long.BYTES);
            ioFile.seek(ioFile.readLong());


            pgm.anchura = ioFile.readInt();
            pgm.altura = ioFile.readInt();
            pgm.maxNivelGris = ioFile.readInt();

            pgm.pixel = new int[pgm.altura][pgm.anchura];

            for (int f = 0; f < pgm.altura; f++)
                for(int c = 0; c < pgm.anchura; c++)
                    pgm.pixel[f][c] = ioFile.readInt();


        } catch (IOException e) {
            System.err.println("Error leyendo el fichero " + nombre);
            throw e;
        }

        return pgm;
    }

    public static class infoImagenes {
        public int numeroImagenes;
        public long tamaño;
        public long posicionInicio[];
        public int infoImagen[][]; // Una terna por imagen [][0]: Filas, [][1]: Columnas, [][2]: Niveles de gris

        public infoImagenes(String nombre) throws IOException {
            RandomAccessFile ioFile;

            try {
                ioFile = new RandomAccessFile(nombre, "r");
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: no se pudo abrir el fichero " + nombre);
                throw e;
            }

            try {
                tamaño = ioFile.length();
                ioFile.seek(0);
                numeroImagenes = ioFile.readInt();
                posicionInicio = new long[numeroImagenes];
                infoImagen = new int[numeroImagenes][3];

                for (int i = 0; i < numeroImagenes; i++) {
                    posicionInicio[i] = ioFile.readLong();
                    long actual = ioFile.getFilePointer();

                    ioFile.seek(posicionInicio[i]);
                    for (int j = 0; j < 3; j++)
                        infoImagen[i][j] = ioFile.readInt();
                    ioFile.seek(actual);
                }

                ioFile.close();

            } catch (IOException e) {
                System.err.println("Error leyendo el fichero " + nombre);
                throw e;
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("El fichero contiene %d imagenes que ocupan %d bytes\n", numeroImagenes,
                    tamaño));
            for (int i= 0; i < numeroImagenes; i++)
                sb.append(String.format("  - Imagen %d: comienza en %d, dimensiones [%d x %d], niveles de gris: %d\n",
                        i+1, posicionInicio[i], infoImagen[i][0], infoImagen[i][1], infoImagen[i][2]));

            return sb.toString();
        }
    }

    public static void main(String [ ] args) throws IOException {
        Scanner input = new Scanner(System.in);
        List<String> nombres = new ArrayList<>();

        String nombre;
        do {
            System.out.print("Escribe el nombre del fichero que quieres incluir en el binario (linea en " +
                    "blanco para finalizar): ");
            nombre = input.nextLine();
            nombre = nombre.trim();
            if (nombre.length() > 0)
                nombres.add(nombre);
        } while (nombre.length() != 0);

        System.out.print("Escribe el nombre del fichero binario: " );
        String binario = input.nextLine().trim();

        System.out.println("Binarizando " + nombres + " en " + binario);

        Binarizador.binarizar(binario, nombres);

        Binarizador.infoImagenes info = new Binarizador.infoImagenes(binario);

        System.out.println(info);
    }

}