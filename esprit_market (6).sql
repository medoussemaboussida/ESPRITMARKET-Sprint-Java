-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 04 mars 2024 à 14:03
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `esprit_market`
--

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `idCategorie` int(11) NOT NULL,
  `nomCategorie` varchar(255) NOT NULL,
  `imageCategorie` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `categorie`
--

INSERT INTO `categorie` (`idCategorie`, `nomCategorie`, `imageCategorie`) VALUES
(11, 'Boissons', 'boissons.png'),
(14, 'Pattes', 'pattes.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `codepromo`
--

CREATE TABLE `codepromo` (
  `idCode` int(11) NOT NULL,
  `reductionAssocie` int(11) NOT NULL,
  `code` varchar(255) NOT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `codepromo`
--

INSERT INTO `codepromo` (`idCode`, `reductionAssocie`, `code`, `dateDebut`, `dateFin`) VALUES
(1, 1000, 'f5tt', '2024-03-04', '2024-04-01');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `idCommande` int(11) NOT NULL,
  `idPanier` int(11) NOT NULL,
  `dateCommande` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`idCommande`, `idPanier`, `dateCommande`) VALUES
(5, 3, '2024-02-24 14:27:25'),
(6, 3, '2024-02-24 14:31:14'),
(7, 3, '2024-02-24 14:34:07'),
(8, 3, '2024-02-24 14:36:15'),
(9, 3, '2024-02-24 14:37:12'),
(10, 3, '2024-02-24 14:38:56'),
(11, 3, '2024-02-25 11:34:18'),
(12, 3, '2024-02-27 11:13:47'),
(13, 3, '2024-02-28 20:49:53'),
(14, 3, '2024-03-04 13:22:32');

-- --------------------------------------------------------

--
-- Structure de la table `commentaire`
--

CREATE TABLE `commentaire` (
  `idCommentaire` int(11) NOT NULL,
  `descriptionCommentaire` varchar(255) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idPublication` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commentaire`
--

INSERT INTO `commentaire` (`idCommentaire`, `descriptionCommentaire`, `idUser`, `idPublication`) VALUES
(2, 'nice', 2, 1),
(4, 'behi behi', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `demandedons`
--

CREATE TABLE `demandedons` (
  `idDemande` int(11) NOT NULL,
  `idUtilisateur` int(11) DEFAULT NULL,
  `contenu` text DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `datePublication` timestamp NOT NULL DEFAULT current_timestamp(),
  `idDons` int(11) DEFAULT NULL,
  `nbpoints` int(11) DEFAULT NULL,
  `nomuser` varchar(255) DEFAULT NULL,
  `prenomuser` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `demandedons`
--

INSERT INTO `demandedons` (`idDemande`, `idUtilisateur`, `contenu`, `image`, `datePublication`, `idDons`, `nbpoints`, `nomuser`, `prenomuser`) VALUES
(2, 1, 'bonjour ', NULL, '2024-03-02 23:48:32', 3, 123, 'Boussida', 'Med Oussema');

-- --------------------------------------------------------

--
-- Structure de la table `dons`
--

CREATE TABLE `dons` (
  `idDons` int(11) NOT NULL,
  `idUser` int(11) DEFAULT NULL,
  `nbpoints` int(11) DEFAULT NULL,
  `date_ajout` timestamp NOT NULL DEFAULT current_timestamp(),
  `etatStatutDons` varchar(255) DEFAULT NULL,
  `idDemande` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `dons`
--

INSERT INTO `dons` (`idDons`, `idUser`, `nbpoints`, `date_ajout`, `etatStatutDons`, `idDemande`) VALUES
(1, 1, 5, '2024-03-02 22:35:09', 'En cours', NULL),
(2, 1, 1, '2024-03-02 23:46:35', 'En cours', NULL),
(3, 1, 1, '2024-03-03 00:02:11', NULL, NULL),
(4, 2, 2, '2024-03-03 17:31:36', 'En cours', NULL),
(5, 1, 4, '2024-03-03 17:49:16', 'En cours', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `evenement`
--

CREATE TABLE `evenement` (
  `id_ev` int(11) NOT NULL,
  `nom_ev` varchar(255) NOT NULL,
  `type_ev` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `image_ev` varchar(255) NOT NULL,
  `description_ev` varchar(255) NOT NULL,
  `code_participant` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `evenement`
--

INSERT INTO `evenement` (`id_ev`, `nom_ev`, `type_ev`, `date`, `image_ev`, `description_ev`, `code_participant`) VALUES
(1, 'jawEljaw', 'jaw', '2024-03-08', 'C:\\\\xampp\\\\htdocs\\\\244.jpg', 'barcha jaw ya weldi', 46);

-- --------------------------------------------------------

--
-- Structure de la table `offre`
--

CREATE TABLE `offre` (
  `idOffre` int(11) NOT NULL,
  `descriptionOffre` varchar(255) NOT NULL,
  `nomOffre` varchar(255) NOT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date NOT NULL,
  `imageOffre` varchar(255) NOT NULL,
  `reduction` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `offre`
--

INSERT INTO `offre` (`idOffre`, `descriptionOffre`, `nomOffre`, `dateDebut`, `dateFin`, `imageOffre`, `reduction`) VALUES
(2, 'offreHbel', '50% free for all products', '2024-03-05', '2024-04-01', 'boissons.png', 50);

-- --------------------------------------------------------

--
-- Structure de la table `offreproduit`
--

CREATE TABLE `offreproduit` (
  `id` int(11) NOT NULL,
  `idOffre` int(11) NOT NULL,
  `idProduit` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `offreproduit`
--

INSERT INTO `offreproduit` (`id`, `idOffre`, `idProduit`) VALUES
(1, 2, 16),
(2, 2, 17),
(3, 2, 18);

-- --------------------------------------------------------

--
-- Structure de la table `panier`
--

CREATE TABLE `panier` (
  `idPanier` int(11) NOT NULL,
  `idUser` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `panier`
--

INSERT INTO `panier` (`idPanier`, `idUser`) VALUES
(3, 1),
(4, 2);

-- --------------------------------------------------------

--
-- Structure de la table `participant`
--

CREATE TABLE `participant` (
  `id_participant` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_evenement` int(11) NOT NULL,
  `date_part` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `participant`
--

INSERT INTO `participant` (`id_participant`, `id_user`, `id_evenement`, `date_part`) VALUES
(1, 1, 1, '2024-03-03'),
(2, 1, 1, '2024-03-03'),
(3, 1, 1, '2024-03-03'),
(4, 2, 1, '2024-03-03');

-- --------------------------------------------------------

--
-- Structure de la table `password_reset_token`
--

CREATE TABLE `password_reset_token` (
  `token` varchar(255) NOT NULL,
  `idUser` int(255) NOT NULL,
  `Timestamp` timestamp(5) NOT NULL DEFAULT current_timestamp(5) ON UPDATE current_timestamp(5),
  `emailUser` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `idProduit` int(11) NOT NULL,
  `categorie_id` int(11) NOT NULL,
  `nomProduit` varchar(255) NOT NULL,
  `quantite` int(11) NOT NULL,
  `prix` float NOT NULL,
  `imageProduit` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`idProduit`, `categorie_id`, `nomProduit`, `quantite`, `prix`, `imageProduit`) VALUES
(16, 11, 'Coca Cola', 46, 3.5, 'CocaCola1L-1.jpg'),
(17, 11, 'Delio Pomme', 98, 0.7, 'delio-pomme.png'),
(18, 11, 'Melliti', 197, 0.5, 'melliti.jpg'),
(19, 11, 'Jus Peche', 200, 3, 'jusDelice.png'),
(20, 11, 'Stika Safia', 200, 3.5, 'eau6.jpg'),
(23, 14, 'Ma9rouna Fell 2', 200, 1.2, 'fell-2-flottant.png');

-- --------------------------------------------------------

--
-- Structure de la table `produitcart`
--

CREATE TABLE `produitcart` (
  `idPanierProduit` int(11) NOT NULL,
  `idPanier` int(11) NOT NULL,
  `idProduit` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produitcart`
--

INSERT INTO `produitcart` (`idPanierProduit`, `idPanier`, `idProduit`) VALUES
(32, 4, 16),
(34, 3, 16),
(36, 3, 17),
(37, 3, 18),
(38, 3, 17);

-- --------------------------------------------------------

--
-- Structure de la table `publication`
--

CREATE TABLE `publication` (
  `idPublication` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `datePublication` date NOT NULL,
  `imagePublication` varchar(255) NOT NULL,
  `titrePublication` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `publication`
--

INSERT INTO `publication` (`idPublication`, `description`, `datePublication`, `imagePublication`, `titrePublication`) VALUES
(1, 'produit lkol freeee !!', '2024-02-28', 'boissons.png', 'jdid el jdid'),
(2, ' tfarchika mouch normal . ay ay ay', '2024-03-03', 'boissons.png', 'summer product .'),
(3, 'el jaw el jaw', '2024-03-03', 'boissons.png', 'summer product reminder ');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `idUser` int(11) NOT NULL,
  `nomUser` varchar(255) NOT NULL,
  `prenomUser` varchar(255) NOT NULL,
  `emailUser` varchar(255) NOT NULL,
  `mdp` varchar(255) NOT NULL,
  `nbPoints` int(255) NOT NULL,
  `numTel` int(8) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`idUser`, `nomUser`, `prenomUser`, `emailUser`, `mdp`, `nbPoints`, `numTel`, `role`) VALUES
(1, 'Boussida', 'Med Oussema', 'oussemaboussida@gmail.com', 'oussema2002', 46, 54007403, 'Client'),
(2, 'benMahmoud', 'Ghassen', 'medoussemaboussida@gmail.com', '123', 18, 54788412, 'admin'),
(4, 'dhahbi', 'yassine', 'yass@gmail.com', '$2a$10$tFNnIDWE9iXcrblx7yMxseGzjYz5Xd81J32kRxO6M3TOuoNIGIo7.', 0, 54008451, 'Client');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`idCategorie`);

--
-- Index pour la table `codepromo`
--
ALTER TABLE `codepromo`
  ADD PRIMARY KEY (`idCode`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`idCommande`),
  ADD KEY `idPanier` (`idPanier`);

--
-- Index pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD PRIMARY KEY (`idCommentaire`),
  ADD KEY `idUser` (`idUser`),
  ADD KEY `idPublication` (`idPublication`);

--
-- Index pour la table `demandedons`
--
ALTER TABLE `demandedons`
  ADD PRIMARY KEY (`idDemande`),
  ADD KEY `idUtilisateur` (`idUtilisateur`),
  ADD KEY `idDons` (`idDons`);

--
-- Index pour la table `dons`
--
ALTER TABLE `dons`
  ADD PRIMARY KEY (`idDons`),
  ADD KEY `idUser` (`idUser`),
  ADD KEY `idDemande` (`idDemande`);

--
-- Index pour la table `evenement`
--
ALTER TABLE `evenement`
  ADD PRIMARY KEY (`id_ev`);

--
-- Index pour la table `offre`
--
ALTER TABLE `offre`
  ADD PRIMARY KEY (`idOffre`);

--
-- Index pour la table `offreproduit`
--
ALTER TABLE `offreproduit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idOffre` (`idOffre`),
  ADD KEY `idProduit` (`idProduit`);

--
-- Index pour la table `panier`
--
ALTER TABLE `panier`
  ADD PRIMARY KEY (`idPanier`),
  ADD KEY `idUser` (`idUser`);

--
-- Index pour la table `participant`
--
ALTER TABLE `participant`
  ADD PRIMARY KEY (`id_participant`),
  ADD KEY `id_evenement` (`id_evenement`);

--
-- Index pour la table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD KEY `idUser` (`idUser`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`idProduit`),
  ADD KEY `categorie_id` (`categorie_id`);

--
-- Index pour la table `produitcart`
--
ALTER TABLE `produitcart`
  ADD PRIMARY KEY (`idPanierProduit`),
  ADD KEY `idPanier` (`idPanier`),
  ADD KEY `idProduit` (`idProduit`);

--
-- Index pour la table `publication`
--
ALTER TABLE `publication`
  ADD PRIMARY KEY (`idPublication`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `idCategorie` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT pour la table `codepromo`
--
ALTER TABLE `codepromo`
  MODIFY `idCode` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
  MODIFY `idCommande` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT pour la table `commentaire`
--
ALTER TABLE `commentaire`
  MODIFY `idCommentaire` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `demandedons`
--
ALTER TABLE `demandedons`
  MODIFY `idDemande` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `dons`
--
ALTER TABLE `dons`
  MODIFY `idDons` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `evenement`
--
ALTER TABLE `evenement`
  MODIFY `id_ev` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `offre`
--
ALTER TABLE `offre`
  MODIFY `idOffre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `offreproduit`
--
ALTER TABLE `offreproduit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `panier`
--
ALTER TABLE `panier`
  MODIFY `idPanier` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `participant`
--
ALTER TABLE `participant`
  MODIFY `id_participant` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `idProduit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT pour la table `produitcart`
--
ALTER TABLE `produitcart`
  MODIFY `idPanierProduit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT pour la table `publication`
--
ALTER TABLE `publication`
  MODIFY `idPublication` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `commande_ibfk_1` FOREIGN KEY (`idPanier`) REFERENCES `panier` (`idPanier`);

--
-- Contraintes pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD CONSTRAINT `commentaire_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `utilisateur` (`idUser`),
  ADD CONSTRAINT `commentaire_ibfk_2` FOREIGN KEY (`idPublication`) REFERENCES `publication` (`idPublication`);

--
-- Contraintes pour la table `demandedons`
--
ALTER TABLE `demandedons`
  ADD CONSTRAINT `demandedons_ibfk_1` FOREIGN KEY (`idUtilisateur`) REFERENCES `utilisateur` (`idUser`),
  ADD CONSTRAINT `demandedons_ibfk_2` FOREIGN KEY (`idDons`) REFERENCES `dons` (`idDons`);

--
-- Contraintes pour la table `dons`
--
ALTER TABLE `dons`
  ADD CONSTRAINT `dons_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `utilisateur` (`idUser`),
  ADD CONSTRAINT `dons_ibfk_2` FOREIGN KEY (`idDemande`) REFERENCES `demandedons` (`idDemande`);

--
-- Contraintes pour la table `offreproduit`
--
ALTER TABLE `offreproduit`
  ADD CONSTRAINT `offreproduit_ibfk_1` FOREIGN KEY (`idOffre`) REFERENCES `offre` (`idOffre`),
  ADD CONSTRAINT `offreproduit_ibfk_2` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`idProduit`);

--
-- Contraintes pour la table `panier`
--
ALTER TABLE `panier`
  ADD CONSTRAINT `panier_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `utilisateur` (`idUser`);

--
-- Contraintes pour la table `participant`
--
ALTER TABLE `participant`
  ADD CONSTRAINT `participant_ibfk_1` FOREIGN KEY (`id_evenement`) REFERENCES `evenement` (`id_ev`);

--
-- Contraintes pour la table `produit`
--
ALTER TABLE `produit`
  ADD CONSTRAINT `produit_ibfk_1` FOREIGN KEY (`categorie_id`) REFERENCES `categorie` (`idCategorie`);

--
-- Contraintes pour la table `produitcart`
--
ALTER TABLE `produitcart`
  ADD CONSTRAINT `produitcart_ibfk_1` FOREIGN KEY (`idPanier`) REFERENCES `panier` (`idPanier`),
  ADD CONSTRAINT `produitcart_ibfk_2` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`idProduit`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
