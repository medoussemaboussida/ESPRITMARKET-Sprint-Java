package service;

import javafx.collections.ObservableList;

import java.util.List;

public interface IServiceProduit <T>{
    void addProduit (T t);
    ObservableList<T> readProduit();
    void modifyProduit(T t);
    void deleteProduit(int  idProduit);

}
