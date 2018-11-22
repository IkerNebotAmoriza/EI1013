package practica7;

import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BinaryExpressionTreeTest {

    private List<Character> generarDatosArbol(int nivel, char semilla) {
        List<Character> resultado = new ArrayList<Character>();

        int total = (1 << nivel) -1;
        for (int i = 0; i < total; i++)
            resultado.add((char)(semilla + i));

            return resultado;
    }


    private <T> EDBinaryTree<T> clonaArbol(EDBinaryTree<T> arbol) {
        if (arbol.isEmpty())
            return new EDBinaryTree<>();

        if (arbol.isLeaf())
            return new EDBinaryTree<>(arbol.getData());

        return new EDBinaryTree<>(arbol.getData(), clonaArbol(arbol.getLeftSubTree()),
                clonaArbol(arbol.getRightSubTree()));
    }

    private <T> List<EDBinaryTree<T>> generararArbolesExtendidos(EDBinaryTree<T> semilla) {
        List<EDBinaryTree<T>> lista = new LinkedList<>();
        if (semilla.isEmpty())
            return lista;

        if (semilla.isLeaf()) {
            lista.add(clonaArbol(semilla));
            return lista;
        }

        lista.add(new EDBinaryTree<>(semilla.getData()));

        List<EDBinaryTree<T>> lizq = generararArbolesExtendidos(semilla.getLeftSubTree());
        List<EDBinaryTree<T>> lder = generararArbolesExtendidos(semilla.getRightSubTree());

        for (EDBinaryTree<T> izq: lizq)
            for (EDBinaryTree<T> der: lder)
                lista.add(new EDBinaryTree<>(semilla.getData(), izq, der));

        //lista.addAll(lizq);
        //lista.addAll(lder);
        return lista;
    }

    private <T> List<EDBinaryTree<T>> generararArbolesNoExtendidos(EDBinaryTree<T> semilla) {
        List<EDBinaryTree<T>> lista = new LinkedList<>();
        if (semilla.isEmpty() || semilla.isLeaf())
            return lista;


        lista.add(new EDBinaryTree<>(semilla.getData(), semilla.getLeftSubTree(), null));
        lista.add(new EDBinaryTree<>(semilla.getData(), null, semilla.getRightSubTree()));

        List<EDBinaryTree<T>> lizq = generararArbolesNoExtendidos(semilla.getLeftSubTree());
        List<EDBinaryTree<T>> lder = generararArbolesNoExtendidos(semilla.getRightSubTree());

        for (EDBinaryTree<T> izq: lizq)
            lista.add(new EDBinaryTree<>(semilla.getData(), izq, null));

        for (EDBinaryTree<T> der: lder)
            lista.add(new EDBinaryTree<>(semilla.getData(), null, der));

        for (EDBinaryTree<T> izq: lizq)
            for (EDBinaryTree<T> der: lder)
                lista.add(new EDBinaryTree<>(semilla.getData(), izq, der));


        List<EDBinaryTree<T>> lizqcomp = generararArbolesExtendidos(semilla.getLeftSubTree());
        List<EDBinaryTree<T>> ldercomp = generararArbolesExtendidos(semilla.getRightSubTree());

        for (EDBinaryTree<T> izq: lizq)
            for (EDBinaryTree<T> der: ldercomp)
                lista.add(new EDBinaryTree<>(semilla.getData(), izq, der));

        for (EDBinaryTree<T> izq: lizqcomp)
            for (EDBinaryTree<T> der: lder)
                lista.add(new EDBinaryTree<>(semilla.getData(), izq, der));

        return lista;
    }

    private <T> void crearArbolesExtendidos(EDBinaryTree<T> semilla,
                                                 List<EDBinaryTree<T>> extendidos,
                                                 List<EDBinaryTree<T>> noExtendidos) {
        // Se asume que semilla es un árbol extendido
        if (semilla.isEmpty())
            return;

        if (semilla.isLeaf()) {
            extendidos.add(clonaArbol(semilla));
            return;
        }

        extendidos.add(clonaArbol(semilla));
        extendidos.add(new EDBinaryTree<>(semilla.getData()));

        boolean resultado = true;

        noExtendidos.add(new EDBinaryTree<>(semilla.getData(),
                clonaArbol(semilla.getLeftSubTree()),
                new EDBinaryTree<>()));

        crearArbolesExtendidos(semilla.getLeftSubTree(), extendidos, noExtendidos);

        noExtendidos.add(new EDBinaryTree<>(semilla.getData(),
                new EDBinaryTree<>(),
                clonaArbol(semilla.getRightSubTree())));

        crearArbolesExtendidos(semilla.getRightSubTree(), extendidos, noExtendidos);
    }

    @org.junit.Test
    public void isExtendedTest() {
        System.out.println("PRUEBA DEL METODO isExtended");
        List<Character> valores = generarDatosArbol(4, 'a');
        EDBinaryTree<Character> completo = new EDBinaryTree<>(valores);

        List<EDBinaryTree<Character>> extendidos  = generararArbolesExtendidos(completo);
        extendidos.add(0, new EDBinaryTree<>());
        List<EDBinaryTree<Character>> noExtendidos = generararArbolesNoExtendidos(completo);


        int cuenta = 1;

        for (EDBinaryTree<Character> arbol: extendidos) {
            System.out.println("PRUEBA " + cuenta);
            System.out.print(arbol);
            System.out.println("RESULTADO ESPERADO");
            System.out.println("  true");
            System.out.println("RESULTADO OBTENIDO");
            boolean resultado = arbol.isExtended();
            System.out.println("  " + resultado);
            assertTrue(resultado);
            System.out.println("");
            cuenta++;
        }

        for (EDBinaryTree<Character> arbol: noExtendidos) {
            System.out.println("PRUEBA " + cuenta);
            System.out.print(arbol);
            System.out.println("RESULTADO ESPERADO");
            System.out.println("  false");
            System.out.println("RESULTADO OBTENIDO");
            boolean resultado = arbol.isExtended();
            System.out.println("  " + resultado);
            assertFalse(resultado);
            System.out.println("");
            cuenta++;
        }
    }


    static private <T> List<List<T>> permutaciones(List<T> vec) {
        // genera todas las permutaciones posibles de la lista vec
        List<List<T>> resultado = new ArrayList<>();

        if (vec.size() > 0) {
            List<T> aux = new ArrayList<>();
            aux.add(vec.get(0));
            resultado.add(aux);

            for (int i = 1; i < vec.size(); i++) {
                while (resultado.get(0).size() == i) {
                    for (int k = 0; k <= resultado.get(0).size(); k++) {
                        aux = new ArrayList<>(resultado.get(0));
                        aux.add(k, vec.get(i));
                        resultado.add(aux);
                    }
                    resultado.remove(0);
                }
            }
        } else
            resultado.add(new LinkedList<>());
        return resultado;
    }

    static Character vdigitsOnLeavesTest[][] = {{'+', '-', '/', '^', '3', '8'}, {'*', '+', '4', '5', '7', '2'}};

    static List<List<Character>> generarExpresionesDigitsOnLeaves(){
        List<List<Character>> casos1 = permutaciones(Arrays.asList(vdigitsOnLeavesTest[0]));
        List<List<Character>> casos2 = permutaciones(Arrays.asList(vdigitsOnLeavesTest[1]));

        List<List<Character>> casos = new ArrayList<>();
        for (int i = 0; i < casos1.size(); i++) {
            List<Character> caso = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                caso.add(casos1.get(i).get(j));
                caso.add(casos2.get(i).get(j));
            }
            caso.add(casos2.get(i).get(3));

            casos.add(caso);
        }

        return casos;
    }

    static boolean testVectorExpresiones(List<Character> vect) {
        // comprueba que los últimos cuatro caracteres sean dígitos
        for (int i = 0; i < 3; i++)
            if (Character.isDigit(vect.get(i)))
                return false;
        for(int i = 0; i < 4; i++)
            if (!Character.isDigit(vect.get(vect.size() - i -1)))
                return false;
        return true;
    }

    @org.junit.Test
    public void digitsOnleavesTest(){
        List<List<Character>> casos = generarExpresionesDigitsOnLeaves();

        int cuenta = 1;
        for (List<Character> caso: casos) {
            System.out.println("PRUEBA " + cuenta);
            BinaryTree<Character> arbol = new EDBinaryTree<>(caso);
            System.out.println(arbol);
            System.out.println("RESULTADO ESPERADO");
            boolean esperado = testVectorExpresiones(caso);
            System.out.println("  " + esperado);
            System.out.println("RESULTADO OBTENIDO");
            boolean obtenido = BinaryExpressionTree.digistsOnLeaves(arbol);
            System.out.println("  " + obtenido);
            assertEquals(esperado, obtenido);
            cuenta++;
            System.out.println();
        }
    }

    static private void leerArboles(String fichero, List<String> expresiones, List<BinaryTree<Character>> arboles,
                                    List<Float> evaluaciones) throws FileNotFoundException {
        arboles.clear();
        evaluaciones.clear();
        expresiones.clear();

        Scanner input = null;
        try {
             input = new Scanner(new FileInputStream(fichero));

             while(input.hasNextLine()) {
                 String expresion = input.nextLine();
                 String valor = input.nextLine();

                 expresiones.add(expresion);
                 String postExpre = BinaryExpressionTree.toPostfix(expresion);
                 arboles.add(BinaryExpressionTree.buildBinaryExpressionTree(postExpre));
                 evaluaciones.add(Float.valueOf(valor));

             }

        } catch (IOException e) {
            System.err.println("No se pudo leer el fichero " + fichero);
            throw e;
        }
    }

    static String ficheroExpresiones = "expresiones.txt";

    @org.junit.Test
    public void evaluateTest() throws FileNotFoundException {
        List<BinaryTree<Character>> arboles = new ArrayList<>();
        List<Float> evaluaciones = new ArrayList<>();
        List<String> expresiones = new ArrayList<>();

        leerArboles(ficheroExpresiones, expresiones, arboles, evaluaciones);

        int cuenta = 1;
        for(BinaryTree arbol: arboles) {
            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("ENTRADA");
            System.out.println(arbol);
            System.out.println("RESULTADO ESPERADO");
            float esperado = evaluaciones.get(cuenta-1);
            System.out.println("  evaluate -> " + esperado);
            float obtenido = BinaryExpressionTree.evaluate(arbol);
            System.out.println("RESULTADO OBTENIDO");
            System.out.println("  evaluate -> " + obtenido);
            assertEquals(esperado, obtenido, 0);
            cuenta++;
        }
    }

    private List<Character> StringToList (String texto) {
        List<Character> resultado = new ArrayList<>();
        for (int i = 0; i< texto.length(); i++)
            resultado.add(texto.charAt(i));

        return resultado;
    }


    @org.junit.Test
    public void asListInOrderTest() throws FileNotFoundException {
        List<BinaryTree<Character>> arboles = new ArrayList<>();
        List<Float> evaluaciones = new ArrayList<>();
        List<String> expresiones = new ArrayList<>();

        leerArboles(ficheroExpresiones, expresiones, arboles, evaluaciones);

        int cuenta = 1;
        for(BinaryTree arbol: arboles) {
            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("ENTRADA");
            System.out.println(arbol);
            System.out.println("RESULTADO ESPERADO");
            String expresion = expresiones.get(cuenta-1);
            List<Character> esperado = StringToList(expresion);
            System.out.println("  asListInOrder -> " + esperado);
            List<Character> obtenido = BinaryExpressionTree.asListInorder(arbol);
            System.out.println("RESULTADO OBTENIDO");
            System.out.println("  asListInOrder -> " + obtenido);
            assertEquals(esperado, obtenido);
            cuenta++;
        }
    }
}

