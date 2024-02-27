package Service;

import entities.Categorie;
import javafx.collections.ObservableList;

public interface IServiceProduit<T> {
    public ObservableList<T> readProduit();


    ObservableList<Categorie> readCategorie();
}
