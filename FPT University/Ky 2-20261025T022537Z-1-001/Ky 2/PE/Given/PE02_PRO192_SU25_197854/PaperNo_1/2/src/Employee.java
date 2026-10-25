/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GIA HUY
 */
public class Employee {
   String name;
   String id;
   double salary;

    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        
        this.salary = salary;
    }

   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
        public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public double getIncome(){
        return salary * 12;
    }

    @Override
    public String toString() {
    return id + "," + name + "," + String.format(java.util.Locale.US, "%.2f", getIncome());

    }
}
