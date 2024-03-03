package service;

import entities.user;

import java.util.List;

public interface IServiceUser <T>{
    T readById(int idUser);


    List<user> getAllUsers();
}
