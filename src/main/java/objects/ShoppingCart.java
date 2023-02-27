package objects;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> myProducts;
    private boolean bioFilter;
    private boolean ageFilter;
    private boolean employeesDiscount;
    private boolean economyFilter;

    public ShoppingCart(boolean bioFilter, boolean ageFilter, boolean employeesDiscount, boolean economyFilter) {
        this.myProducts = new ArrayList<>();
        this.bioFilter = bioFilter;
        this.ageFilter = ageFilter;
        this.employeesDiscount = employeesDiscount;
        this.economyFilter = economyFilter;
    }

    public void addProduct(Product p) {
        myProducts.add(p);
    }

    public void removeProduct(Product p) {
        myProducts.remove(p);
    }
    
    public double getTotalPurchasePrice() {
    	double total = 0;
    	for (int i = 0; i < myProducts.size(); i++) {
    		total += myProducts.get(i).getPurchasePrice();
    	}
    	return Math.round(total*100.0)/100.0;
    }
    
    public double getTotalSellingPrice() {
    	double total = 0;
    	for (int i = 0; i < myProducts.size(); i++) {
    		total += myProducts.get(i).getSellingPrice();
    	}
    	return Math.round(total*100.0)/100.0;
    }

    public ArrayList<Product> getMyProducts() { return myProducts; }
    public boolean isAgeFilter() { return ageFilter; }
    public boolean isBioFilter() { return bioFilter; }
    public boolean isEmployeesDiscount() { return employeesDiscount; }
    public boolean isEconomyFilter() { return economyFilter; }
}
