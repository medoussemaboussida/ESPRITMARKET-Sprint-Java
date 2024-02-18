package service;

import entities.Panier;
import entities.Produit;

public interface IServicePanierProduit <T>{
    void ajouterProduitAuPanier(Panier panier, int idProduit);
    }
