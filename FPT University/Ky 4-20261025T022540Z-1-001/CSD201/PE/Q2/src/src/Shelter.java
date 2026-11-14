class Shelter {
    int id, capacity, occupied;
    Shelter(int i, int c, int o) { id=i; capacity=c; occupied=o; }
    int free() { return capacity-occupied; }
    public String toString() { return "("+id+","+capacity+","+occupied+")"; }
}
