/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Elevator;

import java.util.ArrayList;

/**
 *
 * @author HoangSon
 */
public class Door {
    boolean status;
    
    void open(){
        status = true;
    }
    
    void close(){
        status = false;
    }
}

