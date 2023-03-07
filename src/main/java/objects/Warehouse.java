package objects;

import java.util.ArrayList;

import objects.ShoppingCart.cartType;

public class Warehouse {
    private ArrayList<Product> myProducts;

    public Warehouse() {
        myProducts = new ArrayList<>();
    }

    public ArrayList<Product> getMyProducts(cartType cartType) {
    	if (cartType == cartType.Age) {
    		ArrayList<Product> tempProducts = new ArrayList<>(myProducts);
    		for (int i = myProducts.size()-1; i >= 0; i--) {
    			if (myProducts.get(i).isAgeRestriction()) {
    				tempProducts.remove(i);
    			}
    		}
    		
    		return tempProducts;
    	}
    	else if(cartType == cartType.Bio) {
    		ArrayList<Product> tempProducts = myProducts;
    		for (int i = myProducts.size()-1; i >= 0; i--) {
    			if (myProducts.get(i).getBioRestriction()) {
    				tempProducts.remove(i);
    			}
    		}
    		
    		return tempProducts;
    	}
    	else {
    		return myProducts;
    	}
    }

    public void addProduct(Product p) {
        myProducts.add(p);
    }

    public void removeProduct(Product p) {
        myProducts.remove(p);
    }
}
