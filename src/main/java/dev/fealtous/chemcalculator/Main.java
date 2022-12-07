package dev.fealtous.chemcalculator;

import dev.fealtous.chemcalculator.solver.Configuration;
import dev.fealtous.chemcalculator.solver.Solver;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        String mappingsFile = "/home/matthew/IdeaProjects/ChemCalculator/src/main/resources/elements.txt";
        Molecule.setMapping(mappingsFile);
        Molecule water = new Molecule("H2O");
        Molecule hydrogen = new Molecule("H2");
        Molecule oxygen = new Molecule("O2");
        Molecule[] reagents = {hydrogen,oxygen};
        Molecule[] products = {water};

        Collection<Configuration> solution = Solver.solve(new EquationConfiguration(reagents, products), new HashMap<>(), new LinkedList<>());
        Iterator<Configuration> iterator = solution.iterator();
        int i = 0;
        while(++i < solution.size()) {
            iterator.next();
        }
        System.out.println(iterator.next());

    }
}
