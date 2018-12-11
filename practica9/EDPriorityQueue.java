package practica9;

import java.util.*;

//IMPLEMENTS A PRIORITY QUEUE USING A MINHEAP

public class EDPriorityQueue<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    E[] data;
    int size;
    //optional reference to comparator object
    Comparator<E> comparator;

    //Methods
    //Constructor
    public EDPriorityQueue() {
        this.data = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
    }

    public EDPriorityQueue(Comparator<E> comp) {
        this.data = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
        this.comparator = comp;
    }

    public EDPriorityQueue(Collection<E> c, Comparator<E> comp) {
        this.data = (E[]) new Object[c.size()];
        this.size = 0;
        this.comparator = comp;
        for (E elem : c)
            add(elem);
    }

    public EDPriorityQueue(Collection<E> c) {
        this.data = (E[]) new Object[c.size()];
        this.size = 0;
        int i = 0;
        for (E elem : c)
            add(elem);
    }


    //private methods

    /**
     * compares two objects and returns a negative number if object
     * left is less than object right, zero if are equal, and a positive number if
     * object left is greater than object right
     */
    int compare(E left, E right) {
        if (comparator != null) { //A comparator is defined
            return comparator.compare(left, right);
        } else {
            return (((Comparable<E>) left).compareTo(right));
        }
    }

    /**
     * Exchanges the object references in the data field at indexes i and j
     *
     * @param i index of firt object in data
     * @param j index of second objet in data
     */
    private void swap(int i, int j) {
        E elem_i = this.data[i];
        this.data[i] = this.data[j];
        this.data[j] = elem_i;
    }

    private void sink(int parent) {
        int min, lChild, rChild;
        boolean rightPlace = false;
        while (parent < size/2 && !rightPlace) { //Mientras queden hijos que hundir
            min = parent; lChild = parent*2+1; rChild = lChild+1;
            if (compare(data[min], data[lChild]) > 0) { //Si el hijo izquierdo ha de ser hundido
                min = lChild;
            }
            if (rChild < size && compare(data[min], data[rChild]) > 0) {    //Si el hijo derecho ha de ser hundido
                min = rChild;
            }
            if (parent != min) {    // Si no hay que hundir ningun hijo el elemento del monticulo está en la posicion adecuada
                swap(parent, min);
                parent = min;
            }
            else {
                rightPlace = true;
            }
        }
    }

    private void floating(int child) {
        boolean rightPlace = false;
        int parent;
        while (child > 0 && !rightPlace) { // Mientras el elemento del monticulo tenga padre
            parent = (child-1)/2;
            if (compare(data[parent], data[child]) > 0) {   // Si el padre es mayor se intercambian posiciones
                swap(parent, child);
                child = parent;
            }
            else {
                rightPlace = true;
            }
        }
    }

    private void reallocate() {
        if (size == data.length) {
            E[] aux = data;
            data = (E[]) new Object[data.length * 2];
            for (int i = 0; i < size; i++)
                data[i] = aux[i];
        }
    }

    /**
     * Public methods
     */
    public boolean isEmpty() {
        return (this.size == 0);
    }

    public int size() {
        return this.size;
    }

    public Object[] toArray() {
        Object[] array = new Object[this.size];
        for (int i = 0; i < this.size; i++)
            array[i] = this.data[i];
        return array;
    }

    String toStringHeap() {
        StringBuilder s = new StringBuilder();
        int enNivel = 1;
        int finNivel = 1;
        for (int i = 0; i < size; i++) {
            s.append(data[i]);

            if (i != size - 1)
                if (i == finNivel - 1) {
                    s.append("] [");
                    enNivel *= 2;
                    finNivel += enNivel;
                } else
                    s.append(", ");
        }
        s.append("]");
        return s.toString();
    }

    public String toString() {
        return "EDPriorityQueue: [" + this.toStringHeap() + " - size: " + size;
    }


    /**
     * Inserts an item into the priority_queue. Returns true if successful;
     * returns false if the item is not inserted
     *
     * @param item Item to be inserted in the priority queue
     * @return boolean
     */
    public boolean add(E item) {
        //Add the item to the end of the heap

        this.data[size] = item;
        size++;

        reallocate();

        floating(size - 1);

        return true;
    }


    /**
     * Removes the smallest entry and returns it if the priority queue is not empty.
     * Otherwise, returns NoSuchElementException
     *
     * @return E smallest element in the queue
     */
    public E remove() throws NoSuchElementException {
        if (isEmpty()) throw new NoSuchElementException();
        E result = data[0]; //the root of the heap


        //remove the last item in the array and put it in the root position
        swap(0, size - 1);
        size--;
        if (size > 1)
            sink(0);
        return result;

    }


    /**
     * Returns the smallest entry, WITHOUT REMOVING IT.
     * If the queue is empty, returns NoSuchElementException
     *
     * @return E smallest entry
     */
    public E element() throws NoSuchElementException {
        if (isEmpty()) throw new NoSuchElementException();
        return data[0];
    }


    public int indexOf(E item) {
        int i = 0;
        while (i < this.size && compare(data[i], item) != 0) i++;
        if (i == this.size) return -1;
        return i;
    }

    /**
     * Removes a single instance of the element item
     *
     * @param item Element to be removed (a single instance)
     * @return the value of the element removed
     * @throws NoSuchElementException If there no such item in the collection
     */
    public E remove(E item) throws NoSuchElementException {
        int index = indexOf(item);  //Obtenemos el indice del elemento a eliminar
        if (index == -1) {
            throw new NoSuchElementException();
        }
        swap(index, size-1);    //Lo intercambiamos por la ultima posicion
        size--;
        if (size > 1) {
            int parent = (index-1)/2;
            if (compare(data[parent], data[index]) > 0 && index > 0) { // Si el elemento pertenece a una posicion superior
                floating(index);
            }
            else {
                sink(index);    //Si el elemento pertenece a una posicion inferior
            }
        }
        return item;
    }


    /**
     * Converts a minHeap into a maxHeap.
     **/
    public void maxHeapify() { //VERSION NO OPTIMA ( La dejo porque es la que se me ocurrio antes de mirar la solucion
        int n = size;         //en internet)
        E [] aux = (E[]) new Object[data.length];
        for (int i = size-1; i >= 0; i--) { //Vamos extrayendo el elemento minimo del monticulo
            aux[i] = remove();  //E introduciondolo desde la ultima posicion en el vector auxiliar
        }
        size = n;
        data = aux;
    }


    /**
     * @return -1 if the queue contains a MinHeap, +1 if it is a maxHeap, or = if its empty.
     */
    public int typeOfHeap() {
        if (!isEmpty()) {
            int lChild, rChild;
            int parent = 0;
            while (parent < size/2) {
                lChild = parent*2+1;
                rChild = lChild+1;
                // Si en algun momento se cumple el requisito de un maxHeap
                if (compare(data[parent], data[lChild]) > 0 || rChild < size && compare(data[parent], data[rChild]) > 0) {
                    return 1;
                }
                // Si en algun momento se cumple el requisito de un minHeap
                else if (compare(data[parent], data[lChild]) < 0 || rChild < size && compare(data[parent], data[rChild]) < 0) {
                    return -1;
                }
                parent++;
            }
        }
        else {
            return 0; //Si el vector esta vacio
        }
        return -1; //Si todos los elementos del vector son iguales suponemos que es un minHeap
    }
}
