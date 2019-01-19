package examen;

import practica10.EDMatrixGraph;

import java.util.*;


/** Implementation of interface Graph using adjacency lists
 * @param <T> The base type of the nodes
 * @param <W> The base type of the weights of the edges
 */
public class EDListGraph<T,W> implements EDGraph<T,W> {
	@SuppressWarnings("hiding")
	private class Node<U> {
		U data;
		List<EDEdge<W>> lEdges;

		Node (U data) {
			this.data = data;
			this.lEdges = new LinkedList<EDEdge<W>>();
		}



        @Override
		public boolean equals (Object other) {
			if (this == other)
			    return true;
			if (!(other instanceof Node))
			    return false;
			Node<T> anotherNode = (Node<T>) other;
			return data.equals(anotherNode.data);
		}
	}

	// Private data
	private ArrayList<Node<T>> nodes;
	private int size; //real number of nodes
	private boolean directed;
	private boolean weighted;



	public EDListGraph() {
		directed = false; //not directed
		weighted = false;
		nodes =  new ArrayList<>();
		size =0;
	}

    /** Constructor
     * @param dir <code>true</code> for directed edges;
     * <code>false</code> for non directed edges.
     */
	public EDListGraph (boolean dir) {
		directed = dir;
		weighted = false;
		nodes =  new ArrayList<Node<T>>();
		size =0;
	}

	public EDListGraph (boolean dir, boolean wei) {
		directed = dir;
		weighted = wei;
		nodes =  new ArrayList<Node<T>>();
		size = 0;
	}

	public EDListGraph (EDMatrixGraph<T,W> g) {
	    directed = g.isDirected();
	    weighted = g.isWeighted();
        size = g.getSize();
	    nodes = new ArrayList<Node<T>>();
	    for (int i=0; i < size; i++){
	    	nodes.add(new Node<>(null));
		}

		HashMap<T, Integer> nodeMap = g.getNodes(); // Map with 'g' node, index for each node in the array
		boolean [][] edgeMatrix = g.getAdjacencyMatrix(); // 'g' adjacency matrix

        Iterator<Map.Entry<T, Integer>> iterator = nodeMap.entrySet().iterator(); // EntrySet iterator
        Map.Entry<T, Integer> entry;

        while (iterator.hasNext()) { // While there are nodes left
            entry = iterator.next();
            Node<T> n = new Node<T>(entry.getKey()); // Creates the new node
            int pos = entry.getValue();

            for (int i = 0; i < edgeMatrix.length; i++) { // Checks adjacency matrix
                if (edgeMatrix[pos][i]) {
                    n.lEdges.add(new EDEdge<W>(pos, i)); // If true adds an edge
                }
            }
            nodes.set(pos, n);
        }
	}

    @Override
    public int getSize() {
	    return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

    @Override
    public boolean isWeighted() {

		return this.weighted;
	}

	public boolean isDirected() {
		return this.directed;
	}


	@Override
	public int getNodesCapacity() {
		return nodes.size();
	}

	@Override

	/**
	 * Inserts the item as a new Node of the Graph
	 * The new node is stored in a null position of the array nodes.
	 * If there isn't a null position, then is stored at the end of the array
	 * The method returns the index of the position in the array where the
	 * new node is stored.for (int i=0; i<this.size; i++)
     * 			this.nodes.add(new Node<T>());
	 * Two nodes cannot have the same item. Then, if already exists a node with
	 * item, it returns the position were it is already stored
	 */
	public int insertNode(T item) {

	    int i = 0; int pos=-1; int free=nodes.size();
	    while (i<nodes.size() && pos==-1) {

	    	if (nodes.get(i).data == null) free = i;
	    	else if (nodes.get(i).data.equals(item)) pos = i;
	    	i++;
	    }
	    if (pos == -1) { //No esta
	    	Node<T> newNode = new Node<T>(item);
	    	if (free<nodes.size()) nodes.set(free,newNode);
	    	else {free=size; nodes.add(newNode);}
	    	size++;
	    	return free;
	    }
	    else return pos;
	}

	/**
	 * getNodeIndex (T item)  Returns the index of the Node with item in the array of Nodes
	 * It returns -1 if there is no node with that item
	 */
    @Override
    public int getNodeIndex(T item) {
		Node<T> aux = new Node<T>(item);
		return nodes.indexOf(aux);
	}

	@Override
	public T getNodeValue(int index) throws IndexOutOfBoundsException{
		return nodes.get(index).data;

	}

	/**
	 * insertEdge (EDEdge edge) Inserts the edge in the graph. If the graph is not directed, the method
	 * inserts also the reserve edge
	 * Returns true if the edge has been inserted and false otherwise
	 */
    @Override
    public boolean insertEdge(EDEdge<W> edge) {
		int sourceIndex = edge.getSource();
		int targetIndex = edge.getTarget();
		if (sourceIndex >=0 && sourceIndex<nodes.size() && targetIndex >=0 && targetIndex<nodes.size()) {
			Node<T> nodeSr = nodes.get(sourceIndex);
			Node<T> nodeTa = nodes.get(targetIndex);
			if (nodeSr.data!=null && nodeTa.data != null) {
			   if (!nodeSr.lEdges.contains(edge)) {
				   nodeSr.lEdges.add(edge);
				   nodes.set(sourceIndex,nodeSr);
				   if (!directed) {//no dirigido
					  EDEdge<W> reverse = new EDEdge<W>(targetIndex,sourceIndex,edge.getWeight());
					  nodeTa.lEdges.add(reverse);
					  nodes.set(targetIndex, nodeTa);
				   }
				   return true;
			    }
			}
		}
		return false;
	}

	public boolean insertEdge (T fromNode, T toNode) {
		int fromIndex = getNodeIndex(fromNode);
		if (fromIndex <0)
			return false;
		int toIndex = this.getNodeIndex(toNode);
		if (toIndex <0)
			return false;
		W label = null;
		EDEdge<W> e = new EDEdge<W>(fromIndex,toIndex,label);
		this.insertEdge(e);
		return true;
	}

	@Override
	public EDEdge<W> getEdge(int source, int target) {
		if (source <0 || source >= nodes.size()) return null;

		Node<T> node = nodes.get(source);
		if (node.data == null ) return null;
		for (EDEdge<W> edge: node.lEdges)

			if (edge.getTarget() == target) return edge;

		return null;
	}

    @Override
    public Map<T, Integer> getNodes() {
        Map<T, Integer> ret = new HashMap<>();

        for (int i = 0;i < nodes.size(); i++)
            ret.put(nodes.get(i).data, i);

        return ret;
    }

    @Override
    public Set<Integer> getOutgoing(int index) {

	    if (index<0 || index> nodes.size()) return null;

	    if (nodes.get(index) == null) return null;

        Set<Integer> ret = new HashSet<>();

        for (EDEdge<W> edge: nodes.get(index).lEdges)
            ret.add(edge.getTarget());

        return ret;
    }

	@Override
	public EDEdge<W> removeEdge(int source, int target) {
		if (source <0 || source >= nodes.size() || target<0 || target >= nodes.size()) return null;
		if (nodes.get(source).data!=null && nodes.get(target).data!=null) {
			EDEdge<W> edge = new EDEdge<W>(source, target);
			Node<T> node = nodes.get(source);
			int i = node.lEdges.indexOf(edge);
			if (i != -1) {
				edge = node.lEdges.remove(i);
				if (!directed) {
					EDEdge<W> reverse = new EDEdge<>(target,source);
					nodes.get(target).lEdges.remove(reverse);
				}
				return edge;
			}
		}
		return null;
	}

	@Override
	public T removeNode(int index) {
		if (index >=0 && index < nodes.size()){
			if (!directed) {
				Node<T> node = nodes.get(index);
				for (EDEdge<W> edge: node.lEdges ) {
					int target = edge.getTarget();
					W label = edge.getWeight();
					EDEdge<W> other = new EDEdge<>(target,index,label);
					nodes.get(target).lEdges.remove(other);
				}
			}
			else { //directed
				for (int i=0; i<nodes.size(); i++) {
					if (i!=index && nodes.get(i).data !=null) {
						Node<T> node = nodes.get(i);
						for (EDEdge<W> edge: node.lEdges) {
							if (index == edge.getTarget()) //any weight/label
								node.lEdges.remove(edge);
						}
					}
				}
			}

			Node<T> node = nodes.get(index);
			node.lEdges.clear();
			T ret = node.data;
			node.data = null; //It is not remove, data is set to null
			nodes.set(index, node);
			size--;
			return ret;
		}
		return null;
	}




	public int[] breathFirstSearch (int start) {
		Queue<Integer> qu = new LinkedList<>();
		if (start<0 || start >= nodes.size()) return new int[0];

		//Declare an array 'parent' and initialize its elements to -1
		int [] parent = new int[nodes.size()];
		for (int i=0; i<parent.length; i++)
			parent[i]=-1;


		parent[start] = start;
		qu.add(start);

		while (!qu.isEmpty()) {
			int current = qu.remove();

			for (EDEdge<W> edge: nodes.get(current).lEdges) {
				int neighbor = edge.getTarget();
				if (parent[neighbor] == -1) {
					parent[neighbor] = current;
					qu.add(neighbor);
				}
			}
		}
		return parent;
	}



	public int[] DepthFirstSearch(int start, int exit) {
    	int [] path = new int[nodes.size()];
    	// List with discovered nodes to avoid loops
    	for (int i = 0; i < path.length; i++) {
    		path[i] = -1;
		}
		// If both index are in range
		if (start >= 0 && exit >= 0 && start < size && exit < size && nodes.get(start) != null) {
			path[start] = start;
			dfs(start, exit, path); // Private method call
		}
		return path;
	}

	private void dfs(int current, int exit, int [] path) {
    	for (EDEdge<W> edge: nodes.get(current).lEdges) {
    		int tg = edge.getTarget();
    		if (path[tg] == -1) {
    			path[tg] = current;
    			if (current == exit) { // If we are on the exit
    				break; // End of recursion
				}
    			dfs(tg, exit, path); // Recursive call
			}
		}
	}


	public void printGraphStructure() {
		//System.out.println("Vector size= " + nodes.length);
		System.out.println("Vector size " + nodes.size());
		System.out.println("Nodes: "+ this.getSize());
		for (int i=0; i<nodes.size(); i++) {
			System.out.print("pos "+i+": ");
	        Node<T> node = nodes.get(i);
			System.out.print(node.data+" -- ");
			Iterator<EDEdge<W>> it = node.lEdges.listIterator();
			while (it.hasNext()) {
					EDEdge<W> e = it.next();
					System.out.print("("+e.getSource()+","+e.getTarget()+", "+e.getWeight()+")->" );
			}
			System.out.println();
		}
	}

	
	public Set<T> degree1() {
        HashSet<T> d1Nodes = new HashSet<T>(); // Return set
        HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
        for (int i = 0; i < size; i++) { // Map with counters for each node index
            m.put(i,0); // Index and counter init
        }
        Node<T> actual;
        int aux;
        for (int i = 0; i < size; i++) { // for each node on the nodes list
            actual = nodes.get(i);
            for (int j = 0; j < actual.lEdges.size(); j++) { // for each edge on the actual node
                aux = m.get(actual.lEdges.get(j).getTarget());
                m.put(actual.lEdges.get(j).getTarget(), aux+1);
            }
        }
        for (int i = 0; i < size; i++) {
            if (m.get(i) == 1) {
                d1Nodes.add(nodes.get(i).data);
            }
        }
        return d1Nodes;
	}

	public List<T> findPath(int entry, int exit) {
    	int [] path = DepthFirstSearch(entry, exit);
    	ArrayList<T> aux = new ArrayList<T>();
    	ArrayList<T> data = new ArrayList<T>();

    	while (exit != entry) { // While not in entry position
    		aux.add(nodes.get(exit).data); // Adds node to list
    		exit = path[exit]; // Travels path backwards
		}
		aux.add(nodes.get(entry).data); // Adds entry node

    	for (int i = aux.size()-1; i >= 0; i--) { // Reverse data list
    		data.add(aux.get(i));
		}
		return data;
	}
}
