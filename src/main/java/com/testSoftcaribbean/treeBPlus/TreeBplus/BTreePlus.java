package com.testSoftcaribbean.treeBPlus.TreeBplus;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.testSoftcaribbean.treeBPlus.Entity.Client;
import com.testSoftcaribbean.treeBPlus.Entity.Response;

public class BTreePlus {
    /** Grado del arbol m. */
	private int m;

	/** raiz del arbol */
	private Node root;

	/**Cadena donde se almacena la representacion del arbol b + */
	private String stringTree;

	public BTreePlus() {

	}

	/**
	 *  Método inicializa el árbol B Plus. Establece el grado del árbol B-Plus como orden de entrada.
	 *
	 * @param order
	 */
	public void initialize(int order) {
		this.m = order;
		this.root = null;
	}

	/**
	 * Este método se utiliza para insertar un nuevo par clave-valor en el árbol B Plus
	 *
	 * @param key
	 * @param client
	 */
	public void insert(double key, Client client) {

		// Caso 1: arbol vacío
		if (null == this.root) {
			/**se crea Nodo,se inserta la clave a la lista y nodo se establece como raiz */
			Node newNode = new Node();
			newNode.getLeaves().add(new Leaf(key, client));
			this.root = newNode;
			// nodo sin padre por ser raiz
			this.root.setParent(null);
		}

		// Caso 2: se tiene un unico nodo y no esta lleno
		else if (this.root.getChildren().isEmpty() && this.root.getLeaves().size() < (this.m - 1)) {
			//Para las inserciones hasta que se llene la raiz, actualizamos
            //el nodo raiz y agragamos las nuevas claves-valor
			insertWithinExternalNode(key, client, this.root);
		}

		// Caso 3: Inserción normal
		else {
			Node curr = this.root;
			// Como insertamos el elemento solo en el nodo externo, pasamos al último nivel
			while (!curr.getChildren().isEmpty()) {
				curr = curr.getChildren().get(binarySearchWithinInternalNode(key, curr.getLeaves()));
			}
			insertWithinExternalNode(key, client, curr);
			if (curr.getLeaves().size() == this.m) {
				// si se llena el nodo externo, lo debemos partir
				splitExternalNode(curr, this.m);
			}
		}

	}

	/**
	 * insertamos el par clave-valor para los nodos externos.
	 *
	 * @param key
	 * @param value
	 * @param node
	 */
	private void insertWithinExternalNode(double key, com.testSoftcaribbean.treeBPlus.Entity.Client client, Node node) {
		// busqueda binaria para encontrar el index,que sera el lugar a insertar el nodo de manera 
        // ordenada
		int indexOfKey = binarySearchWithinInternalNode(key, node.getLeaves());
		if (indexOfKey != 0 && node.getLeaves().get(indexOfKey - 1).getKey() == key) {
			// la llave existe, agregamos el valor a la lista de claves
			node.getLeaves().get(indexOfKey - 1).getClients().add(client);
		} else {
			// clave no existente. agregar clave-valor
			Leaf newLeaf = new Leaf(key, client);
			node.getLeaves().add(indexOfKey, newLeaf);
		}
	}

	/**
	 * partir el nodo esterno
	 *
	 * @param curr
	 * @param m
	 */
	private void splitExternalNode(Node curr, int m) {

		// mitar del nodo
		int midIndex = m / 2;
        //nodo medio
		Node middle = new Node();
        //nodo derecho
		Node rightPart = new Node();

		// se ingresa la parte derecha para tener el elemento medio y los 
        //elementos directamente en el elemento medio
		rightPart.setLeaves(curr.getLeaves().subList(midIndex, curr.getLeaves().size()));
		rightPart.setParent(middle);
		//hacemos el medio como el nodo interno, agregamos solo la clave ya que los nodos internos 
        //del árbol no contienen valores
		middle.getLeaves().add(new Leaf(curr.getLeaves().get(midIndex).getKey()));
		middle.getChildren().add(rightPart);
		// Curr sostiene la parte izquierda, así que se actualiza el
        // nodo dividido para que contenga solo la parte izquierda
		curr.getLeaves().subList(midIndex, curr.getLeaves().size()).clear();

		boolean firstSplit = true;
		// propagar el elemento del medio hacia un nodo padre y fusionarlo
		splitInternalNode(curr.getParent(), curr, m, middle, firstSplit);

	}

	/**
	 * dividimos un nodo interno
	 *
	 * @param curr
	 * @param prev
	 * @param m
	 * @param toBeInserted
	 * @param firstSplit
	 *indica si la división está ocurriendo en el primer nodo interno desde la parte inferior
	 */
	private void splitInternalNode(Node curr, Node prev, int m, Node toBeInserted, boolean firstSplit) {
		if (null == curr) {
			// if we split the root before, then a new root has to be created
            //si partimos la raiz antes, entonces una nueva raiz tiene que haber sido creada
            this.root = toBeInserted;
            //buscamos donde el hijo sera insertado haciendo una
            //busqueda binaria en las claves
			int indexForPrev = binarySearchWithinInternalNode(prev.getLeaves().get(0).getKey(), toBeInserted.getLeaves());
			prev.setParent(toBeInserted);
			toBeInserted.getChildren().add(indexForPrev, prev);
			if (firstSplit) {
				// actualice la lista vinculada solo para la primera división 
                //(para el nodo externo)
				if (indexForPrev == 0) {
					toBeInserted.getChildren().get(0).setNext(toBeInserted.getChildren().get(1));
					toBeInserted.getChildren().get(1).setPrev(toBeInserted.getChildren().get(0));
				} else {
					toBeInserted.getChildren().get(indexForPrev + 1)
							.setPrev(toBeInserted.getChildren().get(indexForPrev));
					toBeInserted.getChildren().get(indexForPrev - 1)
							.setNext(toBeInserted.getChildren().get(indexForPrev));
				}
			}
		} else {
			// fusionamos el nodo interno con el nodo medio + la derecha de la división anterior
			mergeInternalNodes(toBeInserted, curr);
			if (curr.getLeaves().size() == m) {
				// do a split again if the internal node becomes full
				int midIndex = (int) Math.ceil(m / 2.0) - 1;
				Node middle = new Node();
				Node rightPart = new Node();

				// since internal nodes follow a split like the b tree, right
				// part contains elements right of the mid element, and the
				// middle becomes parent of right part
				rightPart.setLeaves(curr.getLeaves().subList(midIndex + 1, curr.getLeaves().size()));
				rightPart.setParent(middle);

				middle.getLeaves().add(curr.getLeaves().get(midIndex));
				middle.getChildren().add(rightPart);

				List<Node> childrenOfCurr = curr.getChildren();
				List<Node> childrenOfRight = new ArrayList<>();

				int lastChildOfLeft = childrenOfCurr.size() - 1;

				// update the children that have to be sent to the right part
				// from the split node
				for (int i = childrenOfCurr.size() - 1; i >= 0; i--) {
					List<Leaf> currKeysList = childrenOfCurr.get(i).getLeaves();
					if (middle.getLeaves().get(0).getKey() <= currKeysList.get(0).getKey()) {
						childrenOfCurr.get(i).setParent(rightPart);
						childrenOfRight.add(0, childrenOfCurr.get(i));
						lastChildOfLeft--;
					} else {
						break;
					}
				}

				rightPart.setChildren(childrenOfRight);

				// update the overfull node to contain just the left part and
				// update its children
				curr.getChildren().subList(lastChildOfLeft + 1, childrenOfCurr.size()).clear();
				curr.getLeaves().subList(midIndex, curr.getLeaves().size()).clear();

				// propogate split one level up
				splitInternalNode(curr.getParent(), curr, m, middle, false);
			}
		}
	}

	/**
	 *fusionar nodos internos
	 *
	 * @param mergeFrom
	 * @param mergeInto
	 */
	private void mergeInternalNodes(Node mergeFrom, Node mergeInto) {
		Leaf keyToBeInserted = mergeFrom.getLeaves().get(0);
		Node childToBeInserted = mergeFrom.getChildren().get(0);
        //buscamos el index donde la clave esta insertado con una busqueda binaria
		int indexToBeInsertedAt = binarySearchWithinInternalNode(keyToBeInserted.getKey(), mergeInto.getLeaves());
		int childInsertPos = indexToBeInsertedAt;
		if (keyToBeInserted.getKey() <= childToBeInserted.getLeaves().get(0).getKey()) {
			childInsertPos = indexToBeInsertedAt + 1;
		}
		childToBeInserted.setParent(mergeInto);
		mergeInto.getChildren().add(childInsertPos, childToBeInserted);
		mergeInto.getLeaves().add(indexToBeInsertedAt, keyToBeInserted);

		// actualizar la lista de enlaces de nodos externos
		if (!mergeInto.getChildren().isEmpty() && mergeInto.getChildren().get(0).getChildren().isEmpty()) {

			//Si la fusión ocurre en el último elemento, solo el puntero
            //entre el nuevo nodo y el último elemento 
            //anterior debe actualizarse
			if (mergeInto.getChildren().size() - 1 != childInsertPos
					&& mergeInto.getChildren().get(childInsertPos + 1).getPrev() == null) {
				mergeInto.getChildren().get(childInsertPos + 1).setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setNext(mergeInto.getChildren().get(childInsertPos + 1));
			}
			// Si se produce una fusión en el último elemento, solo se debe 
            //actualizar el puntero entre el nuevo nodo y el último elemento anterior
			else if (0 != childInsertPos && mergeInto.getChildren().get(childInsertPos - 1).getNext() == null) {
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
			}
            
			// Si se produce una fusión en el medio, entonces el elemento 
            //siguiente y los punteros anterior y siguiente del elemento anterior 
            //deben actualizarse
			else {
				mergeInto.getChildren().get(childInsertPos)
						.setNext(mergeInto.getChildren().get(childInsertPos - 1).getNext());
				mergeInto.getChildren().get(childInsertPos).getNext()
						.setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
			}
		}

	}

	/**
	 * Metodo para la impresion del arbol
	 */
	public Response printTree() {
		stringTree = "";
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(this.root);
		queue.add(null);
		Node curr = null;
		int levelNumber = 2;
		
		stringTree += "Printing level 1 \n\r";
		while (!queue.isEmpty()) {
			curr = queue.poll();
			if (null == curr) {
				queue.add(null);
				if (queue.peek() == null) {
					break;
				}
				stringTree +="\n" + "Printing level " + (levelNumber++) +"\n\r";
				continue;
			}

			printNode(curr);

			if (curr.getChildren().isEmpty()) {
				break;
			}
			for (int i = 0; i < curr.getChildren().size(); i++) {
				queue.add(curr.getChildren().get(i));
			}
		}

		curr = curr.getNext();
		while (null != curr) {
			printNode(curr);
			curr = curr.getNext();
		}
		stringTree +="\n" + "***************************************************************\n\r";
		stringTree +="\n" + "********************* print end **********************\n\r";
		stringTree +="\n" + "***************************************************************\n\r";

		Response responseTree = new Response(stringTree);
		return responseTree;
	}

	/**
	 * Metodo que imprime todo los valores de un nodo hoja
	 *
	 */
	private void printNode(Node curr) {
		for (int i = 0; i < curr.getLeaves().size(); i++) {
			stringTree += "Id -> "+curr.getLeaves().get(i).getKey() + ":(";
			String values = "";
			for (int j = 0; j < curr.getLeaves().get(i).getClients().size(); j++) {
				values = values + curr.getLeaves().get(i).getClients().get(j) + ",";
			}
			stringTree += values.isEmpty() ? ");  " : values.substring(0, values.length() - 1) + ");  ";
		}
		stringTree += " || ";
	}

	/**
	 *Se modifica la busqueda binaria en nodo interno
	 * @param key
	 * @param keyList
	 * @return la primer index de la lista con la clave es mayor que la clave de entrada
	 */
	public int binarySearchWithinInternalNode(double key, List<Leaf> leavesList) {
		int st = 0;
		int end = leavesList.size() - 1;
		int mid;
		int index = -1;
		// retornamos el primer index si la clave es menor que el primer elemento
		if (key < leavesList.get(st).getKey()) {
			return 0;
		}
        // retornamos el tamaño de la lista  + 1 como la nueva posición de la clave si es mayor que el ultimo 
        //elemento
		if (key >= leavesList.get(end).getKey()) {
			return leavesList.size();
		}
		while (st <= end) {
			mid = (st + end) / 2;
            // la condición garantiza que encontramos un lugar donde la clave
            //es mas pequela que el elemento en este index y sea mayor o igual
            //que el elemento que esta en el index izquiedo. Esta ligar es donde la clave
            //debera insertarse
			if (key < leavesList.get(mid).getKey() && key >= leavesList.get(mid - 1).getKey()) {
				index = mid;
				break;
			} // seguir con la busqueda binaria
			else if (key >= leavesList.get(mid).getKey()) {
				st = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		return index;
	}

	/**
	 * buscar el valor de una clave
	 *
	 * @param key
	 * @return lista de valores de una clave
	 */
	public List<Client> search(double key) {
		List<Client> searchValues = null;

		Node curr = this.root;
		// Recorrer el nodo externo que deberia tener la clave
		while (curr.getChildren().size() != 0) {
			curr = curr.getChildren().get(binarySearchWithinInternalNode(key, curr.getLeaves()));
		}
		List<Leaf> leavesList = curr.getLeaves();
        //hacer una busqueda lineal en este nodo para la clave
        //poner los parametros 'searchValues' solo si es exitoso
		for (int i = 0; i < leavesList.size(); i++) {
			if (key == leavesList.get(i).getKey()) {
				searchValues = leavesList.get(i).getClients();
			}
			if (key < leavesList.get(i).getKey()) {
				break;
			}
		}
		return searchValues;
	}
}