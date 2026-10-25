/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GIA HUY
 */
public class ResortRoom {
    String roomName;
    int dailyRate;

    public ResortRoom(String roomName, int dailyRate) {
        this.roomName = roomName;
        this.dailyRate = dailyRate;
    }
    
    public String getRoomName(){
        return roomName.toLowerCase();
    
    }

    public int getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(int dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public String toString() {
        return "ResortRoom{" + "" + roomName + "," + dailyRate + '}';
    }

        
}
