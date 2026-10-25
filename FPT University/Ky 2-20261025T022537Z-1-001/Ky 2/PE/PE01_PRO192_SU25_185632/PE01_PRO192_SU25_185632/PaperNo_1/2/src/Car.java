/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GIA HUY
 */
public class Car {
    String brand;
    String model;
    int year;

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
    
    public int getCarAge(int currentYear){
    int age = currentYear - year;
    if(age < 0){
        return 0;
    }
    return age;
}

    @Override
    public String toString() {
        return brand + "," + model + "," + year ;
    }
    
}
