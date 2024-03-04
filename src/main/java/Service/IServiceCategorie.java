package Service;

import entities.Categorie;
import javafx.collections.ObservableList;

public interface IServiceCategorie <T> {


    ObservableList<Categorie> readCategorie();

    ObservableList<Categorie> sortCategorieAsc();

    ObservableList<Categorie> sortCategorieDesc();
}
