package control;

import objects.*;

import java.util.ArrayList;

public class CommandLineInterface {
    ControlUnit cu;
    ArrayList<ShoppingCart> myShoppingCart;
    public CommandLineInterface(){
        cu = new ControlUnit();
        myShoppingCart = new ArrayList<>();
    }

    public void ausgabe() {
        System.out.println("Wilkommen im CLI von Goosemart!");
        String ausgabe = "";
        for(Product product : cu.myWarehouse.getMyProducts()) {
            if(product instanceof Groceries) {
                Groceries temp = (Groceries) product;
                ausgabe = ausgabe + "\n\tProduktID: " + temp.getProductId() + " Produkt: " + temp.getProductDesignation() + " Preis: " + temp.getSellingPrice() + " MHD: " + temp.getBestByDate();
            } else if(product instanceof HouseholdItems) {
                HouseholdItems temp = (HouseholdItems) product;
                ausgabe = ausgabe + "\n\tProduktID: " + temp.getProductId() + " Produkt: " + temp.getProductDesignation() + " Preis:" + temp.getSellingPrice() + " Recycling_Anteil: " + temp.getRecycleProportion();
            } else if(product instanceof GeneralItems) {
                GeneralItems temp = (GeneralItems) product;
                ausgabe = ausgabe + "\n\tProduktID: " + temp.getProductId() + " Produkt: " + temp.getProductDesignation() + " Preis:" + temp.getSellingPrice();

            }
        }
        System.out.println(ausgabe);
    }
}
