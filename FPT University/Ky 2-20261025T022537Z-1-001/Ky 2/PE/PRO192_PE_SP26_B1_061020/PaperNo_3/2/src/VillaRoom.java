
import java.awt.AWTEventMulticaster;
import jdk.internal.vm.vector.VectorSupport;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GIA HUY
 */
public class VillaRoom extends ResortRoom {
    private int poolSize;

    public VillaRoom(String roomName, int dailyRate, int poolSize) {
        super(roomName, dailyRate);
        this.poolSize = poolSize;
    }


    @Override
    public String toString() {
        return "VillaRoom{" + "" + roomName + "," + dailyRate + "," + poolSize + '}';
    }
    
    public void setData(){
       System.out.print(roomName);
    }
    
    public int getValue(){
        if (poolSize > 20){
            return dailyRate * 2;
        }else {
            return dailyRate + 500;
        }
    }

}

