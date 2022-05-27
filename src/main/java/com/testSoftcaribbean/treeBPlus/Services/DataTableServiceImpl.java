package com.testSoftcaribbean.treeBPlus.Services;


import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import org.springframework.stereotype.Service;

import com.testSoftcaribbean.treeBPlus.Entity.Client;

@Service
public class DataTableServiceImpl implements DataTableService {

    /**
     * traer los datos almacenados
     * 
     * @param nombre del archivo
     * @return lista de los clientes
     */
    @Override
    public List<Client> getDataTable(String table) {
        List<Client> listClients = new ArrayList<>();
        // se traen los registros del archivo
        try (Stream<String> stream = Files.lines(Paths.get(table + ".txt"))) {
            // recorremos las linas del archivo
            stream.forEach(line -> {
                // se divide el registro y se carga un una lista
                String[] input = line.split(",");
                // se ignora el registro si el la primer fila o es null
                if (!input[0].equals("id") && input[0] != null) {
                    Client client = new Client();
                    client.setId(Integer.parseInt(input[0]));
                    client.setFirstName(input[1]);
                    client.setLastName(input[2]);
                    client.setDirection(input[3]);
                    client.setMovil(input[4]);
                    client.setAge(Integer.parseInt(input[5]));
                    client.setDateBirth(input[6]);

                    // agregamos cliente a la lista
                    listClients.add(client);
                }

            });
            stream.close();
        } catch (IOException e) {
            System.out.println("An error ocurred.");
            e.printStackTrace();
        }
        return listClients;
    }

    @Override
    public boolean insertDataTable(String table, Client client) {

        FileWriter file;
        try {
            file = new FileWriter(table + ".txt",true);
            file.write("\r\n"+client.toStringTable());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}