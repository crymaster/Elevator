/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;


import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author HoangSon
 */
public class Operate extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Building building = new Building(5);
        ImageView buildingView = building.loadBuildingView();
        ImageView elevatorView = building.loadElevator();
        building.startElevator();
        Person p1 = new Person();
        Person p2 = new Person();
        Person p3 = new Person();
        Person p4 = new Person();
        //double x = 785;
        //double y = 457;
        //elevatorView.setX(x);
        //elevatorView.setY(y);
        /*Path elevatorPath = new Path();        
        elevatorPath.getElements().add(new MoveTo(x, y));
        elevatorPath.getElements().add(new LineTo(x, y-103));
        PathTransition pathTran = new PathTransition(Duration.seconds(10), elevatorPath, elevatorView);
        pathTran.playFromStart();*/
        
        Pane root = new Pane();
        root.getChildren().add(buildingView);
        root.getChildren().add(elevatorView);
        Scene scene = new Scene(root, 839, 573);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        building.addPerson(p1, 1, 5);
        building.addPerson(p2, 1, 2);
        building.addPerson(p3, 4, 3);
        building.addPerson(p4, 3, 2);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
