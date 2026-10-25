
public class Restaurant {

    private String restaurantName;
    private String city;
    private int seatedGuests;
    private int guestCapacity;

    public Restaurant(String restaurantName, String city, int seatedGuests, int guestCapacity) {
        this.restaurantName = restaurantName;
        this.city = city;
        this.seatedGuests = seatedGuests;
        this.guestCapacity = guestCapacity;
    }

    public String getRestaurantName() {
        String Name = "";
    
        if (restaurantName.length() > 10 ){
         Name = restaurantName.toUpperCase();            
      } 
        if (restaurantName.length()<=10){
  
     Name = restaurantName.substring(0, 10).toUpperCase();    
      }
     return Name;
  }
    public int getAvailableSeating(){
        int availableSeating;  
        if (( availableSeating = guestCapacity - seatedGuests ) < 0) {
           return 0; 
        }
        return availableSeating;
    }
    @Override
    public String toString() {
        return String.format("%s-%s-%d-%d",restaurantName, city , seatedGuests , guestCapacity );
    }
    
}