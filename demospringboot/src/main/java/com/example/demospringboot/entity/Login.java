package com.example.demospringboot.entity;

public interface Login {
    boolean signIn(String username, String password);
    void signOut();
    boolean resetPassword(String newPassword);
    
    default boolean signUp() {
        return true;
    }
}