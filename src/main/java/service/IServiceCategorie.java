package service;

import java.util.List;

public interface IServiceCategorie <T> {
    void addCategorie (T t);
    List<T> readCategorie();
    void deleteCategorie(int id);
    void modifyCategorie (T t);

}
