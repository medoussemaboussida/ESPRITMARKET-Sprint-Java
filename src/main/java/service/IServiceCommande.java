package service;

import entities.Panier;

public interface IServiceCommande <T>{
    void ajouterCommande(Panier panier);
}
