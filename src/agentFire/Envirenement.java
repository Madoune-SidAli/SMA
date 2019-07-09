package agentFire;

import java.util.Random;

public class Envirenement {

    private static final int N = 10;
    private static Integer[][] grille = new Integer[N][N];
    private static String[][] grille_organ_name = new String[N][N];
    private static Position position_secouriste;
    private static boolean GameOver = false;
    public static int fireNbr = 0;

    public Envirenement() {
        initialize();
    }

    public static void initialize() {
        GameOver = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grille_organ_name[i][j] = "organ" + i + j;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grille[i][j] = 0;
            }
        }

        //choix aleatoire de l'organisme ataint par le virus      
        Random randomno = new Random();
        int x = randomno.nextInt(10);
        int y = randomno.nextInt(10);
        fireNbr++;
        grille[x][y] = 1;

        // positon aleatoire de l'agent 
        x = randomno.nextInt(10);
        y = randomno.nextInt(10);
        position_secouriste = new Position(x, y);

    }

    public static synchronized Position percept_getposition() {
        return position_secouriste;
    }

    public static Position percept_getOrganposition(String organ_name) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grille_organ_name[i][j].equals(organ_name)) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public static boolean isGameOver() {
        int nombreorganeinfict = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grille[i][j] == 1 ) {
                    nombreorganeinfict++;
                }
            }
        }

        if (nombreorganeinfict == 0) {
            GameOver = true;
            Gui.afficheGameOver();
        }
        return GameOver;
    }

    //tkhaabrna l'etat ta3 la case
    public static Integer perceptOrganismeEtat(Position position) {
        return grille[position.getX()][position.getY()];
    }

    //Tbadel l'etat 
    public static void actionchangeOrganismeEtat(Position position, int etat) {
        if (etat == 1) {
            if (grille[position.getX()][position.getY()] == 0) {
                grille[position.getX()][position.getY()] = etat;
                Gui.changecolorEtata(position, etat);
            }
        } else {
            grille[position.getX()][position.getY()] = etat;
            Gui.changecolorEtata(position, etat);
        }
    }

    //deplacer l
    public static synchronized void secouriste_deplace(Position position) {
        position_secouriste = position;
        Gui.drawagent(position);
    }

//north, east, south, and west
//northeast (NE), southeast (SE), southwest (SW), and northwest (NW)
    public static boolean percept_caninfectnorth(Position position) {
        if ((position.getY() - 1) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean percept_caninfectnortheast(Position position) {
        return percept_caninfectnorth(position) && percept_caninfecteast(position);
    }

    public static boolean percept_caninfecteast(Position position) {
        if ((position.getX() + 1) < N) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean percept_caninfectsoutheast(Position position) {
        return percept_caninfectsouth(position) && percept_caninfecteast(position);
    }

    public static boolean percept_caninfectsouth(Position position) {
        if ((position.getY() + 1) < N) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean percept_caninfectsouthwest(Position position) {
        return percept_caninfectsouth(position) && percept_caninfectwest(position);
    }

    public static boolean percept_caninfectwest(Position position) {
        if ((position.getX() - 1) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean percept_caninfectnourthwest(Position position) {
        return percept_caninfectnorth(position) && percept_caninfectwest(position);
    }

}
