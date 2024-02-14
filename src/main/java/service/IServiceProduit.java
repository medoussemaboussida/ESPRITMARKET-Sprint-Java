package service;

import java.util.List;

public interface IServiceProduit <T>{
    void addProduit (T t);
    List<T> readProduit();
    void modifyProduit(T t);
    void deleteProduit(int  idProduit);

}
