package practica4;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
* Implementación incompleta de una lista usando una cadena no circular de nodos 
* doblemente enlazados.
*/
public class EDDoubleLinkedList<T> implements List<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) { this.data = data;};
    }

    private Node first = null;
    private Node last = null;
    private int size = 0;

    public EDDoubleLinkedList(Collection<T> col) {
        for (T elem: col) {
            Node n = new Node(elem);
            if (first == null)
                first = last = n;
            else {
                n.prev = last;
                last.next = n;
                last = n;
            }
        }
        size = col.size();
    }

    
     /**
     * Invierte el orden de los elementos de la lista.
     */
    public void reverse() {
        if (first != null && size > 1) {

            Node actual = first;
            Node siguiente = first.next;
            while (actual.next != null) {

                actual.next = actual.prev;  //Invertimos las referencias del nodo actual
                actual.prev = siguiente;

                actual = siguiente;
                siguiente = siguiente.next;
            }
            actual.next = actual.prev;  //Invertimos las referencias para el ultimo nodo
            actual.prev = siguiente;

            first = last;   //Actualizamos las referencias a los nodos en los extremos
            last = actual;
        }
    }

    /**
     *  Añade los elementos de la lista intercalándolo con la lista actual.
     */
    public void shuffle(List<T> lista) {
        if (lista != null && lista.size() != 0){
            if (first == null){ //Si la lista actual esta vacia, la lista se convierte en la actual
                Node aux = new Node(lista.get(0));
                first = aux;
                last = aux;
                size++;

                for(int i = 1; i < lista.size(); i++){  //Anadimos los elementos de la lista al final
                    aux = new Node(lista.get(i));
                    last.next = aux;
                    aux.prev = aux;
                    last = aux;
                    size++;
                }
            }
            if (lista.size() == 1){ //Si la lista actual solo tiene un elemento
                Node aux;
                for(int i = 0; i < lista.size(); i++){  //Anadimos los elementos de la lista al final
                    aux = new Node(lista.get(i));
                    last.next = aux;
                    aux.prev = aux;
                    last = aux;

                    size++;
                }
            }
            Node actual = first;
            Node aux;
            int index = 0;

            while (actual.next != null && index < lista.size()) {   //Recorremos la lista actual
                aux = new Node(lista.get(index));   //Creamos el nodo a intercalar e incrementamos el indice
                index++;

                aux.next = actual.next; //Modificamos las referencias
                aux.prev = actual;
                aux.next.prev = aux;
                actual.next = aux;

                actual = aux.next;  //Pasamos al siguiente nodo
                size++;
            }
            for (int i = index; i < lista.size(); i++) {    //Anadimos los nodos restantes
                aux = new Node(lista.get(i));
                last.next = aux;
                aux.prev = aux;
                last = aux;

                size++;
            }
        }
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        Object[] v = new Object[size];

        Node n = first;
        int i = 0;
        while(n != null) {
            v[i] = n.data;
            n = n.next;
            i++;
        }

        return v;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        first = last = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");

        if (isEmpty())
            sb.append("[]");
        else {
            sb.append("[");
            Node ref = first;
            while (ref != null) {
                sb.append(ref.data);
                ref = ref.next;
                if (ref == null)
                    sb.append("]");
                else
                    sb.append(", ");
            }
        }

        sb.append(": ");
        sb.append(size);

        return sb.toString();
    }
}
