package practica7;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BinaryExpressionTree {
	
	//private BinaryTree<Character> exp = null;

	//cronstructor de árbol de expresiones a partir de una expresión notación potfijas completamente parentizada
			
	public static BinaryTree<Character> buildBinaryExpressionTree(String chain) {

		BinaryTree<Character> tree = new EDBinaryTree<Character>();
		if (chain.length()==0) return tree;
		
		Stack<EDBinaryTree<Character>> pila = new Stack<EDBinaryTree<Character>>();
		boolean error = false;
		if (chain != null) {
			int i=0;

			while (i < chain.length() && !error) {
				Character c = chain.charAt(i);
				if (Character.isDigit(c)) {
					EDBinaryTree<Character> single = new EDBinaryTree<Character>(c);
					pila.push(single);
				}
				else if (isValidOperator(c)) {
					EDBinaryTree<Character> rightS=null, leftS=null;
					if (!pila.empty()) {
						rightS = pila.peek();
						pila.pop();
					}
					else error = true;  //wrong input chain
					if (!error) {
						if (!pila.empty()) {
							leftS = pila.peek();
							pila.pop();
						}
						else error = true; //wrong input chain
					}
					if (!error) {
						EDBinaryTree<Character> b= new EDBinaryTree<Character>(c,leftS,rightS);
						pila.push(b);
					}
				}
				else if (c!=' ') error = true; //wrong input chain, operator
				i++;  //next character in chain
			}
			if (!error) {
				if (!pila.empty()) {
					tree = pila.peek();
					pila.pop();
				}
				else error = true;; //wrong input chain
			}
			if (!pila.empty()) error=true; //stack must be empty at the end
		}
		if (error ) return null; 
		else return tree;
	}

    static String toPostfix (String chain) {
        Stack<Character> pila = new Stack<Character>();
        String out = "";
        boolean error = false;
        if (chain != null) {
            int i = 0;

            while (i < chain.length() && !error) {
                Character c = chain.charAt(i);
                if (Character.isDigit(c)) out += c;
                else if (isValidOperator(c)) pila.push(c);
                else if (c == '(') pila.push(c);
                else if (c == ')') {
                    if (pila.isEmpty()) error = true;
                    boolean salir = false;
                    while (!pila.isEmpty() && !salir) {
                        char e = pila.pop();
                        if (e != '(') out += e;
                        else salir = true;
                    }
                    if (!salir) error = true;
                } else error = true;
                i++;
            }
        }
        if (error) {
            System.out.println("expressión de entrada incorrecta");
            out = null;
        }
        return out;
    }

    /** Detemrines whether, on a expression tree, the digits are on the leaves and valid operators on the internal
     * nodes.
     * @param tree
     * @return <code>true</code> if digits are only on the leaves.
     * STUDENT NOTE: ITS ASSUMED THAT THE BINARY TREE IS EXTENDED
     */
	public static boolean digistsOnLeaves(BinaryTree<Character> tree) {
	    if (!tree.isLeaf() && isValidOperator(tree.getData())) { // If root is not a leaf and conatins a valid operator
	        return digistsOnLeaves(tree.getLeftSubTree()) && digistsOnLeaves(tree.getRightSubTree()); // Recursive call for both childs
        }
        else if ( tree.isLeaf() && 0 <= Character.getNumericValue(tree.getData()) && Character.getNumericValue(tree.getData()) <= 9 ) { // If root is a leaf and contains a valid operand
            return true;
        }
        else return false; // If none of the previous conditions are satisfied
	}

	private static boolean isValidOperator(Character c) {
		if (c=='+' || c=='-' || c=='*' || c=='/' || c=='^')
			return true;
		else return false;
	}

    /** Computes the result of executing the operation on a evaluation tree.
     * @param tree
     * @return The result of the evaluation.
     */
	public static float evaluate(BinaryTree<Character> tree) {
	   if ( digistsOnLeaves(tree) == true ) { // If the tree is an expression tree
	       if ( !tree.isLeaf() ) { // If the tree is an operator
	           switch (tree.getData()) { // Recursive calls for each operation
                   case '+':
                       return evaluate(tree.getLeftSubTree()) + evaluate(tree.getRightSubTree());
                   case '-':
                       return evaluate(tree.getLeftSubTree()) - evaluate(tree.getRightSubTree());
                   case'/':
                       return evaluate(tree.getLeftSubTree()) / evaluate(tree.getRightSubTree());
                   case'*':
                       return evaluate(tree.getLeftSubTree()) * evaluate(tree.getRightSubTree());
                   case'^':
                       return (float) Math.pow(evaluate(tree.getLeftSubTree()),evaluate(tree.getRightSubTree()));
               }
           }
           else return Character.getNumericValue(tree.getData()); // If the tree is an operand
       }
       return -1; // If the tree is not an expression tree
	}

    /** Returns a list with the result of crossing the nodes of a expression tree in inOrder. The list will be
     * completely parenthesised.
     * @param tree
     * @return The list
     */
	public static List<Character> asListInorder(BinaryTree<Character> tree) {
		// TODO Ejercicio 4
        return null;
	}
}
