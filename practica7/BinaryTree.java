package practica7;


public interface BinaryTree<T> {
	
public boolean isEmpty();
public boolean isLeaf();
public T getData();
public BinaryTree<T> getLeftSubTree();
public BinaryTree<T> getRightSubTree();
}
