/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;

import java.util.LinkedList;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
/**
 *
 * @author HoangSon
 */
public class Elevator implements Runnable{
    public static final double INIT_X = 740;
    public static final double INIT_Y = 401;
    public static final double X_BASE = 785;
    public static final double Y_BASE = 457;
    public static final double FLOOR_GAP = 103;
    ImageView elevatorView;
    double currentY;
    int currentFloor;
    int direction;
    int capacity;
    int status;                         //0:idle;1:going;2:stop for passenger
    Floor[] floors;
    private LinkedList<Order> onOperating;
    private LinkedList<Order> onWaiting;
    public Elevator() {
        onOperating = new LinkedList<>();
        onWaiting = new LinkedList<>();
    }

    public Elevator(Floor[] floors,int currentFloor) {
        this.currentFloor = currentFloor;
        this.direction = 0;
        this.status = 0;
        this.floors = floors;
        this.capacity = 0;
        currentY = Y_BASE+currentFloor*FLOOR_GAP;
        onOperating = new LinkedList<>();
        onWaiting = new LinkedList<>();
    }
           
    private synchronized void idle(){
        if(status==0){
            try{
                System.out.println("Elevator is idle at "+(currentFloor+1)+"F");
                wait();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        notify();
    }
    
    private void move(){
        //Nothing to operate
        if(onOperating.isEmpty()){
            //No floors is waiting
            if(onWaiting.isEmpty()){
                status = 0;
                idle();
            }
            //Floors are waiting
           else{
                rearrange();
            }
        }
        //On operating
        else{
            if(status ==0){
                status = 1;
                notify();
                return;
            }
            currentFloor += direction;
            Path elevatorPath = new Path();        
            
            elevatorPath.getElements().add(new MoveTo(X_BASE, currentY));
            if(direction>0){
                currentY -= FLOOR_GAP;
                elevatorPath.getElements().add(new LineTo(X_BASE, currentY));
                PathTransition pathTran = new PathTransition(Duration.seconds(2), elevatorPath, elevatorView);
                pathTran.setInterpolator(Interpolator.LINEAR);
                pathTran.playFromStart();
                System.out.println("Up " + (currentFloor +1)+"F");
                try{
                    Thread.currentThread().sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            else if(direction<0){
                currentY += FLOOR_GAP;
                elevatorPath.getElements().add(new LineTo(X_BASE, currentY));
                PathTransition pathTran = new PathTransition(Duration.seconds(2), elevatorPath, elevatorView);
                pathTran.playFromStart();
                System.out.println("Down " + (currentFloor +1)+"F");
                try{
                    Thread.currentThread().sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
   private void checkToStop(){
       if(currentFloor == onOperating.peek().floor){    
            notifyFloor(onOperating.remove().direction);
            waitForPerson();
        }
   }
   
   private void notifyFloor(int direction){
        floors[currentFloor].openDoor();
        if(capacity>0){
            floors[currentFloor].notifyGoingOut();
            floors[currentFloor].notifyOutDone();
        }
        floors[currentFloor].notifyWaitingPerson(direction);
    }
    
    private void waitForPerson(){
        floors[currentFloor].notifyElevator();
   }
    
    private void rearrange(){
        onOperating.add(onWaiting.remove());
        if(currentFloor>onOperating.peek().floor){
            direction = -1;
        }
        else{
            direction = 1;
        }
        
        int j = -1;
        int direction;
        int floor;
        while(++j<onWaiting.size()){
            direction = onWaiting.get(j).direction;
            floor = onWaiting.get(j).floor;
            //Person's direction >< elevator's direction
            //Person's direction >< first order's direction
            if(direction != this.direction ||
               direction != onOperating.peek().direction){
                continue;
            }
            //Elevator down: Person's floor > elevator's floor
            //Elevator up: Person's floor < elevator's floor
           if((this.direction<0 && floor>currentFloor) ||
               (this.direction>0 && floor<currentFloor) ){
               continue;
            }
            //Person's direction = elevator's direction = first order's
            //Go Down
            if(this.direction<0){
                for(int i=0; i<onOperating.size(); i++){
                    if(floor>onOperating.get(i).floor){
                        onOperating.add(i, onWaiting.remove(j));
                        break;
                    }
                }
            }
            //Go Up
            else{
               for(int i=0; i<onOperating.size(); i++){
                    if(floor<onOperating.get(i).floor){
                        onOperating.add(i, onWaiting.remove(j));
                        break;
                    }
                }
            }
        }
    }
    private void going(){
        System.out.println("Elevator is going");
   }
  
  synchronized void arrangeSelectedFloor(int floor){
     if(onOperating.isEmpty()){
         if(floor>this.currentFloor){
            direction = 1;
            onOperating.add(new Order(floor, direction));
          Order order;
             for(int i=0;i<onWaiting.size();i++){
                   order = onWaiting.get(i);
                    if(direction == order.direction){
                       if(floor == order.floor){
                            onWaiting.remove(i);
                        }
                       else if(order.floor>currentFloor && order.floor<onOperating.peekLast().floor){
                           for(int j=0;j<onOperating.size();j++){
                                if(order.floor<onOperating.get(j).floor){
                                    onOperating.add(j, onWaiting.remove(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else{
               direction = -1;
               onOperating.add(new Order(floor, direction));
               Order order;
                for(int i=0;i<onWaiting.size();i++){
                    order = onWaiting.get(i);
                    if(direction == order.direction){
                        if(floor == order.floor){
                            onWaiting.remove(i);
                        }
                        else if(order.floor>currentFloor && order.floor<onOperating.peekLast().floor){
                            for(int j=0;j<onOperating.size();j++){
                               if(order.floor<onOperating.get(j).floor){
                                    onOperating.add(j, onWaiting.remove(i));
                                    break;
                                }
                            }
                        }
                    }
               }
            }
        }
        else{
            if(direction>0){
               int orderFloor;
                for(int i=0;i<onOperating.size();i++){
                   orderFloor = onOperating.get(i).floor;
                   if(floor==orderFloor){
                       break;
                    }
                    if(floor<orderFloor){
                        onOperating.add(i, new Order(floor, direction));
                        break;
                    }
                }
                Order order;
                for(int i=0;i<onWaiting.size();i++){
                    order = onWaiting.get(i);
                    if(direction == order.direction){
                        if(floor == order.floor){
                            onWaiting.remove(i);
                        }
                        else if(order.floor<currentFloor && order.floor>onOperating.peekLast().floor){
                            for(int j=0;j<onOperating.size();j++){
                               if(order.floor>onOperating.get(j).floor){
                                    onOperating.add(j, onWaiting.remove(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else{
                int orderFloor;
                for(int i=0;i<onOperating.size();i++){
                    orderFloor = onOperating.get(i).floor;
                    if(floor==orderFloor){
                        break;
                    }
                    if(floor>orderFloor){
                        onOperating.add(i, new Order(floor, direction));
                        break;
                    }
                }
                Order order;
                for(int i=0;i<onWaiting.size();i++){
                    order = onWaiting.get(i);
                   if(direction == order.direction){
                        if(floor == order.floor){
                            onWaiting.remove(i);
                        }
                        else if(order.floor<currentFloor && order.floor>onOperating.peekLast().floor){
                            for(int j=0;j<onOperating.size();j++){
                                if(order.floor>onOperating.get(j).floor){
                                    onOperating.add(j, onWaiting.remove(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    synchronized void pushed(int floor,int direction){
       //If elv is idle, move as the first person call
       if(status == 0){
           onOperating.add(new Order(floor,direction));
            if(currentFloor>floor){
                this.direction = -1;
            } if(currentFloor<floor){
                this.direction = 1;
           }else{
                this.direction = 0;
           }
            status = 1;
            idle();
            return;
        }
        //Person's direction >< elevator's direction
        //Person's direction >< first order's direction
        
        if(this.direction != direction ||
           direction != onOperating.peek().direction){
           onWaiting.add(new Order(floor, direction));
            return;
         }
        
        //Elevator down: Person's floor > elevator's floor
        //Elevator up: Person's floor < elevator's floor
        if((this.direction<0 && floor>currentFloor) ||
           (this.direction>0 && floor<currentFloor) ){
            onWaiting.add(new Order(floor, direction));
            return;
       }
      //Person's direction = elevator's direction = first order's
        //Go Down
       if(this.direction<0){
           for(int i=0; i<onOperating.size(); i++){
               if(floor>onOperating.get(i).floor){
                   onOperating.add(i, new Order(floor, this.direction));
                  break;
               }
            }
       }
        //Go Up
        else{
            for(int i=0; i<onOperating.size(); i++){
                if(floor<onOperating.get(i).floor){
                    onOperating.add(i, new Order(floor, this.direction));
                    break;
               }
           }
        }
  }
    
    void addPerson(Person person){
        person.setFloor(floors[person.dest]);
   }
  
    ImageView getElevatorView(){
        elevatorView = new ImageView(new Image(getClass().getResourceAsStream("images/elevator.png")));
        elevatorView.setX(INIT_X);
        elevatorView.setY(INIT_Y);
        return elevatorView;
    }
   @Override
   public void run() {
        while(true){
            move();
            checkToStop();
//            try{
//               Thread.currentThread().sleep(2000);
//           
//            }catch(InterruptedException ex){
//               ex.printStackTrace();
//           }
       }
    }
    
    
}