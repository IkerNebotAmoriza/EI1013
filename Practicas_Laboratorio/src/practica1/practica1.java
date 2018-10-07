package practica1;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    static public Set <Integer> filtra(Iterator<Integer> iter) {
        //HACER CON UN SET AUXILIAR Y DOS ITERADORES
        Set<Integer> aux = new HashSet<Integer>();
        int n1,n2;

        while (iter.hasNext()){
            boolean passed = true;
            n1 = iter.next();
            while (n1==0 && iter.hasNext()){
                n1 = iter.next();
            }

            if (n1 == 0 && !iter.hasNext()){
                passed=false;
            }

            Iterator<Integer> iter2 = iter;
            while (iter2.hasNext()){
                n2 = iter2.next();
                while (n2==0 && iter.hasNext()){
                    n2 = iter2.next();
                }
                if(n1%n2==0){
                    passed=false;
                    break;
                }
            }
            if(passed){
                aux.add(n1);
            }
        }
        return aux;
    }

    /**
     * Toma una colección de conjuntos de <i>String</i> y devuelve como resultado un conjunto con aquellos <i>String </i>
     * Que aparecen en al menos dos conjuntos de la colección.
     * @param col Coleccion de conjuntos de <i>String</i>
     * @return Conjunto de <i>String</i> repetidos. 
     */
    static public Set<String> repetidos(Collection<Set<String>> col) {
        // TODO
        return null;
    }
}
