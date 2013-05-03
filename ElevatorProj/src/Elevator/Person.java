/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Elevator;

/**
 *
 * @author HoangSon
 */
public class Person implements Runnable{
    static int count=0;
    int no;
    int status;
    int direction;
    int dest;
    private Floor floor;
    

    public Person() {
        no = count;
        count++;
    }
    
    public void setFloor(Floor floor){
        this.floor = floor;
    }
            
    private void walkTo(){
        System.out.println("Person " + no +" at "+(floor.no+1)+"F is walking to elevator");
    }
    
    private void walkAway(){
        System.out.println("Person " + no +" is walking away");
   }
    
   private void pushButton(){
        
        //System.out.println("Person " + no + " pushes button");
       floor.pushed(direction);
        //waitForElevator();
   }
    
    private void selectFloor(){
       System.out.println("Person " + no + " selects "+(dest+1)+"F");
        floor.sendSelectedFloor(dest);
   }
    
    private void waitForElevator(){
        System.out.println("Person " + no + " is waiting");
        enter();
    }
    
    private void enter(){
        floor.allowToEnter(direction);
        System.out.println("Person " + no + " enters the elevator");
    }
    
    private void goOut(){
        floor.notifyGoingOut();
        System.out.println("Person " + no + " goes out");
    }
    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        System.out.println(thisThread.getName());
        walkTo();
        pushButton();
        enter();
        selectFloor();
        goOut();
    }
}
