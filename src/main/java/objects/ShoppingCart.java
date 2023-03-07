package objects;

import java.util.ArrayList;

public class ShoppingCart {
	public enum cartType {
		Bio,
		Employee,
		Economy,
		Age,
		Gift,
		None
	}
	
    private ArrayList<Product> myProducts;
    private cartType cartType;

    public ShoppingCart(cartType cartType) {
        this.myProducts = new ArrayList<>();
        this.cartType = cartType;
    }

    public void addProduct(Product p) {
        myProducts.add(p);
    }

    public void removeProductByIndex(int i) {
        myProducts.remove(i);
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
    
    public cartType getCartType() { return cartType; }
}
