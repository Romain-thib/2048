package fr.univartois.butinfo.ihm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plateau2048 {

    public enum Direction {
        HAUT, BAS, GAUCHE, DROITE
    }

    private int[][] grille = new int[4][4];
    private int score = 0;
    private Random random = new Random();

    public Plateau2048() {
        ajouterNouvelleTuile();
        ajouterNouvelleTuile();
    }

    public int[][] getGrille() {
        return grille;
    }

    public int getScore() {
        return score;
    }

    public void ajouterNouvelleTuile() {
        List<int[]> casesVides = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grille[i][j] == 0)
                    casesVides.add(new int[]{i, j});

        if (!casesVides.isEmpty()) {
            int indexAleatoire = random.nextInt(casesVides.size());
            int[] caseChoisie = casesVides.get(indexAleatoire);

            double tirage = random.nextDouble();
            int valeur = 2;
            if (tirage >= 0.9) {
                valeur = 4;
            }

            grille[caseChoisie[0]][caseChoisie[1]] = valeur;
        }
    }

    public boolean deplacer(Direction direction) {
        boolean mouvementEffectue = false;
        for (int i = 0; i < 4; i++) {
            int[] ligne = switch (direction) {
                case HAUT -> getColonne(i);
                case BAS -> reverse(getColonne(i));
                case GAUCHE -> grille[i];
                case DROITE -> reverse(grille[i]);
            };

            int[] fusionnee = fusionnerLigne(ligne);
            if (!equals(ligne, fusionnee)) {
                mouvementEffectue = true;
                switch (direction) {
                    case HAUT -> setColonne(i, fusionnee);
                    case BAS -> setColonne(i, reverse(fusionnee));
                    case GAUCHE -> grille[i] = fusionnee;
                    case DROITE -> grille[i] = reverse(fusionnee);
                }
            }
        }
        return mouvementEffectue;
    }

    private int[] fusionnerLigne(int[] ligne) {
        int[] resultat = new int[4];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            if (ligne[i] != 0) {
                if (index > 0 && resultat[index - 1] == ligne[i]) {
                    resultat[index - 1] *= 2;
                    score += resultat[index - 1];
                } else {
                    resultat[index++] = ligne[i];
                }
            }
        }
        return resultat;
    }

    private int[] getColonne(int col) {
        int[] colonne = new int[4];
        for (int i = 0; i < 4; i++) {
            colonne[i] = grille[i][col];
        }
        return colonne;
    }

    private void setColonne(int col, int[] values) {
        for (int i = 0; i < 4; i++) {
            grille[i][col] = values[i];
        }
    }

    private int[] reverse(int[] array) {
        int[] reversed = new int[4];
        for (int i = 0; i < 4; i++) {
            reversed[i] = array[3 - i];
        }
        return reversed;
    }

    private boolean equals(int[] a, int[] b) {
        for (int i = 0; i < 4; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    public boolean estTermine() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grille[i][j] == 0 ||
                    (i < 3 && grille[i][j] == grille[i + 1][j]) ||
                    (j < 3 && grille[i][j] == grille[i][j + 1]))
                    return false;
        return true;
    }
}
