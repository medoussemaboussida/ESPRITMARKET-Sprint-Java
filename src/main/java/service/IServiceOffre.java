package service;

import entities.Offre;
import javafx.collections.ObservableList;

public interface IServiceOffre  <T>{
    public ObservableList<T> readOffre();
}
