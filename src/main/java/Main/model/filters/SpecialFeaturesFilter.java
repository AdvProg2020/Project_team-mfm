package Main.model.filters;

import Main.model.Product;

import java.util.ArrayList;

public class SpecialFeaturesFilter extends Filter {
    private final String name;
    private String featureTitle;
    private String desiredFilter;

    public SpecialFeaturesFilter(String name, String featureTitle, String desiredFilter, ArrayList<Product> products) {
        this.name = name;
    }
}
