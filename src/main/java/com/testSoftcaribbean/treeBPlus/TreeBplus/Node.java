package com.testSoftcaribbean.treeBPlus.TreeBplus;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

@Data
public class Node {
    /** lista de pares de clave-valor de un nodo */
	private List<Leaf> leaves;

	/** Hijos solo de los nodos internos */
	private List<Node> children;

	/** nodo a la izquierda , solo para nodos externos. */
	private Node prev;

	/** Nodo siguiente. Solo para nodos externos */
	private Node next;

	/** padre del nodo. NULL si es raiz */
	private Node parent;

	public Node() {
		this.leaves = new ArrayList<>();
		this.children = new ArrayList<>();
		this.prev = null;
		this.next = null;
	}

	/**
	 * insertar lista de claves
	 *
	 * @param leaves
	 */
	public void setLeaves(List<Leaf> leaves) {
		Iterator<Leaf> iter = leaves.iterator();
		while (iter.hasNext()) {
			this.leaves.add(iter.next());
		}
	}

	@Override
	public String toString() {
		return "leaves =" + leaves.toString();
	}
}