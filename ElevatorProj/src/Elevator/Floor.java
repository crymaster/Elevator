/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Elevator;
import java.util.LinkedList;

/**
 *
 * @author HoangSon
 */
public class Floor {
    int no;
    private boolean upButton;
    private boolean downButton;
    private boolean availableUp;
    private boolean availableDown;
    Door door;
    LinkedList<Person> personUpList;
    LinkedList<Person> personDownList;
    private Elevator elv;
    private EnterPermission upPermission;
    private EnterPermission downPermisson;
    
    public Floor(int no) {
        this.no = no;
        upButton = false;
        downButton = false;
        availableUp = false;
        availableDown = false;
        personUpList = new LinkedList<>();
        personDownList = new LinkedList<>();
        door = new Door();
        upPermission = new EnterPermission();
        downPermisson = new EnterPermission();
    }
    
    void addPerson(Person person){
        if(person.direction>0){
            personUpList.add(person);
        }
        else{
            personDownList.add(person);
        }
        new Thread(person).start();
    }
 
    void pushed(int button){
        //Signal go up
        if(button > 0){
            //Up button has not been pressed
            if(!upButton){
                upButton = true;
                System.out.println(no+1+"F Up pressed");
                elv.pushed(no,button);
            }
        }
        //Signal go down
        else{
            if(!downButton){
                downButton = true;
                System.out.println(no+1+"F Down pressed");
                elv.pushed(no,button);
            }
        }
    }
    
    void openDoor(){
        System.out.println("Door is opened");
        door.open();
    }
    
    void closeDoor(){
        System.out.println("Door is closed");
        door.close();
    }
    
    void sendSelectedFloor(int floor){
        elv.arrangeSelectedFloor(floor);
        if(floor>no){
           elv.addPerson(personUpList.remove());
           if(personUpList.isEmpty()){
               door.close();
               notifyElevator();
           } 
        }
        else{
            elv.addPerson(personDownList.remove());
            if(personDownList.isEmpty()){
               door.close();
               notifyElevator();
           }
        }
    }
    
    synchronized void notifyElevator(){
        if(door.status){
            try{
                wait(2000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
    }
    
    synchronized void notifyGoingOut(){
        if(!door.status){
            try{
                wait();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
    }
    
    void notifyWaitingPerson(int direction){
        if(direction>0){
            availableUp = true;
            upPermission.allowToEnter(availableUp);
        }
        else{
            availableDown = true;
            downPermisson.allowToEnter(availableDown);
        }
    }
    
    void allowToEnter(int direction){
        if(direction>0){
            upPermission.allowToEnter(availableUp);
        }
        else{
           downPermisson.allowToEnter(availableDown);
        }
    }
        
    void setElevator(Elevator elv){
        this.elv = elv;
    }
    
    class EnterPermission{
        synchronized void allowToEnter(boolean available){
            if(!available){                
                try{
                    wait();
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
            notify();
        }
    }
    
    
}
