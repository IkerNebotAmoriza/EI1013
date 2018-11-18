package practica6;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BinarizadorTest {

    String pgms[] = {"test1.pgm", "test2.pgm", "test3.pgm"};
    String binario = "test.dat";
    String esperado = "referencia.dat";

    private static void  testCompararFicheros(String fichero, String referencia) throws IOException {
        RandomAccessFile fich, ref;

        System.out.println("Comparando el fichero " + fichero + "con la referencia " + referencia);

        Binarizador.infoImagenes info = new Binarizador.infoImagenes(fichero);
        System.out.println("DATOS DEL FICHERO GENERADO");
        System.out.println(info);

        info = new Binarizador.infoImagenes(referencia);
        System.out.println("DATOS DEL FICHERO DE REFERECIA");
        System.out.println(info);

        try {
            fich = new RandomAccessFile(fichero, "r");
            ref = new RandomAccessFile(referencia, "r");
        } catch(FileNotFoundException e) {
            System.err.println("  No se pudieron abrir los ficheros");
            assertTrue(false);
            return;
        }

        System.out.println("Comparando longitudes");
        if (ref.length() != fich.length()) {
            System.out.println(" Los ficheros no tienen la misma longitud");
            System.out.println("  Longitud de la referencia (" + referencia + "): " + ref.length());
            System.out.println("  Longitud del binario generado (" + fichero + "): " + fich.length());
        }
        assertEquals(ref.length(), fich.length());

        System.out.println("Comparando byte a byte");
        for(int i= 0; i < ref.length(); i++) {
            byte f = fich.readByte();
            byte r = ref.readByte();

            if (f != r)
                System.out.println(fichero + " es diferente de " + referencia + " en el byte " + i + " [" + r +"] " +
                        "<>[" + f + "]");
            assertEquals(r, f);
        }

        ref.close();
        fich.close();
    }

    @org.junit.Test
    public void binarizadorTest() throws IOException {
        List<String> cuadros = new ArrayList<>();

        for (String pgm: pgms) {
            System.out.println("Leyendo el fichero " + pgm);
            ImagenPGM imagen;
            imagen = new ImagenPGM(pgm);
            System.out.println("  " + imagen);

            String cuadro = "cuadro."+pgm;
            System.out.println("Cuadrando y guardando en " + cuadro);
            imagen.cuadrar();
            imagen.guardarPGM(cuadro);
            System.out.println("  " + imagen);
            cuadros.add(cuadro);
        }

        System.out.println("Binarizando los ficheros " + cuadros + " y guardando en " + binario);
        Binarizador.binarizar(binario, cuadros);

        testCompararFicheros(binario, esperado);

    }

}
