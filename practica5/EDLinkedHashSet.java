package practica5;

import javax.swing.text.EditorKit;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EDLinkedHashSet<T> implements Set<T> {
    protected class Node {
        T data;
        Node next = null;
        Node prev = null;

        Node(T data) {
            this.data = data;
        }
    }

    static final private int DEFAULT_CAPACITY = 10;
    static final private int DEFAULT_THRESHOLD = 7;

    Node[] table;
    boolean[] used;
    int size = 0;
    int dirty = 0;
    int rehashThreshold;
    Node first = null;
    Node last = null;

    public EDLinkedHashSet() {
        table = new EDLinkedHashSet.Node[DEFAULT_CAPACITY];
        used = new boolean[DEFAULT_CAPACITY];
        rehashThreshold = DEFAULT_THRESHOLD;
    }

    public EDLinkedHashSet(Collection<T> col) {
        table = new EDLinkedHashSet.Node[DEFAULT_CAPACITY];
        used = new boolean[DEFAULT_CAPACITY];
        rehashThreshold = DEFAULT_THRESHOLD;

        addAll(col);
    }

    /**
     * Calcula un código de dispersión mayor que cero y ajustado al tamaño de <code>table</code>.
     * @param item  Un valor cualquiera, distinto de <code>null</code>.
     * @return Código de disersión ajustado al tamaño de la tabla.
     */
    int hash(T item) {
        return (item.hashCode() & Integer.MAX_VALUE) % table.length;
    }

    /**
     * Realiza la ampliación de las tabla y la redispersión de los elementos si se cumple la condición de que
     * <code>dirty > rehashThreshold</code>. La tabla  dobla su tamaño. El vector <code>used</code> y
     * <code>rehashThreshold</code> se modifican de forma análoga.
     */
    private void rehash() {
        if (dirty >= rehashThreshold) {
            // Guardamos la tabla orignal en una tabla auxilar
            Node [] aux = table;
            // Sustituimos las tablas de nodos y usados por una del doble de su tamaño
            table = new EDLinkedHashSet.Node[aux.length*2];
            used = new boolean[aux.length*2];
            // Duplicamos el limite de onemos a cero el contador de celdas usadas
            dirty = 0;
            // Duplicamos el limite de elementos
            rehashThreshold *= 2;

            // Recorremos la lista de nodos auxiliar añadiendo cada uno a la tabla
            Node n = first;
            while (n != null) {
                if (n == first){
                    add((T) n.data);
                    first = n;
                    n = n.next;
                }
                else if (n == last){
                    add((T) n.data);
                    last = n;
                    n = n.next;
                }
                else {
                    add((T) n.data);
                    n = n.next;
                }
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
    public boolean contains(Object item) {
        if (item == null)
            throw new NullPointerException();

        int code = hash((T) item);

        while(used[code] == true) {
            if (table[code]!=null && table[code].data.equals(item))
                return true;

            code = (code + 1) % table.length;
        }

        return false;
    }

    @Override
    public Iterator<T> iterator()  { throw new UnsupportedOperationException(); }


    @Override
    public Object[] toArray() {
        Object[] v = new Object[size];

        Node ref = first;
        int i = 0;
        while (ref != null) {
            v[i] = ref.data;
            ref = ref.next;
            i++;
        }

        return v;
    }

    @Override
    public <T1> T1[] toArray(T1[] a)  {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T item) {
        if (item == null)
            throw new NullPointerException();

        int code = hash(item);
        int  free = -1;

        while (used[code]) {
            if (table[code] == null && free == -1)
                free = code;

            if (table[code]!= null && table[code].data.equals(item))
                return false;

            code = (code + 1) % table.length;
        }

        if (free == -1) {
            free = code;
            used[code] = true;
            dirty++;
        }

        Node n = new Node(item);
        table[code] = n;
        if (last == null)
            first = last = n;
        else {
            n.prev = last;
            last.next = n;
            last = n;
        }
        size++;
        rehash();

        return true;
    }

    @Override
    public boolean remove(Object item) {
        // Si el item a eliminar es nulo lanzamos una excepcion
        if ( item != null ){
            int code = hash((T) item);

            // Si el elemento a eliminar es el unico de la tabla
            if(used[code] && table[code] != null && table[code].data.equals(item) && size == 1){
                table[code] = null;
                first = null;
                last = null;
                size--;
                return true;
            }

            // Si el elemento a eliminar es el primero de la tabla
            if (used[code] && first == table[code] && table[code].data.equals(item)){
                first = table[code].next;
                first.prev = null;
                table[code] = null;
                size--;
                return true;
            }

            // Recorremos la tabla de usados
            while (used[code]) {
                if(table[code] != null && table[code].data.equals(item)){

                    // Si el elemento a eliminar es el ultimo de la lista
                    if (last == table[code]) {
                        last = table[code].prev;
                        last.next = null;
                        table[code] = null;
                        size--;
                        return true;
                    }

                    // Si el elemento a eliminar se encuentra en el medio
                    table[code].prev.next = table[code].next;
                    table[code].next.prev = table[code].prev;
                    table[code] = null;
                    size--;
                    return true;
                }
                code = (code + 1) % table.length;
            }
            return false;
        }
        throw new NullPointerException();
    }

    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int s = size();
        for(T item: c)
            add(item);

        return (s != size());
    }

    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }


    @Override
    public void clear() {
        size = dirty  = 0;
        first = last = null;

        for (int i=0; i < table.length; i++) {
            used[i] = false;
            table[i] = null;
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Table :{");
        boolean coma = false;
        for (int i = 0; i < table.length; i++){
            if (table[i] != null) {
                if (coma)
                    sb.append(", ");
                sb.append(i + ": " + table[i].data);
                coma = true;
            }
        }

        sb.append("}\n");

        sb.append("Ordered: [");
        Node ref = first;
        coma = false;
        while (ref != null) {
            if (coma)
                sb.append(", ");
            sb.append(ref.data);
            ref = ref.next;
            coma = true;
        }
        sb.append("]\n");
        sb.append("size: " + size);
        sb.append(", capacity: " + table.length);
        sb.append(", rehashThreshold: " + rehashThreshold);

        return sb.toString();
    }

}
