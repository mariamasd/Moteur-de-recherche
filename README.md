# Moteur de recherche offline - Clone egrep

Ce projet est un clone simplifié de la commande Linux `egrep`.  
Il permet de rechercher des motifs dans des fichiers texte à l'aide d'expressions régulières (RegEx) respectant partiellement la norme **ERE**.

## Fonctionnalités

- Support des motifs avec :
  - Parenthèses `()`
  - Alternative `|`
  - Concaténation
  - Opérateur étoile `*`
  - Caractère universel `.`
  - Lettres ASCII
- Recherche ligne par ligne dans des fichiers texte.
- Interface graphique simple pour sélectionner le fichier et entrer le motif.
- Utilisation d’automates finis :
  - **NFA** (Automate fini non-déterministe)
  - **DFA** (Automate fini déterministe)
- Optimisation pour motifs simples avec l’algorithme **KMP**.

## Structure du projet
src/
├── app/ # Interface graphique
├── automaton/ # NFA, DFA, State
├── engine/ # Gestion de la recherche dans le fichier
├── regex/ # Parser et représentation de l'arbre de RegEx
├── KMP/ # Algorithme Knuth-Morris-Pratt
├── Main.java # Point d’entrée du programme

## Compilation

Ouvrir un terminal dans le dossier `src` :

```bash
cd src
javac Main.java app/*.java automaton/*.java engine/*.java regex/*.java KMP/*.java
java Main
