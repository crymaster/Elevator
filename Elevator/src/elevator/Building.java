/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author HoangSon
 */
public class Building {
    Elevator elv;
    Floor[] floors;
   
    public Building(int numberOfFloor) {
        floors = new Floor[numberOfFloor];
        elv = new Elevator(floors,0);
        for(int i = 0; i<numberOfFloor; i++){
            floors[i] = new Floor(i);
            floors[i].setElevator(elv);
        }
    }
    
    public Building(int numberOfFloor, Elevator elv) {
        this.elv = elv;
        floors = new Floor[numberOfFloor];
        for(int i = 0; i<numberOfFloor; i++){
            floors[i] = new Floor(i);
            floors[i].setElevator(elv);
        }
        elv.floors = floors;
    }
    
    public void setElevator(Elevator elv){
        this.elv = elv;
    }
    
    public void addPerson(Person person,int currentFloor,int destination){
        if(currentFloor == destination){
            System.out.println("Person should have destination different from current floor");
            return;
        }
        if(currentFloor>floors.length || destination>floors.length){
            System.out.println("Floor must be equal or smaller than top floor");
            return;
        }
        if(currentFloor<1 || destination<1){
            System.out.println("Floor must be bigger than 0");
            return;
        }
        person.setFloor(floors[currentFloor-1]);
        person.dest = destination-1;
        if(currentFloor > destination){
            person.direction = -1;
        }
        else{
            person.direction = 1;
        }
        floors[currentFloor-1].addPerson(person);
    }
    
    public ImageView loadBuildingView(){
        Image buildingImage = new Image(getClass().getResourceAsStream("images/building.png"));
        ImageView buildingView = new ImageView(buildingImage);
        return buildingView;
    }
    
    public ImageView loadElevator(){
        return elv.getElevatorView();
    }
    
    public void startElevator(){
        new Thread(elv).start();
    }
}
