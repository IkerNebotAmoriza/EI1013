package practica2;

import java.util.*;

public class practica2 {

    /** Comprueba si dos listas de String son equivalentes.
     *
     * Dos listas son equivalentes si contienen los mismos elementos y la misma cantidad de ellos.
     * @param l1    Primera lista
     * @param l2    Segunda lista
     * @return <code>true</code> si las listas son equivalentes. <code>false</code> en caso contrario.
     */
    static public boolean equivalentes(List<String> l1, List<String> l2) {
        if (l1.size() != l2.size()) {
           return false;
        }
        Iterator<String> iter = l1.listIterator();
        String aux;
        List<String> listAux = new ArrayList<String>(l2);

        while (iter.hasNext()) {
            aux = iter.next();
            if (listAux.contains(aux)) {
                listAux.remove(aux);
            }else {
                return false;
            }
        }
        return true;
    }

    /** Invierte el orden de los elmentos de una lista.
     *
     * @param iter Un iterador de la lista. Puede estar en cualqueir posición de la lista.
     */
    static public void invierte(ListIterator<String> iter) {
        while (iter.hasNext()) {
            iter.next();
        }

        List<String> auxList = new ArrayList<String>();
        String aux;

        while (iter.hasPrevious()){
            aux = iter.previous();
            auxList.add(aux);
        }

        while (iter.hasNext()) {
            aux = auxList.get(iter.nextIndex());
            iter.next();
            iter.set(aux);
        }
    }

    /** Ordena los elementos de una lista de menor a mayor
     * @param l     La lista
     * @return      Una nueva lista con los mismo elementos, pero ordenados.
     */
    static public List<Integer> ordenar(List<Integer> l) {
        List<Integer> sortedList = new ArrayList<Integer>();
        List<Integer> copy = new ArrayList<Integer>(l);

        if (l.isEmpty()){
            return sortedList;
        }

        ListIterator<Integer> iter;
        Integer min, aux;

        while (!copy.isEmpty()){
            min = copy.get(0);
            iter = copy.listIterator();
            while (iter.hasNext()){
                aux = iter.next();
                if (min >= aux){
                    min = aux;
                }
            }
            sortedList.add(min);
            copy.remove(min);
        }
        return sortedList;
    }
}
