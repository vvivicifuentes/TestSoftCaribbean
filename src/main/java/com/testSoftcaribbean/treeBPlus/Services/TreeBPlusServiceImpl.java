package com.testSoftcaribbean.treeBPlus.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testSoftcaribbean.treeBPlus.Entity.Client;
import com.testSoftcaribbean.treeBPlus.TreeBplus.BTreePlus;

@Service
public class TreeBPlusServiceImpl implements TreeBPlusService{

    /**
     * definimos el grado que va a tener el arbol b
     * y cargamos los datos de la tabla en memoria
     */
    @Override
    public BTreePlus initialize(int degree, List<Client> listClient) {
        BTreePlus tree = new BTreePlus();
        tree.initialize(degree);
        //por cada cliente de la lista cargar al arbol
        listClient.forEach(client -> tree.insert(client.getId(), client));
        
        return tree;
    }
    
}

