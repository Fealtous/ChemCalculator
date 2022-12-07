package dev.fealtous.chemcalculator;

public class ModifiableInt {
    private int val;
    public ModifiableInt(int val) {
        this.val = val;
    }
    public int get() {
        return val;
    }
    public void multiply(int m) {
        val *= m;
    }
    public void add(int a) {
        val += a;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
