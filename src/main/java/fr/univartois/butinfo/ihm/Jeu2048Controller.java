package fr.univartois.butinfo.ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Le contrôleur du jeu 2048, qui gère les interactions utilisateur et l'affichage.
 */
public class Jeu2048Controller {

    @FXML
    private Button bas;

    @FXML
    private Button droite;

    @FXML
    private Button gauche;

    @FXML
    private Button haut;

    @FXML
    private Button rejouer;

    @FXML
    private Label score;

    @FXML
    private GridPane tileGrid;

    private Label[][] tileLabels = new Label[4][4];

    private Plateau2048 jeu = new Plateau2048();

	private Scene scene;


    @FXML
    void onClick(ActionEvent event) {
        Object source = event.getSource();
        Plateau2048.Direction direction = null;

        if (source == haut) direction = Plateau2048.Direction.HAUT;
        else if (source == bas) direction = Plateau2048.Direction.BAS;
        else if (source == gauche) direction = Plateau2048.Direction.GAUCHE;
        else if (source == droite) direction = Plateau2048.Direction.DROITE;
        else if (source == rejouer) {
            jeu = new Plateau2048();
            mettreAJourAffichage();
            score.setText("Votre score : 0");
            return;
        }

        if (direction != null && jeu.deplacer(direction)) {
            jeu.ajouterNouvelleTuile();
            mettreAJourAffichage();
            if (jeu.estTermine()) {
                score.setText("Partie terminée !");
            }
        }
    }

    private void mettreAJourAffichage() {
        int[][] grille = jeu.getGrille();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int valeur = grille[i][j];
                Label label = tileLabels[i][j];
                label.setText(valeur == 0 ? "" : String.valueOf(valeur));
                label.setStyle(String.format("""
                	    -fx-background-color: %s;
                	    -fx-font-size: 24px;
                	    -fx-font-weight: bold;
                	    -fx-alignment: center;
                	    -fx-border-color: #888;
                	    -fx-border-width: 2px;
                	""", couleurPour(valeur)));
            }
        }
        score.setText("Votre score : " + jeu.getScore());
    }

    private String couleurPour(int valeur) {
        return switch (valeur) {
            case 2 -> "#eee4da";
            case 4 -> "#ede0c8";
            case 8 -> "#f2b179";
            case 16 -> "#f59563";
            case 32 -> "#f67c5f";
            case 64 -> "#f65e3b";
            case 128 -> "#edcf72";
            case 256 -> "#edcc61";
            case 512 -> "#edc850";
            case 1024 -> "#edc53f";
            case 2048 -> "#edc22e";
            default -> "#cdc1b4";
        };
    }
    
    public void deplacerDepuisClavier(Plateau2048.Direction direction) {
        if (jeu.deplacer(direction)) {
            jeu.ajouterNouvelleTuile();
            mettreAJourAffichage();
            if (jeu.estTermine()) {
                score.setText("Partie terminée !");
            }
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> deplacerDepuisClavier(Plateau2048.Direction.HAUT);
                case DOWN -> deplacerDepuisClavier(Plateau2048.Direction.BAS);
                case LEFT -> deplacerDepuisClavier(Plateau2048.Direction.GAUCHE);
                case RIGHT -> deplacerDepuisClavier(Plateau2048.Direction.DROITE);
                case R -> {
                    jeu = new Plateau2048();
                    mettreAJourAffichage();
                    score.setText("Votre score : 0");
                }
            }
        });
    }
    
    
    @FXML
    void initialize() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Label label = new Label();
                label.setMinSize(100, 100);
                label.setStyle("""
                    -fx-border-color: #bbb;
                    -fx-alignment: center;
                    -fx-font-size: 24px;
                    -fx-font-weight: bold;
                    -fx-background-color: #cdc1b4;
                """);
                label.setWrapText(true);
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                tileGrid.add(label, j, i);
                tileLabels[i][j] = label;
            }
        }

        // Empêche les boutons de capter le focus clavier
        haut.setFocusTraversable(false);
        bas.setFocusTraversable(false);
        gauche.setFocusTraversable(false);
        droite.setFocusTraversable(false);
        rejouer.setFocusTraversable(false);

        tileGrid.requestFocus();
        mettreAJourAffichage();
    }


}
