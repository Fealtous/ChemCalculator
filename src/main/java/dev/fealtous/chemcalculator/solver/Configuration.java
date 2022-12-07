package dev.fealtous.chemcalculator.solver;

import java.util.Collection;

public interface Configuration {
    Collection<Configuration> getNeighbors();
    boolean isSolution();
}
