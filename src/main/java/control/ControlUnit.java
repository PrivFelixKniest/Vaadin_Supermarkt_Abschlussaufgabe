package control;

import objects.*;
import objects.ShoppingCart.cartType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class ControlUnit {
    Warehouse myWarehouse;
    public ArrayList<ShoppingCart> myShoppingCarts;
    private double daylyIncome = 0.0d;
    private static ControlUnit cu = new ControlUnit();
    public enum Property {
		BestBy,
		Recycling,
		Restricted,
		None
	}
    public enum State {
		Home,
		AddItems,
		AddShoppingCart,
		ViewShoppingCarts,
		PasswordEmployees
	}

    ControlUnit() {
        myWarehouse = new Warehouse();
        myShoppingCarts = new ArrayList<>();
        // initial add groceries
        myWarehouse.addProduct(new Groceries("Mineralwasser", 0.4d, 0.89d, false, false, LocalDate.of(2023, 4, 16)));
        myWarehouse.addProduct(new Groceries("Toastbrot", 0.5d, 1.99d, false, false, LocalDate.of(2023, 6, 19)));
        myWarehouse.addProduct(new Groceries("Butter", 0.39d, 1.49d, false, false, LocalDate.of(2023, 2, 26)));
        myWarehouse.addProduct(new Groceries("Wurst", 0.69d, 1.99d, false, true, LocalDate.of(2023, 3, 12)));
        myWarehouse.addProduct(new Groceries("Käse", 0.49d, 1.29d, false, false, LocalDate.of(2023, 2, 20)));
        myWarehouse.addProduct(new Groceries("Flasche Wein", 1.99d, 6.99d, true, false, LocalDate.of(2024, 2, 12)));
        // initial add household items
        myWarehouse.addProduct(new HouseholdItems("Klobürste", 0.99d, 4.99d, false, false, 0.3));
        myWarehouse.addProduct(new HouseholdItems("Plastikbesteck", 0.1d, 2.25d, false, true, 0.5));
        myWarehouse.addProduct(new HouseholdItems("Putzlappen", 0.99d, 3.99d, false, false, 0.6));
        myWarehouse.addProduct(new HouseholdItems("Zahncreme", 0.99d, 2.99d, false, false, 0.1));
        // initial add general items
        myWarehouse.addProduct(new GeneralItems("DVD Actionfilm", 5.99d, 12.99d, true, false));
        myWarehouse.addProduct(new GeneralItems("DVD Familienfilm", 3.99d, 8.99d, false, false));
        
    }
    // Singelton design pattern test - TODO ist noch nicht in ausarbeitung 
    public static ControlUnit getInstance() {
    	return cu;
    }

    public ArrayList<Product> getProducts() {
        return myWarehouse.getMyProducts(cartType.None);
    }
    
    public void addShoppingCart(ShoppingCart shoppingCart) {
    	myShoppingCarts.add(shoppingCart);
    }
	public void removeShoppingCart(ShoppingCart shoppingCart) {
		for(ShoppingCart s : myShoppingCarts){
			if (s.getName().equals(shoppingCart.getName())) {
				myShoppingCarts.remove(s);
			}
		}
	}
    
    public double getDaylyIncome() {
    	return daylyIncome;
    }
    
    public void addToDaylyIncome(double add) {
    	daylyIncome = (Math.round((daylyIncome + add)*100.0)/100.0);
    }
    public boolean PasswordEmployeesShoppingCart(String input) {
    	if (input.equals("1234")) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public String propertyToString(Property prop) {
    	if (prop == Property.BestBy) {
    		return "Mindesthaltbarkeitsdatum";
    	} else if (prop == Property.Recycling) {
    		return "Recyclinganteil";
    	} else if (prop == Property.Restricted) {
    		return "Altersbeschränkt";
    	}
    	return "Undefiniert";
    }
    
    public String stateToString(State myState) {
		switch (myState) {
		case Home:
			return "Home - Dieser Einkaufswagen";
		case AddItems:
			return "Produkte Hinzufügen";
		case AddShoppingCart:
			return "Einkaufswagen Anlegen";
		case ViewShoppingCarts:
			return "Meine Einkaufswaegen";
		case PasswordEmployees:
			return "Einkaufswagen Anlegen - Passwort Eingeben";
		default:
			return "Unbekannte Seite";
		}
	}
    // get highest by index functions
    public int getHighestSpecialBestByDateIndex(ShoppingCart sc) {
		ArrayList<Product> thisProducts = new ArrayList<Product>(sc.getMyProducts());
		for (int i = sc.getMyProducts().size() - 1; i >= 0; i--) {
			if (!(sc.getMyProducts().get(i) instanceof Groceries)) {
				thisProducts.remove(i);
			}
		}
		if (thisProducts.size() > 0) {
    		Groceries p = (Groceries)thisProducts.get(0);
    		int h = 0;
        	for (int i = 1; i < thisProducts.size(); i ++) {
        		Groceries listProduct = (Groceries)thisProducts.get(i);
        		if (p.getBestByDate().isBefore(listProduct.getBestByDate())) {
        			h = i;
        		}
        	}
        	int index = sc.getMyProducts().indexOf(thisProducts.get(h));
        	return index;
    	}
		return -1;
    }
    
    public int getHighestSpecialRecyclingIndex(ShoppingCart sc) {
    	ArrayList<Product> thisProducts = new ArrayList<Product>(sc.getMyProducts());
		for (int i = sc.getMyProducts().size() - 1; i >= 0; i--) {
			if (!(sc.getMyProducts().get(i) instanceof HouseholdItems)) {
				thisProducts.remove(i);
			}
		}
		if (thisProducts.size() > 0) {
    		HouseholdItems p = (HouseholdItems)thisProducts.get(0);
    		int h = 0;
        	for (int i = 1; i < thisProducts.size(); i ++) {
        		HouseholdItems listProduct = (HouseholdItems)thisProducts.get(i);
        		if (p.getRecycleProportion() < listProduct.getRecycleProportion()) {
        			h = i;
        		}
        	}
        	int index = sc.getMyProducts().indexOf(thisProducts.get(h));
        	return index;
    	}
		return -1;
    }
    
    public int getHighestSpecialRestrictedIndex(ShoppingCart sc) {
    	ArrayList<Product> thisProducts = new ArrayList<Product>(sc.getMyProducts());
		for (int i = sc.getMyProducts().size() - 1; i >= 0; i--) {
			if (!(sc.getMyProducts().get(i) instanceof GeneralItems)) {
				thisProducts.remove(i);
			}
		}
		if (thisProducts.size() > 0) {
    		int h = 0;
        	for (int i = 1; i < thisProducts.size(); i ++) {
        		GeneralItems listProduct = (GeneralItems)thisProducts.get(i);
        		if (listProduct.isAgeRestriction()) {
        			h = i;
        		}
        	}
        	int index = sc.getMyProducts().indexOf(thisProducts.get(h));
        	return index;
    	}
		return -1;
    }
    
    // get lowest by index functions
    public int getLowestSpecialBestByDateIndex(ShoppingCart sc) {
		ArrayList<Product> thisProducts = new ArrayList<Product>(sc.getMyProducts());
		for (int i = sc.getMyProducts().size() - 1; i >= 0; i--) {
			if (!(sc.getMyProducts().get(i) instanceof Groceries)) {
				thisProducts.remove(i);
			}
		}
		if (thisProducts.size() > 0) {
    		Groceries p = (Groceries)thisProducts.get(0);
    		int h = 0;
        	for (int i = 1; i < thisProducts.size(); i ++) {
        		Groceries listProduct = (Groceries)thisProducts.get(i);
        		if (p.getBestByDate().isAfter(listProduct.getBestByDate())) {
        			h = i;
        		}
        	}
        	int index = sc.getMyProducts().indexOf(thisProducts.get(h));
        	return index;
    	}
		return -1;
    }
    
    public int getLowestSpecialRecyclingIndex(ShoppingCart sc) {
    	ArrayList<Product> thisProducts = new ArrayList<Product>(sc.getMyProducts());
		for (int i = sc.getMyProducts().size() - 1; i >= 0; i--) {
			if (!(sc.getMyProducts().get(i) instanceof HouseholdItems)) {
				thisProducts.remove(i);
			}
		}
		if (thisProducts.size() > 0) {
    		HouseholdItems p = (HouseholdItems)thisProducts.get(0);
    		int h = 0;
        	for (int i = 1; i < thisProducts.size(); i ++) {
        		HouseholdItems listProduct = (HouseholdItems)thisProducts.get(i);
        		if (p.getRecycleProportion() > listProduct.getRecycleProportion()) {
        			h = i;
        		}
        	}
        	int index = sc.getMyProducts().indexOf(thisProducts.get(h));
        	return index;
    	}
		return -1;
    }
    
    public int getLowestSpecialRestrictedIndex(ShoppingCart sc) {
    	ArrayList<Product> thisProducts = new ArrayList<Product>(sc.getMyProducts());
		for (int i = sc.getMyProducts().size() - 1; i >= 0; i--) {
			if (!(sc.getMyProducts().get(i) instanceof GeneralItems)) {
				thisProducts.remove(i);
			}
		}
		if (thisProducts.size() > 0) {
    		int h = 0;
        	for (int i = 1; i < thisProducts.size(); i ++) {
        		GeneralItems listProduct = (GeneralItems)thisProducts.get(i);
        		if (!listProduct.isAgeRestriction()) {
        			h = i;
        		}
        	}
        	int index = sc.getMyProducts().indexOf(thisProducts.get(h));
        	return index;
    	}
		return -1;
    }
    
    // Random Gift Carts
    
    public ArrayList<Product> getRandomProductList(int amount) {
	    Random rand = new Random(); 
	    ArrayList<Product> productList = new ArrayList<Product>();
	    ArrayList<Product> itemList = myWarehouse.getMyProducts(cartType.None);
	    int nextItem = rand.nextInt(itemList.size());
	    while ((getTotalListSellingPrice(productList) + itemList.get(nextItem).getSellingPrice()) < amount) {
		    productList.add(itemList.get(nextItem));
		    nextItem = rand.nextInt(itemList.size());
	    }
	    // "maybe another one might fit" - increase chances to get close to the desired amount
	    nextItem = rand.nextInt(itemList.size());
	    if ((getTotalListSellingPrice(productList) + itemList.get(nextItem).getSellingPrice()) < amount) {
		    productList.add(itemList.get(nextItem));
	    }
	    return productList;
    }
   
    private double getTotalListSellingPrice(ArrayList<Product> myProducts) {
    	double total = 0;
    	for (int i = 0; i < myProducts.size(); i++) {
    		total += myProducts.get(i).getSellingPrice();
    	}
    	return Math.round(total*100.0)/100.0;
    }
}
