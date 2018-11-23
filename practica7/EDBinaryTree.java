package practica7;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class EDBinaryTree<T> implements BinaryTree<T> {
	
	protected static class BinaryNode<U> {
		protected U data;
		protected BinaryNode<U> left;
		protected BinaryNode<U> right;

	
		//constructors
		public BinaryNode (U data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
		
		public String toString() {
			return this.data.toString();
		}	
	}
	
	BinaryNode<T> root;  //reference to the node root of the tree
	
	//constructor empty binary tree
	public EDBinaryTree(){
		root = null;
	}
	
	//constructor: creates a new tree with a node with data in the root and left and right empty
	public EDBinaryTree(T data) {
		BinaryNode<T> n = new  BinaryNode<T>(data);
		this.root = n;
	}
	
	//constructor: given a node, constructs a binary tree whose root is that node
	private EDBinaryTree(BinaryNode<T> n){
		this.root = n;
	}
	
	//constructor: given data, and two binary trees constructs a new binary tree
	public EDBinaryTree(T data, EDBinaryTree<T> leftS, EDBinaryTree<T> rightS) {
		BinaryNode<T> root = new BinaryNode<T>(data);
		if (leftS != null) {
			root.left = leftS.root;
		}
		if (rightS != null) {
			root.right = rightS.root;
		}
		this.root = root;
	}

	// constructor: given a vector with the elements sorted by levels constructs a new binary tree.
	public EDBinaryTree(List<T> vector) {
		root = fromList(vector, 0);
	}

	private BinaryNode<T> fromList(List<T> list, int current) {
		if (current >= list.size())
			return null;

		BinaryNode<T> n = new BinaryNode<>(list.get(current));
		n.left = fromList(list, current*2+1);
		n.right = fromList(list, current*2+2);

		return n;
	}
	
	public boolean isEmpty() {
		return (this.root == null);
	}
	
	public boolean isLeaf() {
		if (root != null) {
			if (root.left == null && root.right == null)
				return true;
			else 
				return false;
		}
		else return false;
	}
	
	public T getData () {
		if (root != null) 
			return root.data;
		else
			return null;
	}
	
	public EDBinaryTree<T> getLeftSubTree(){
		if (root != null) {
			if (root.left != null) {
				EDBinaryTree<T> leftTree = new EDBinaryTree<T>(root.left);
				return leftTree;
			}
			else 
				return new EDBinaryTree<T>();  //empty tree
		}	
		else
				return null;
	}
	
	public EDBinaryTree<T> getRightSubTree(){
		if (root != null) {
			if (root.right != null) {
				EDBinaryTree<T> rightTree = new EDBinaryTree<T>(root.right);
				return rightTree;
			}
			else 
				return new EDBinaryTree<T>();  //empty tree
		}	
		else
				return null;
	}

    public int size () {
        if (isEmpty())
            return 0;
        else return 1+getLeftSubTree().size()+getRightSubTree().size();
    }

    /**
     * Determines wether the tree is extended. That is that each node has either two or none siblings.
     * @return <code>true</code> if extended.
     */
    public boolean isExtended() {
        if (isEmpty() || isLeaf()) { // If the root BinaryNode is a leaf or is empty
            return true;
        }
        if (root.left == null || root.right == null) {  // Returns false if root BinaryNode has only one child
            return false;
        }
        return getLeftSubTree().isExtended() == true && getRightSubTree().isExtended() == true; // Recursion call
    }

    // methods needed for toString
    private static StringBuilder repeated(char c, int times) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < times; i++)
	        sb.append(c);

	    return sb;
    }

    private int depth(BinaryNode<T> node) {
	    if (node == null)
	        return 0;

	    int ld = depth(node.left) + 1;
	    int rd = depth(node.right) + 1;

	    return ld > rd ? ld : rd;
    }

    private int createSpaces(List<String> offset, List<String> separator) {
	    int depth = depth(root);

	    offset.clear();
	    separator.clear();

	    if (depth < 1)
	        return 0;

	    int pad = 1;
	    for (int i = 0; i < depth; i++) {
	        offset.add(0, repeated(' ', pad -1 ).toString());
	        pad = 2*pad+1;
	        separator.add(0, repeated(' ', pad).toString());
        }

        return depth;
    }

	@Override
	public String toString() {
		StringBuilder resultado = new StringBuilder();
        Queue<BinaryNode<T>> q = new LinkedList<>();
        List<String> margen = new LinkedList<>();
        List<String> separacion = new LinkedList<>();
        int altura =  createSpaces(margen, separacion);

        if (altura == 0) {
            resultado.append("------------\n");
            resultado.append(" Empty tree\n");
            resultado.append("------------\n");
            return resultado.toString();
        }

 	    StringBuilder barra = repeated('-',   ((1 << (altura-1)) * 4) - 3);

        int nactual = 0;
        int cuenta = 1;
        resultado.append(barra).append('\n').append(margen.get(nactual));
        q.add(root);

        while (nactual < altura) {
            BinaryNode<T> n = q.remove();
            String dato = " ";
            if (n != null)
                dato = n.data.toString();

            resultado.append(dato);
            cuenta--;
            if (cuenta > 0)
                resultado.append(separacion.get(nactual));
            else {
                resultado.append('\n');
                nactual++;
                cuenta = 1 << nactual;

                if (nactual < altura)
                    resultado.append(margen.get(nactual));
                else
                    resultado.append(barra).append('\n');
            }

            if (nactual < altura ) {
                if (n == null) {
                    q.add(null);
                    q.add(null);
                } else {
                    q.add(n.left);
                    q.add(n.right);
                }
            }
        }
        return resultado.toString();
	}
}
