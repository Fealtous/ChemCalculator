package dev.fealtous.chemcalculator;

public class ModifiableFloat {
    private float val;
    public ModifiableFloat(float val) {
        this.val = val;
    }
    public float get() {
        return val;
    }
    public void multiply(float m) {
        val *= m;
    }
    public void add(float a) {
        val += a;
    }
}
