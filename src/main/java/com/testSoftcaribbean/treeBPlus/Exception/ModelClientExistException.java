package com.testSoftcaribbean.treeBPlus.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ModelClientExistException extends RuntimeException {
        public ModelClientExistException(String message){
        //** Enviamos el mensaje de la excepcion a la clase padre */
        super(message);
    }
}
