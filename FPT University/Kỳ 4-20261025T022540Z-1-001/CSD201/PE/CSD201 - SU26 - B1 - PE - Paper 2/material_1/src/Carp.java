// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============
class Carp {
  String pond;
  int size,state;
  Carp() {
   }
  Carp(String xPond, int xSize, int xState){
    pond=xPond;size=xSize; state=xState;
   }
  public String toString(){
    return("(" +pond+","+size + "," + state + ")");
   }
 }
