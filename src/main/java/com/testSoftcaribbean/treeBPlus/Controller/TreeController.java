package com.testSoftcaribbean.treeBPlus.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.testSoftcaribbean.treeBPlus.Entity.Client;
import com.testSoftcaribbean.treeBPlus.Entity.Response;
import com.testSoftcaribbean.treeBPlus.Exception.ModelClientExistException;
import com.testSoftcaribbean.treeBPlus.Exception.ModelNotFoundException;
import com.testSoftcaribbean.treeBPlus.Services.DataTableService;
import com.testSoftcaribbean.treeBPlus.Services.TreeBPlusService;
import com.testSoftcaribbean.treeBPlus.TreeBplus.BTreePlus;

@RestController
@RequestMapping(value = "treeBPlus")
public class TreeController {
    BTreePlus tree;

    @Autowired
    DataTableService tableService;
    @Autowired
    TreeBPlusService treeService;
    private static final int DEGREE = 5;

    /**
     * si no esta cargado el arbol en memoria se inicializa de lo contrario devuelve
     * el arbol ya inicializado
     */
    private void initializeTree() {
        if (null == tree ) {
            // se trae los clientes del archivo
            List<Client> listClients = tableService.getDataTable("client");
            // se cargan los clientes al arbol
            this.tree = treeService.initialize(DEGREE, listClients);
            System.out.println(this.tree.printTree());
        }
    }

    /**
     * crear clientes en el archivo y en el arbol
     * 
     * @param client
     * @return cliente
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<Response> createClient(@Valid @RequestBody Client client) {
        this.initializeTree();
        // se busca el cliente por id para verificar si ya existe
        List<Client> existClient = this.tree.search(client.getId());
        if(null != existClient){
            throw new ModelClientExistException("The client already exists.");
        }
        // ingresar el nuevo cliente al arbol
        this.tree.insert(client.getId(), client);
        // grabar nuevo cliente al archivo
        tableService.insertDataTable("client", client);
       
        return new ResponseEntity<>(this.tree.printTree(), HttpStatus.OK);       
    }

    /**
     * buscar dentro del arbol un cliente
     * 
     * @param id
     * @return lista de clientes encontrados
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/{id}")
    public ResponseEntity<List<Client>> getCustomer(@PathVariable("id") int id) {
        this.initializeTree();
        // se busca el cliente por id
        List<Client> listClient = this.tree.search(id);

        // Manejo de excepciones personalizadas 404 Not Found
        if (null == listClient || listClient.size() == 0) {
            throw new ModelNotFoundException("Client not found");
        }
        return new ResponseEntity<>(listClient, HttpStatus.OK);
    }

    /**
     * se capturan todas las respuestas malas y se agrega el error por validación
     * 
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}