package dev.fealtous.chemcalculator;

import dev.fealtous.chemcalculator.solver.Configuration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class EquationConfiguration implements Configuration {
    private Molecule[] reagents;
    private int[] reagent_quantity;
    private Molecule[] products;
    private int[] product_quantity;

    public EquationConfiguration(Molecule[] reagents, Molecule[] products) {
        reagent_quantity = new int[reagents.length];
        product_quantity = new int[products.length];
        Arrays.fill(reagent_quantity,1);
        Arrays.fill(product_quantity,1);
        this.reagents = reagents;
        this.products = products;
    }

    public EquationConfiguration(Molecule[] reagents, int[] reagent_quantity, Molecule[] products, int[] product_quantity) {
        this.reagents = reagents;
        this.reagent_quantity = reagent_quantity;
        this.products = products;
        this.product_quantity = product_quantity;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new HashSet<>();
        int[] reagent_qt;
        int[] prod_qt = product_quantity.clone();

        for (int i = 0; i < reagent_quantity.length; i++) {
            reagent_qt = reagent_quantity.clone();
            reagent_qt[i]+=1;
            neighbors.add(new EquationConfiguration(reagents, reagent_qt, products, prod_qt));
        }
        reagent_qt = reagent_quantity.clone();
        for (int i = 0; i < product_quantity.length; i++) {
            prod_qt = product_quantity.clone();
            prod_qt[i]+=1;
            neighbors.add(new EquationConfiguration(reagents, reagent_qt, products, prod_qt));
        }
        return neighbors;
    }

    @Override
    public boolean isSolution() {
        float sum1 = 0;
        float sum2 = 0;
        for (int i = 0; i < reagents.length; i++) {
            sum1+=reagents[i].getMolarMass() * reagent_quantity[i];
        }
        for (int i = 0; i < products.length; i++) {
            sum2+=products[i].getMolarMass() * product_quantity[i];
        }
        return sum1==sum2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquationConfiguration that = (EquationConfiguration) o;
        return Arrays.equals(reagents, that.reagents) && Arrays.equals(reagent_quantity, that.reagent_quantity) && Arrays.equals(products, that.products) && Arrays.equals(product_quantity, that.product_quantity);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(reagents);
        result = 31 * result + Arrays.hashCode(reagent_quantity);
        result = 31 * result + Arrays.hashCode(products);
        result = 31 * result + Arrays.hashCode(product_quantity);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Reactants:\n");
        for (int i = 0; i < reagents.length; i++) {
            stringBuilder.append(reagents[i]).append(" ").append(reagent_quantity[i]).append("\n");
        }
        stringBuilder.append("Products:\n");
        for (int i = 0; i < products.length; i++) {
            stringBuilder.append(products[i]).append(" ").append(reagent_quantity[i]).append("\n");
        }
        return stringBuilder.toString();
    }
}
