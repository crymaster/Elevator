/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Elevator;

/**
 *
 * @author HoangSon
 */
public class Operate {
    public static void main(String[] args) {
        Building building = new Building(7);
        Person p1 = new Person();
        Person p2 = new Person();
        Person p3 = new Person();
        Person p4 = new Person();
        building.addPerson(p1, 1, 6);
        building.addPerson(p2, 3, 5);
        building.addPerson(p3, 4, 3);
        building.addPerson(p4, 3, 2);
    }
}

