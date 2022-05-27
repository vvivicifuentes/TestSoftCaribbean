package com.testSoftcaribbean.treeBPlus.TreeBplus;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import com.testSoftcaribbean.treeBPlus.Entity.Client;

@Data
public class Leaf {
    /** clave. */
	double key;

	/**Lista de valores por clave. solo para nodos externos*/
	List<Client> clients;


	public Leaf(double key, Client client) {
		this.key = key;
		if (null == this.clients) {
			clients = new ArrayList<>();
		}
		this.clients.add(client);
	}
	
	/**
	 * inicializar la clave
	 *
	 * @param key
	 */
	public Leaf(double key) {
		this.key = key;
		this.clients = new ArrayList<>();
	}

	public String toString() {
		return "Key [key=" + key + ", clients=" + clients + "]";
	}
}
