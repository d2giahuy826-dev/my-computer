class Animal {
    String tag;
    int urgency, waiting;
    Animal(String t, int u, int w) { tag=t; urgency=u; waiting=w; }
    public String toString() { return "("+tag+","+urgency+","+waiting+")"; }
}
