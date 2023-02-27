package control;

import java.time.LocalDate;
import java.util.Scanner;


import objects.GeneralItems;
import objects.Groceries;
import objects.HouseholdItems;
import objects.ShoppingCart;

// TODO (remove shopping carts / products)
// TODO display daylyIncome counter
// TODO Mitarbeiter login on einkaufswagen anlegen
// TODO EK / VK nur eins anzeigen je nach korb typ
// TODO EK / VK entscheiden welches zum daylyCounter geadded werden muss, je nach korb typ
// TODO Einkaufswagenfilter
// TODO Javadoc


public class ShoppingCLI {
	ControlUnit cu = new ControlUnit();
	public int currentShoppingCartIndex = 0;
	public enum State {
			Home,
			AddItems,
			AddShoppingCart,
			ViewShoppingCarts
	}
	State myState;
	
	
	public static void main(String[] args) {
		
		new ShoppingCLI();
	}
	
	public ShoppingCLI() {
		// width = 80 Zeichen
		
		// init view
		boolean running = true;
		Scanner sc = new Scanner(System.in);
		myState = State.Home;
		printSpacer();
		
		//runtime
		while (running) {
			// Views by State
			if (myState == State.Home) {
				printWelcome();
				printShoppingCartContent(currentShoppingCartIndex);
			}
			else if (myState == State.AddItems) {
				printWelcome();
				printItemList();
			}
			else if (myState == State.AddShoppingCart) {
				printWelcome();
				printShoppingCartList();
			}
			else if (myState == State.ViewShoppingCarts) {
				printWelcome();
				printMyShoppingCarts();
			}
			// CommandInput
			String input = printCommandLine(sc);
			evalInput(input);
			printSpacer();
		}
		sc.close();
	}
	
	// page methods
	
	private void printWelcome() {
		System.out.println("(" + getStateString() + ") Willkommen im Goosemart ");
		printLine();
		System.out.println("");
	}
	
	private void printItemList() {
		
		if (cu.myShoppingCarts.size() > 0) {
			System.out.println("- Alle verfügbaren Produkte (Aktueller Einkaufswagen: " + (currentShoppingCartIndex+1) + "/" + cu.myShoppingCarts.size() + ") -");
			System.out.println("- Index  |  Produkt  |  EK  |  VK  |  Produktspeziefische Eigenschaft -");
			
			for (int i = 0; i < (cu.myWarehouse.getMyProducts()).size(); i++) {
				System.out.print((i+1) + "  |  ");
				System.out.print(cu.myWarehouse.getMyProducts().get(i).getProductDesignation() + "  |  ");
				System.out.print(cu.myWarehouse.getMyProducts().get(i).getPurchasePrice() + "\u20AC" + "  |  ");
				System.out.print(cu.myWarehouse.getMyProducts().get(i).getSellingPrice() + "\u20AC" + "  |  ");
				// Typecheck for product specific information
				if (cu.myWarehouse.getMyProducts().get(i) instanceof Groceries) {
					Groceries temp = (Groceries)cu.myWarehouse.getMyProducts().get(i);
					System.out.print("Mindestens Haltbar bis: " + temp.getBestByDate());
				}
				else if (cu.myWarehouse.getMyProducts().get(i) instanceof HouseholdItems) {
					HouseholdItems temp = (HouseholdItems)cu.myWarehouse.getMyProducts().get(i);
					System.out.print("Anteil an Recyclebaren Materialien: " + temp.getRecycleProportion());
				}
				else if (cu.myWarehouse.getMyProducts().get(i) instanceof GeneralItems) {
					if (cu.myWarehouse.getMyProducts().get(i).isAgeRestriction()) {
						System.out.print("Altersbeschraenkung Ü18");
					} else {
						System.out.print("Nicht Altersbeschraenkt");
					}
				}
				System.out.println();
			}
		} else {
			System.out.println("- Kein Einkaufswagen Vorhanden -");
		}
	}
	
	private void printShoppingCartList() {
		System.out.println("- Index  |  Einkaufswagentyp -");
		System.out.println("1  |  Spar Einkaufswagen");
		System.out.println("2  |  Altersbeschraenkter Einfaufswagen");
		System.out.println("3  |  Bio Einkaufswagen");
		System.out.println("4  |  Mitarbeiter Einkaufswagen");
	}
	
	private void printMyShoppingCarts() {
		
		if (cu.myShoppingCarts.size() > 0) {
			System.out.println("Meine Einkaufswaegen - Einkaufswagen waehlen");
			for (int i = 0; i < cu.myShoppingCarts.size(); i++) {
				System.out.print((i+1) + "  |  ");
				if (cu.myShoppingCarts.get(i).isAgeFilter()) {
					System.out.println("Altersbeschraenkter Einfaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).isBioFilter()) {
					System.out.println("Bio Einkaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).isEconomyFilter()) {
					System.out.println("Spar Einkaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).isEmployeesDiscount()) {
					System.out.println("Mitarbeiter Einkaufswagen");
				}
			}
		} else {
			System.out.print("- Kein Einkaufswagen Vorhanden -");
		}
		System.out.println();
	}
	
	private void printShoppingCartContent(int cartIndex) {
		if (cu.myShoppingCarts.size() > 0) {
			System.out.println("- Alle Produkte im Einkaufswagen (" + (cartIndex+1) + "/" + cu.myShoppingCarts.size() + ") -");
			System.out.println("- Index  |  Produkt  |  EK  |  VK  |  Produktspeziefische Eigenschaft -");
			for (int i = 0; i < cu.myShoppingCarts.get(cartIndex).getMyProducts().size(); i++) {
				System.out.print((i+1) + "  |  ");
				System.out.print(cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).getProductDesignation() + "  |  ");
				System.out.print(cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).getPurchasePrice() + "\u20AC" + "  |  ");
				System.out.print(cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).getSellingPrice() + "\u20AC" + "  |  ");
				// Type Check for product specific information
				if (cu.myWarehouse.getMyProducts().get(i) instanceof Groceries) {
					Groceries temp = (Groceries)cu.myWarehouse.getMyProducts().get(i);
					System.out.print("Mindestens Haltbar bis: " + temp.getBestByDate());
				}
				else if (cu.myWarehouse.getMyProducts().get(i) instanceof HouseholdItems) {
					HouseholdItems temp = (HouseholdItems)cu.myWarehouse.getMyProducts().get(i);
					System.out.print("Anteil an Recyclebaren Materialien: " + temp.getRecycleProportion());
				}
				else if (cu.myWarehouse.getMyProducts().get(i) instanceof GeneralItems) {
					if (cu.myWarehouse.getMyProducts().get(i).isAgeRestriction()) {
						System.out.print("Altersbeschraenkung Ü18");
					} else {
						System.out.print("Nicht Altersbeschraenkt");
					}
				}
				System.out.println("");
			}
			System.out.println("");
			System.out.println("Total EK: " + 
					cu.myShoppingCarts.get(cartIndex).getTotalPurchasePrice() + 
					"\u20AC" + 
					" Total VK: " + 
					cu.myShoppingCarts.get(cartIndex).getTotalSellingPrice() + 
					"\u20AC");
		} else {
			System.out.print("- Kein Einkaufswagen Vorhanden -");
		}
		System.out.println();
	}
	
	private String printCommandLine(Scanner sc) {
		System.out.println();
		printLine();
		System.out.println("    Legende| ':' entsprechender Befehl, '/' nächster Befehl, ',' oder, '...' u.s.w.");
		switch (myState) {
			case Home:
				System.out.println("--- Befehle| Einkaufswagen Anlegen: ea / Einkaufswagen Wechseln: ew / Produkt Hinzufügen: ph / Bezahlen: b ---");
				break;
			case AddItems:
				System.out.println("--- Befehle| Zurueck: z / Produkt Waehlen: 1,2,... ---");
				break;
			case AddShoppingCart:
				System.out.println("--- Befehle| Zurueck: z / Einkaufswagen Waehlen: 1,2,3,4 ---");
				break;
			case ViewShoppingCarts:
				System.out.println("--- Befehle| Zurueck: z / Einkaufswagen Waehlen: 1,2,... / Einkaufswagen Wechseln: ew ---");
				break;	
		}
		System.out.print("<Behfelszeile> => ");
		return sc.nextLine();
	}
	
	// utility methods
	
	private void printSpacer() {
		for (int i = 0; i < 30; i++) {
			System.out.println("");
		}
	}
	
	private void printLine() {
				for (int i = 0; i < 80; i++) {
					System.out.print("-");
				}
				System.out.println("");
	}
	
	private String getStateString() {
		switch (myState) {
		case Home:
			return "Home - Dieser Einkaufswagen";
		case AddItems:
			return "Produkte Hinzufügen";
		case AddShoppingCart:
			return "Einkaufswagen Anlegen";
		case ViewShoppingCarts:
			return "Meine Einkaufswaegen";
		default:
			return "Unbekannte Seite";
		}
	}
	
	private boolean isStrInt(String str) {
		try {
			Integer.parseInt(str);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private void evalInput(String input) {
		if (myState == State.Home) {
			switch (input) {
				case "ea":
					myState = State.AddShoppingCart;
					break;
				case "ew":
					myState = State.ViewShoppingCarts;
					break;
				case "ph":
					myState = State.AddItems;
					break;
				case "b":
					cu.addToDaylyIncome(
							cu.myShoppingCarts.get(currentShoppingCartIndex)
							.getTotalPurchasePrice());
					cu.myShoppingCarts.remove(currentShoppingCartIndex);
					currentShoppingCartIndex = 1;
					myState = State.ViewShoppingCarts;
					break;
				case "z":
					myState = State.Home;
				default:
					break;
			}
		} else if (myState == State.AddShoppingCart) {
			switch (input) {
				case "1":
					// Spar
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(false, false, false, true);
					break;
				case "2":
					// Ü18
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(false, true, false, false);
					break;
				case "3":
					// Bio
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(true, false, false, false);
					break;
				case "4":
					// Mitarbeiter
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(false, false, true, false);
					break;
				case "z":
					myState = State.Home;
				default:
					break;
			}
		} else if (myState == State.AddItems) {
			switch(input) {
				case "z":
					myState = State.Home;
					break;
				default:
					if (cu.myShoppingCarts.size() > 0) {
						for (int i = 1; i <= cu.myWarehouse.getMyProducts().size(); i++) {
							if (isStrInt(input)) {
								if (Integer.parseInt(input) == i) {
									cu.myShoppingCarts.get(currentShoppingCartIndex)
										.addProduct(cu.myWarehouse.getMyProducts().get(i-1));
									myState = State.Home;
								}
							}
						}
					}
					break;
			}
		} else if (myState == State.ViewShoppingCarts) {
			switch (input) {
			case "ea":
				myState = State.AddShoppingCart;
				break;
			case "z":
				myState = State.Home;
			default:
				for (int i = 1; i <= cu.myShoppingCarts.size(); i++) {
					if (isStrInt(input)) {
						if (Integer.parseInt(input) == i) {
							currentShoppingCartIndex = (i-1);
							myState = State.Home;
						}
					}
				}
				break;
		}
		} else {
			switch (input) {
				case "z":
					myState = State.Home;
				default:
					break;
			}
		}
	}
}
