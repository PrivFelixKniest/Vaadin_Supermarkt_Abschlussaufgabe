package objects;

import java.util.ArrayList;

public class Warehouse {
    private ArrayList<Product> myProducts;

    public Warehouse() {
        myProducts = new ArrayList<>();
    }

    public ArrayList<Product> getMyProducts() {
        return myProducts;
    }

    public void addProduct(Product p) {
        myProducts.add(p);
    }

    public void removeProduct(Product p) {
        myProducts.remove(p);
    }
}
