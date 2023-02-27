package control;

import objects.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ControlUnit {
    Warehouse myWarehouse;
    public ArrayList<ShoppingCart> myShoppingCarts;
    private double daylyIncome = 0.0d;

    public ControlUnit() {
        myWarehouse = new Warehouse();
        myShoppingCarts = new ArrayList<>();
        // initial add groceries
        myWarehouse.addProduct(new Groceries("Mineralwasser", 0.4d, 0.89d, false, LocalDate.of(2023, 4, 16), true));
        myWarehouse.addProduct(new Groceries("Toastbrot", 0.5d, 1.99d, false, LocalDate.of(2023, 6, 19),true));
        myWarehouse.addProduct(new Groceries("Butter", 0.39d, 1.49d, false, LocalDate.of(2023, 2, 26), true));
        myWarehouse.addProduct(new Groceries("Wurst", 0.69d, 1.99d, false, LocalDate.of(2023, 3, 12), false));
        myWarehouse.addProduct(new Groceries("Käse", 0.49d, 1.29d, false, LocalDate.of(2023, 2, 20), true));
        myWarehouse.addProduct(new Groceries("Flasche Wein (Weiß)", 1.99d, 6.99d, true, LocalDate.of(2024, 2, 12), true));
        myWarehouse.addProduct(new Groceries("Flasche Wein (Rot)", 2.29d, 7.99d, true, LocalDate.of(2023, 12, 7), true));
        // initial add household items
        myWarehouse.addProduct(new HouseholdItems("Klobürste", 0.99d, 4.99d, false, 30));
        myWarehouse.addProduct(new HouseholdItems("Plastikbesteck", 0.1d, 2.25d, false, 5));
        myWarehouse.addProduct(new HouseholdItems("Putzlappen", 0.99d, 3.99d, false, 60));
        myWarehouse.addProduct(new HouseholdItems("Zahncreme", 0.99d, 2.99d, false, 10));
        // initial add general items
        myWarehouse.addProduct(new GeneralItems("DVD Actionfilm", 5.99d, 12.99d, true));
        myWarehouse.addProduct(new GeneralItems("DVD Familienfilm", 3.99d, 8.99d, false));

    }

    public ArrayList<Product> getProducts() {
        return myWarehouse.getMyProducts();
    }
    
    public void addShoppingCart(boolean bioFilter, boolean ageFilter, boolean employeesDiscount, boolean economyFilter) {
    	myShoppingCarts.add(new ShoppingCart(bioFilter, ageFilter, employeesDiscount, economyFilter));
    }
    
    public double getDaylyIncome() {
    	return daylyIncome;
    }
    
    public void addToDaylyIncome(double add) {
    	daylyIncome += add;
    }
}
