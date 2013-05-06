/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;
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
    private Permission upPermission;
    private Permission downPermisson;
    private Wait outDone;
    private Wait inDone;
    public Floor(int no) {
        this.no = no;
        upButton = false;
        downButton = false;
        availableUp = false;
        availableDown = false;
        personUpList = new LinkedList<>();
        personDownList = new LinkedList<>();
        door = new Door();
        upPermission = new Permission();
        downPermisson = new Permission();
        outDone = new Wait();
        inDone = new Wait();
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
                elv.pushed(no,button);
                System.out.println(no+1+"F Up pressed");
            }
        }
        //Signal go down
        else{
            if(!downButton){
                downButton = true;
                elv.pushed(no,button);
                System.out.println(no+1+"F Down pressed");
            }
        }
    }
    
    void openDoor(){
        door.open();
        System.out.println("Door is opened");
    }
    
    void closeDoor(){
        door.close();
        System.out.println("Door is closed");
    }
    
    void sendSelectedFloor(int floor){
        elv.arrangeSelectedFloor(floor);
        System.out.println("Selected");
        if(floor>no){
           elv.addPerson(personUpList.remove());
           if(personUpList.isEmpty()){
               System.out.println("I'm last");
               closeDoor();
               notifyElevator();
           } 
        }
        else{
            elv.addPerson(personDownList.remove());
            if(personDownList.isEmpty()){
               closeDoor();
               notifyElevator();
           }
        }
    }
    
    void notifyElevator(){
        inDone.waitFor(!door.status, 1000);
//        if(door.status){
//            try{
//                wait(1000);
//                System.out.println("Elevator notified");
//            }
//            catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//        else{
//            System.out.println("I notifies");
//        }
//        notify();
    }
    
    synchronized void notifyGoingOut(){
        if(!door.status){
            try{
                wait();
                if(--elv.capacity==0){
                    notifyOutDone();
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
    }
    
    void notifyOutDone(){
        if(elv.capacity>0){
            outDone.waitFor(false,1000);
        }
        else{
            outDone.waitFor(true,0);
        }
        
    }
    
    void notifyWaitingPerson(int direction){
        if(direction>0){
            availableUp = true;
            upPermission.allow(availableUp);
        }
        else{
            availableDown = true;
            downPermisson.allow(availableDown);
        }
    }
    
    void allowToEnter(int direction){
        if(direction>0){
            upPermission.allow(availableUp);
        }
        else{
            downPermisson.allow(availableDown);
        }
        elv.capacity++;
    }
        
    void setElevator(Elevator elv){
        this.elv = elv;
    }
    
    class Permission{
        synchronized void allow(boolean available){
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
    
    class Wait{
        synchronized void waitFor(boolean available,long milis){
            if(!available){                
                try{
                    wait(milis);
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
            notify();
        }
    }
    
}
