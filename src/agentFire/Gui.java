package agentFire;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Gui extends Application implements Runnable {

    static jade.core.Runtime rt;
    static AgentContainer mc;
    private static final Circle secouriste = createCircle(Color.BLUE);
    private static Rectangle[][] Organe = createOrganisme();
    private static final Group group = new Group();
    private Thread thread;
    private static final int N = 10;
    private static Stage stage;

    public static void main(String[] args) {
        Gui application = new Gui();
        application.start();
    }

    @Override
    public void start(Stage stage1) throws Exception {
        stage = stage1;
        for (int i = 1; i < N; i++) {
            Line seesaw = new Line(i * 60, 0, i * 60, 600);
            Line seesawH = new Line(0, i * 60, 600, i * 60);
            seesaw.setStroke(Color.RED);
            seesaw.setStrokeWidth(2);
            seesawH.setStroke(Color.GREEN);
            seesawH.setStrokeWidth(2);
            group.getChildren().addAll(seesaw, seesawH);
        }
        Line seesawF = new Line(600, 0, 600, 600);
        group.getChildren().add(seesawF);
        Launchjade();
        Initialise();
        drawagent(Envirenement.percept_getposition());
        group.getChildren().add(secouriste);
        final Scene scene = new Scene(group, 600, 600, Color.CORAL);
        stage.setTitle("Agent fire fighter  ");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    private static Circle createCircle(Color color) {
        final Circle circle = new Circle(25, 25, 25, color);
        circle.setOpacity(0.7);
        return circle;
    }

    public static void afficheGameOver() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label succes = new Label("");
                succes.setLayoutX(10);
                succes.setLayoutY(200);
                succes.setFont(new Font("Arial", 48));

            }
        });

    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "thread");
            thread.start();
        }
    }

    @Override
    public void run() {
        launch();
    }

    public static void Launchjade() {
        // Lancement de la plateforme Jade  
        try {

            // Get a hold on JADE runtime
            rt = jade.core.Runtime.instance();

            // Exit the JVM when there are no more containers around
            rt.setCloseVM(true);

            Profile pMain = new ProfileImpl(null, 8888, null);
            System.out.println("Launching jade");
            mc = rt.createMainContainer(pMain);

        } // fin du try 
        catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Envirenement envirenement = new Envirenement();
            AgentController Secouriste = mc.createNewAgent("secouriste", secouriste.class.getName(), new Object[]{});
            Secouriste.start();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    AgentController organe = mc.createNewAgent("organ" + i + j, Fire.class.getName(), new Object[]{});
                    organe.start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void Initialise() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                group.getChildren().add(Organe[i][j]);
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                changecolorEtata(new Position(i, j), Envirenement.perceptOrganismeEtat(new Position(i, j)));
            }
        }

    }

    private static Rectangle[][] createOrganisme() {
        Color etat_Color = Color.WHITE;;
        final Rectangle[][] rectangle = new Rectangle[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                final Rectangle rect = new Rectangle(i * 60 + 5, j * 60 + 5, 55, 55);
                rect.setFill(etat_Color);
                rectangle[i][j] = rect;
            }
        }
        return rectangle;
    }

    public static void changecolorEtata(Position position, int etat) {
        Color etat_Color = Color.WHITE;
        switch (etat) {

            case 0: {
                etat_Color = Color.WHITE;
                break;
            }
            case 1: {
                etat_Color = Color.RED;
                break;
            }

            case 2: {
                etat_Color = Color.BLUE;
                break;
            }
        }
        Organe[position.getX()][position.getY()].setFill(etat_Color);

    }

    public static void drawagent(Position position) {
        secouriste.setCenterX(position.getX() * 60 + 30);
        secouriste.setCenterY(position.getY() * 60 + 30);
    }
}
