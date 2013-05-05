/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author HoangSon
 */
public class Operate extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Building building = new Building(7);
        ImageView buildingView = building.loadImageView();
        Person p1 = new Person();
        Person p2 = new Person();
        Person p3 = new Person();
        Person p4 = new Person();
        building.addPerson(p1, 1, 6);
        building.addPerson(p2, 3, 5);
        building.addPerson(p3, 4, 3);
        building.addPerson(p4, 3, 2);
                
        GridPane root = new GridPane();
        root.add(buildingView,0,0);
        
        Scene scene = new Scene(root, 800, 573);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
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
