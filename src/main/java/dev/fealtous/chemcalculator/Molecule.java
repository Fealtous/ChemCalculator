package dev.fealtous.chemcalculator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Molecule {
    // Contains H -> 1.0078 for example.
    private final String molecule; // Nope! We parse every time unless it's already defined. in composition
    private static final HashMap<String,Float> mapping = new HashMap<>();
    private HashMap<String, ModifiableInt> composition;
    public Molecule(String equation) {
        molecule = equation;
        getQuantities();
    }

    // If it contains "(" then it has a polyatomic ion
    // If it contains a polyatomic ion, then we do recursion and call it on that ion.
    // Basically, it's ElementQuantity(ElementQuantity)Quantity
    public Map<String,ModifiableInt> getQuantities() {
        composition = new HashMap<>();
        String[] sub_molecules = molecule.split("\\(");
        for (int i = 1; i < sub_molecules.length; i++) {
            new Molecule(sub_molecules[i]).getQuantities().forEach((k, v) -> {
                // If it's already in there,we need to update it. IIRC hashmaps don't support reassignment
                if (composition.containsKey(k)) {
                    composition.get(k).add(v.get());
                } else {
                    composition.put(k, new ModifiableInt(v.get()));
                }
            });
        }
        char[] chars = sub_molecules[0].toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // First, if we're looking at the beginning of an element name. It might be He or just H, we don't know yet.
            // Alternatively it could be like CO2, where C and O are different elements.
            if (Character.isUpperCase(chars[i])) {
                if (i + 1 < chars.length && Character.isUpperCase(chars[i + 1])) {
                    composition.put(String.valueOf(chars[i]), new ModifiableInt(1)); // If it is CO2, we are at C, check O, it's capital, so C is quantity 1.
                    // Do not skip, no digits processed.
                } else if (i + 1 < chars.length && Character.isDigit(chars[i + 1])) {
                    // Okay, it wasn't CO2, so it's like H20. But what if it's 2 digits of atoms? Like C60?
                    if (i + 2 < chars.length && Character.isDigit(chars[i + 2])) {
                        // No need to go higher than that. I won't be working with any molecules with more than 9 in a slot anyway
                        StringBuilder x = new StringBuilder();
                        x.append(chars[i + 1]).append(chars[i + 2]);
                        composition.put(String.valueOf(chars[i]), new ModifiableInt(Integer.valueOf(x.toString())));
                        i += 2; // Make sure to skip, since we processed *both* digits.
                    } else {
                        //It wasn't C60, so it's N#, put in the map.
                        composition.put(String.valueOf(chars[i]), new ModifiableInt(Integer.valueOf(String.valueOf(chars[i + 1]))));
                        i++; // Make sure to skip, since we already processed the digit.
                    }
                }
                else {
                    composition.put(String.valueOf(chars[i]), new ModifiableInt(1));
                }
            }
            else if (chars[i] == ')') {
                int finalI = i;
                composition.forEach((k, v) -> {
                    int val = Integer.parseInt(String.valueOf(chars[finalI + 1]));
                   composition.get(k).multiply(val);
                });
                i++;
            }
        }

        return composition;
    }
    public float getMolarMass() {
        ModifiableFloat sum = new ModifiableFloat(0f);
        composition.forEach((k,v) -> sum.add(mapping.get(k) * composition.get(k).get()));
        return sum.get();
    }
    public static void setMapping(String filepath) {
        File mappingsFile = new File(filepath);
        Scanner f;
        try {
            f = new Scanner(mappingsFile);
            while (f.hasNext()) {
                String s = f.nextLine();
                String[] s2 = s.split(" ");
                mapping.put(s2[0],Float.parseFloat(s2[1]));
            }
            f.close();
        } catch (IOException e) {
            System.out.println("Mappings failed to apply, check file path.");
        }
    }

    @Override
    public boolean equals(Object o) {
        return this.molecule.equals(((Molecule) o).molecule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(molecule, composition);
    }

    @Override
    public String toString() {
        return molecule;
    }
}
