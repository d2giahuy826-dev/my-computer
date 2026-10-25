public class Boss extends Employee {

    private double extra;
    private int level;

   

    public Boss(String id, String name, double salary, double extra, int level) {
        super(id, name, salary);
        this.extra = extra;
        this.level = level;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public double getIncome() {

        if (level == 1) {
            return 12 * salary + extra;
        } else if (level == 2) {
            return 12 * salary * 1.2 + extra;
        } else if (level == 3) {
            return 12 * salary * 1.5 + extra;
        }

        return 0;
    }

    @Override
    public String toString() {
        return id + "," + name + ","
                + String.format(java.util.Locale.US, "%.2f", extra) + ","
                + level + ","
                + String.format(java.util.Locale.US, "%.2f", getIncome());
    }
}