package com.example.tpo4.exceptions;

public class OsobaNotFoundException extends Exception{
    public OsobaNotFoundException(int id) {
        super("Osoba with id " + id + " not found");
    }
}
