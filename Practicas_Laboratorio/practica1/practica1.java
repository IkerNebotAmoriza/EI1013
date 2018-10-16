package Practicas_Laboratorio.practica1;

import java.util.*;

public class practica1 {

    /**
     *  Método que toma dos conjuntos de enteros y separa los elementos entre aquellos que sólo aparecen una vez
     *  y aquellos que aparecen repetidos. El método modifica los conjuntos que toma como parámetros.
     * @param unicos    A la entrada un conjunto de enteros. A la sálida los elementos que solo aparecen en uno de
     *                  los conjuntos.
     * @param repetidos A la entrada un conjunto de enteros. A la salida los elementos que aparecen en ambos conjuntos.
     */
    static public void separa(Set<String> unicos, Set<String> repetidos) {
        Iterator<String> iter = repetidos.iterator();   //INICIAMOS EL ITERADOR EN REPETIDOS

        while (iter.hasNext()) {
            String aux = iter.next();   //SALTAMOS SOBRE EL ELEMENTO Y GUARDAMOS SU VALOR

            if(unicos.contains(aux)) {  //SI EL ELEMENTO ITERADO ESTA EN UNICOS, LO ELIMINAMOS DE UNICOS
                unicos.remove(aux);
            }else{
                unicos.add(aux);    //SI EL ELEMENTO ITERADO NO ESTA REPETIDO LO ANADIMOS A UNICOS
                iter.remove();  //Y LO ELIMINAMOS DE REPETIDOS
            }
        }
    }

    /**
     *  Toma un iterador a una colección de enteros positivos y devuelve como resultado un conjunto con aquellos elementos
     *  de la colección que no son múltiplos de algún otro de la colección. Los ceros son descartados
     * @param iter  Iterador a una colección de enteros
     * @return Conjunto de de enteros.
     */
    static public Set<Integer> filtra(Iterator<Integer> iter) {
        Set<Integer> aux = new HashSet<Integer>();
        Set<Integer> sol = new HashSet<Integer>();
        Iterator<Integer> iter1, iter2;
        int n1,n2;
        boolean pass;


        while (iter.hasNext()){ //MIENTRAS EL ITERADOR RECIBIDO CONTENGA ELEMENTOS
            n1 = iter.next();
            if (n1 > 0 && !(aux.contains(n1))){ //AÑADIMOS LOS ELEMENTOS QUE CUMPLEN LA CONDICION AL SET AUXILIAR
                aux.add(n1);
            }
        }

        iter1 = aux.iterator();
        while (iter1.hasNext()){    //MIENTRAS NOS QUEDEN ELEMENTOS EN EL ITERADOR
            pass = true;    //SUPONEMOS QUE EL ELEMENTO ACTUAL CUMPLE ES VALIDO
            n1 = iter1.next();
            iter2 = aux.iterator();
            while (iter2.hasNext()){    //Y LO COMPARAMOS CON TODOS LOS ELEMENTOS DEL SET AUXILIAR
                n2 = iter2.next();
                if (n1 != n2 && n1%n2 == 0) {   //SI CUMPLE LA CONDICION SE DESCARTA
                    pass = false;
                }
            }
            if (pass){  //SI ES VALIDO SE AÑADE A LA SOLUCION
                sol.add(n1);
            }
        }
        return sol;
    }

    /**
     * Toma una colección de conjuntos de <i>String</i> y devuelve como resultado un conjunto con aquellos <i>String </i>
     * Que aparecen en al menos dos conjuntos de la colección.
     * @param col Coleccion de conjuntos de <i>String</i>
     * @return Conjunto de <i>String</i> repetidos. 
     */
    static public Set<String> repetidos(Collection<Set<String>> col) {
        Set<String> sol = new HashSet<String>();    //CONJUNTO SOLUCION
        Set<String> vistos = new HashSet<String>(); //CONJUNTO DE VISTOS
        String aux;

        List<String> lista = new ArrayList<String>();
        Iterator<Set<String>> iterSet = col.iterator(); //METEMOS TODOS LOS ELEMENTOS DE LA COLECCION EN UNA LISTA
        while (iterSet.hasNext()){
            lista.addAll(iterSet.next());
        }
        Iterator<String> iterLista = lista.iterator();
        while (iterLista.hasNext()){
            aux = iterLista.next();
            iterLista.remove();
            if ( !(vistos.contains(aux)) && lista.contains(aux) ){  //SI EL VALOR ACTUAL ES NUEVO Y ESTA REPETIDO
                sol.add(aux);   //LO AÑADIMOS A LA SOLUCION
            }
            vistos.add(aux);
        }
        return sol;
    }
}
