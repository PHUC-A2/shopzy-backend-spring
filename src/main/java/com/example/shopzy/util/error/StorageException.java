package com.example.shopzy.util.error;

public class StorageException extends Exception { // id không hợp lệ

    // constructor
    public StorageException(String message) {
        super(message);
    }
}