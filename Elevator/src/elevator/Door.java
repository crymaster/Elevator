/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevator;

/**
 *
 * @author HoangSon
 */
public class Door {
    boolean status;
    
    Door(){
        status = false;
    }
    void open(){
        status = true;
    }
    
    void close(){
        status = false;
    }
}
