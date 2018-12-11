package practica9;

public class Heapsort {

	private static void swap(int [] v, int p1, int p2) {
		int aux = v[p1];
		v[p1] = v[p2];
		v[p2] = aux;
	}
	
	private static void sink(int[] v, int p, int size) { //Version para un maxHeap del metodo 'sink(...)' de la clase EDPriorityQueue
	    int max, lChild, rChild;
	    boolean rightPlace = false;
	    while (p < size/2 && !rightPlace) { // Mientras queden hijos que evaluar
	        max = p; lChild = p*2+1; rChild = lChild+1;
	        // Comprobamos cual es el mayor elemento de los tres
	        if (v[p] < v[lChild]) {
	            max = lChild;
            }
            if (rChild < size && v[max] < v[rChild]) {
                max = rChild;
            }
            if (p != max) { // Si hay algun hijo que hundir
                swap(v ,p, max);
                p = max;
            }
            else {
                rightPlace = true;  // Cuando el padre sea el mayor elemento finalizamos
            }
        }
	}
	
	private static void heapify (int[] v) {
		for (int i=v.length-1; i>=0; i--) {
			sink(v,i,v.length);
		}
	}
	
	static public void heapsort (int [] v) {
		
		heapify(v);
		
		int n=v.length;
		while (n>1) {
			swap (v, 0,n-1);
			n--;
			sink(v, 0,n);
		}
	}
}
