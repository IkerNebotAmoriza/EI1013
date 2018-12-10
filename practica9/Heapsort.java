package practica9;

public class Heapsort {

	private static void swap(int [] v, int p1, int p2) {
		int aux = v[p1];
		v[p1] = v[p2];
		v[p2] = aux;
	}
	
	private static void sink(int[] v, int p, int size) {
		// TODO Ejercicio 4
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
