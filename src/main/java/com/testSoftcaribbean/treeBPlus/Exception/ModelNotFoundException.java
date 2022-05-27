package com.testSoftcaribbean.treeBPlus.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModelNotFoundException extends RuntimeException {
    
    public ModelNotFoundException(String message){
        //** Enviamos el mensaje de la excepcion a la clase padre */
        super(message);
    }
}
