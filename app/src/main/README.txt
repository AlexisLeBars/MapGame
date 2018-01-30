***************************************************************
*															  *
*							READ ME							  *
*															  *
***************************************************************

Informations Générales :

Projet PPM Android 2017-2018
Application Guess My Place
Auteurs : Alexis Le Bars, Aurélien Lamoureux

********************* Options implémentées ********************

- Gestion de la rotation
- Persistance durable
- Possibilité de trier les scores sur la difficulté, les scores,
  les dates ainsi que le mode de jeu
- Partage de scores
- Mode de jeu par recherche de pays
- Mode de jeu inversée

***************************************************************

Contenu de l'application :

L'application compte 3 Activitiés principales :
- MainActivity, gérant la page d'acceuil et permettant la sélection du mode et de la difficulté de jeu
- GameActivity, gérant l'affichage et le déroulement du jeu
- ScoreView, gérant l'affichage du tableau des scores avec l'aide de ScoresAdapter s'occupant de remplir les données du tableau

Elle compte aussi 3 Classes qui s'occupent de la base de données contenant les informations concernant les scores :
- DataBaseHelper, contenant les informations structurelles de la base qui a une table et 5 colonnes (id, level, score, date, mode)
- Scores, décrivant les informations d'un score (id, level, score, date, mode)
- ScoresDataSource, manipulant la base de données afin d'ajouter, rechercher, filtrer les informations des scores

Elle dispose enfin d'une dernière Classe, DBSpot, contenant les informations sur les lieux à rechercher (coordonnées GPS, difficulté, lieu déjà vu).


Description de l'application :

L'utilisateur démarre sur la page d'acceuil et est invité à sélectionner le mode et la difficulté du jeu.
Les différents mode de jeu sont définis ainsi :
- Normal, jeu classique où l'on doit essayer d'obtenir un score minimal en se situant le plus proche possible du lieux recherché sur la streetView.
  Le score est le cumul de toute les différences de distance
- Inverse, version inversée du mode Normal où l'on doit essayer d'obtenir un score maximal en se situant le plus loin possible du lieux recherché sur la streetView.
  Le score est le cumul de toute les différences de distance
- Country, version du jeu où l'on doit essayer d'obtenir un score maximal en se situant sur le pays du lieux recherché sur la streetView.
  Le score est le cumul de toute les bonnes réponses (les noms de rue sont cachés)
Et la difficulté :
- Easy, tout est autorisé et les lieux facilement reconnaissables
- Medium, tout est autorisé et les lieux assez difficilement reconnaissables
- Hard, les déplacements sur la streetView sont désactivés mais pas ceux de la caméra, les noms de rue cachés et les lieux assez difficilement reconnaissables

Le jeu se lance et l'utilisateur peut donc marquer un lieu proche de celui recherché et ainsi de suite jusqu'a à la fin de la partie où le score est calculé et mis en base.
Le joueur est ensuite redirigé vers le tableau des scores dont il peut trier les informations en cliquant sur les entêtes.
Enfin l'utilisateur peut partager un score en cliquant sur la ligne correspondante du tableau.


Difficultés rencontrées :
- Nous avons tout d'abord tenté de calculer nous même les distances à l'aide de la formule des distances orthodromiques mais les résultats n'étant pas convainquant nous avons utilisé les fonctions de google Maps
- N'autoriser qu'un seul click sur la carte entre chaque recherche


