/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.exception;

/**
 *
 * @author Lenovo T530
 */
public class ServidorException extends RuntimeException{
    
    public ServidorException(String message){
        super(message);
    }
    
    public ServidorException(String message, Throwable cause){
        super(message,cause);
    }
    
}
