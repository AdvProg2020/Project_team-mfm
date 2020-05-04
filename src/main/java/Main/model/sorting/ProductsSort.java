package Main.model.sorting;

import Main.model.Product;

import java.util.Comparator;

public abstract class ProductsSort implements Comparator {

    //to use this for example: allProducts.sort(new ProductsSort.productSortByNameAscending());
    static class productSortByNameAscending implements Comparator<Product> {
        public int compare(Product one, Product two) {
            return one.getName().compareToIgnoreCase(two.getName());
        }
    }

    static class productSortByNameDescending implements Comparator<Product> {
        public int compare(Product one, Product two) {
            return (-1)*(one.getName().compareToIgnoreCase(two.getName()));
        }
    }

    static class productSortByView implements Comparator<Product> {
        public int compare(Product one, Product two) {
            return (-1)*(one.getOpenFrequency().compareTo(two.getOpenFrequency()));
        }
    }

    static class productSortByRate implements Comparator<Product> {
        public int compare(Product one, Product two) {
            return (-1)*(one.getAverageScore().compareTo(two.getAverageScore()));
        }
    }
}