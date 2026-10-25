/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GIA HUY
 */
public class ElectricCar extends Car{
   int batteryCapacity;

    public ElectricCar(String brand, String model, int year, int batteryCapacity) {
        super(brand, model, year);
        this.batteryCapacity = batteryCapacity;
    }
   
    public int getEstimatedRange(int speed){
      int K;
      if ( speed <= 60 ){
          K =5;
      }else{
          K =4;
      }
      int ER = batteryCapacity * K;
    return ER;
    }

    @Override
    public String toString() {
        return brand.toUpperCase() + "," + model + "," + year +","+ batteryCapacity;
    }
    
}
