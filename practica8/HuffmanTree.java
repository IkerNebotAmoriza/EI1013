package practica8;

import java.io.IOException;
import java.util.*;


public class HuffmanTree {
	
	private static class Node {

		public boolean isLeaf = false;
		public char c = '\0';
		public float f = (float) 0.0;
		public Node left = null;
		public Node right =  null;
		
		public Node(boolean leaf, char chr, float freq) { isLeaf = leaf; c = chr; f = freq; left = null; right = null; };
		
		public String toString()
		{
			StringBuilder retVal = new StringBuilder();
			retVal.append("[" + isLeaf + ", " + c + ", " + f + "]");
			return retVal.toString();
		}

		public boolean equals(Object obj) {
		    if (obj == null)
		        return false;

		    if (obj.getClass() != getClass())
		        return false;

		    Node root = (Node) obj;

		    if (this.isLeaf && root.isLeaf && (this.c == root.c))
		        return true;

		    if (!this.isLeaf && !root.isLeaf && left.equals(root.left) && right.equals(root.right))
		        return true;

		    return false;
        }
	}

	
	Node root = null; //the Huffman binary tree


    //Builds a binaryTree for Huffman codification given the array of characters and its frequency	
	public HuffmanTree(int freqs[],char chars[])
	{
		Comparator<Node> comparator = new NodeComparator();
		
		//codesList = new LinkedList[256];

        //build Huffman binary tree
		PriorityQueue<Node> queue = new PriorityQueue<Node>(256, comparator);

		for (int i=0; i<256; i++)
		{
			if (freqs[i] > 0) {
				Node n = new Node(true, chars[i], freqs[i]);
				queue.add(n);
			}
		}
		
		while (queue.size() > 1)
		{
			Node n1 = queue.remove();
			Node n2 = queue.remove();
			
			Node n3 = new Node(false,'\0',n1.f+n2.f);
			n3.left = n1;
			n3.right = n2;
			queue.add(n3);
		}

		root = queue.remove();

	}

    /** Construye un árbol de Huffman a partir de los carateres y sus codificaciones.
     * @param codes Map en el que por cada caracter se contiene ña lista de 0s y 1s que lo codifica.
     */
	public HuffmanTree (Map<Character, List<Integer>>  codes) {
		root = new Node(false, '\0', (float) 0.0);
		Set<Character> ks = codes.keySet();
		Iterator<Character> cIterator = ks.iterator();
		char cAux; Node nAux;
		List<Integer> lAux;

		if (!ks.isEmpty()) {
			while (cIterator.hasNext()) {
				cAux = cIterator.next();
				lAux = codes.get(cAux);
				nAux = root;

				for (int i = 0; i < lAux.size(); i++) {
					if (lAux.get(i) == 0) {
						if (nAux.left != null) {
							nAux = nAux.left;
						}
						else {
							nAux.left = new Node(false, '\0', (float) 0.0);
							nAux = nAux.left;
						}
					}
					else {
						if (nAux.right != null) {
							nAux = nAux.right;
						}
						else {
							nAux.right = new Node(false, '\0', (float) 0.0);
							nAux = nAux.right;
						}
					}
				}
				nAux.isLeaf = true;
				nAux.c = cAux;
			}
		}
	}

	public class NodeComparator implements Comparator<Node>
	{
	    @Override
	    public int compare(Node x, Node y)
	    {
	        // Assume both nodes can be null
	    	
	        if (x == null && y == null)
	        {
	            return 0;
	        }
	        if (x.f < y.f)
	        {
	            return -1;
	        }
	        if (x.f > y.f)
	        {
	            return 1;
	        }
	        
	        return 0;
	    }
	}

    /** Dado un charácter c, devuelve la lista de 0s y 1s que lo codifica dentro del árbol. <cose>null</cose> en el
     *  caso de que el carácter no se encuentre en el árbol.
     * @param c El carácter
     * @return  Lista de 0s y 1s o <code>null</code>.
     */
	public List<Integer> findCode(char c) {
	    List<Integer> path = findCode(root, c); // Private method call
	    if (path != null) {
	        Collections.reverse(path);
        }
	    return path;
    }

	private List<Integer> findCode(Node n, char c) {
        if (n != null){
            if (n.isLeaf){ // If the current node is a leaf, checks its data and returns an empty array if equals 'c'
                if (n.c == c){
                    return new ArrayList<Integer>();
                }
            }
            else { // If its not a leaf, makes a recursive call to its children
                List<Integer> left = findCode(n.left, c);
                if (left != null) { // If the character is on the left branch adds '0' to the given array
                    left.add(0);
                    return left;
                }
                List<Integer> right = findCode(n.right, c);
                if (right != null) { // If the character is on the right branch adds '1' to the given array
                    right.add(1);
                    return right;
                }
            }
        }
        return null; // If the character is not in the tree returns null
    }

    /** Dada un String compuesto  por 0s y 1s que representan la codificacion de uno o mas carácteres. Decodifica y
     * añade a los carateres a una lista.
     * @param str   0s y 1s
     * @param l     Lista conteniendo los caracteres decodificados.
     */
	public void decode(String str, List<Character> l) {
	    int [] codes = new int[str.length()+1]; // Vector where all decisions extracted from 'str' are stored
	    for (int i=0; i < str.length(); i++) {
	        codes[i] = (Character.getNumericValue(str.charAt(i)));
        }
        Node node = root;
        for (int i = 0; i < codes.length; i++) { // For each decision in 'codes' and an extra iteration
            if (!node.isLeaf) { // If 'node' its not a leaf
                if (codes[i] == 0) {
                    node = node.left; // Go left
                } else {
                    node = node.right; // Go right
                }
            } else { // If 'node' its a leaf, adds its data to the list and freezes the index for one iteration
                l.add(node.c);
                node = root;
                i--;
            }
        }
	}

	public static HuffmanTree createFromFile(String filename) throws IOException {
         Frequencies table = new Frequencies();
         HuffmanTree tree = null;

        table.loadFile(filename);
        tree = new HuffmanTree(table.frequenciesTable(),table.charsTable());

        return tree;
    }

    @Override
    public boolean equals(Object obj) {
        return (root == obj) || (root != null && (getClass() == obj.getClass()) && root.equals(((HuffmanTree) obj).root));
    }
}
