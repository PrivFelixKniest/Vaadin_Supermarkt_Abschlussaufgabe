package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import control.ControlUnit.Property;
import control.ControlUnit.State;
import objects.GeneralItems;
import objects.Groceries;
import objects.HouseholdItems;
import objects.Product;
import objects.ShoppingCart;
import objects.ShoppingCart.cartType;

// TODO Javadoc


public class ShoppingCLI {
	private ControlUnit cu = new ControlUnit();
	private int currentShoppingCartIndex = 0;
	private Property highestSpecialProperty = Property.None;
	private Property lowestSpecialProperty = Property.None;
	private cartType thisCartType;
	private State myState;
	private boolean running = true;
	
	
	public static void main(String[] args) {
		new ShoppingCLI();
	}
	
	public ShoppingCLI() {
		// width = 80 Zeichen
		
		// init view
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
			else if (myState == State.PasswordEmployees) {
				printWelcome();
				System.out.println("- Bitte das Passwort Eingeben -");
			}
			
			// CommandInput
			String input = printCommandLine(sc);
			evalInput(input);
			printSpacer();
			if (cu.myShoppingCarts.size() != 0) {
				thisCartType = cu.myShoppingCarts.get(currentShoppingCartIndex).getCartType();
			}
		}
		System.out.println("-> Laden geschlossen, Auf wiedersehen ...");
		sc.close();
	}
	
	// page methods
	
	private void printWelcome() {
		System.out.println("(" + cu.stateToString(myState) + ") Willkommen im Goosemart [Tageseinnahmen " + cu.getDaylyIncome() + "\u20AC]");
		printLine();
		System.out.println("");
	}
	
	private void printItemList() {
		
		if (cu.myShoppingCarts.size() > 0) {
			System.out.println("- Alle verfügbaren Produkte (Aktueller Einkaufswagen: " + (currentShoppingCartIndex+1) + "/" + cu.myShoppingCarts.size() + ") -");
			System.out.print("- Index  |  Produkt  |  ");
			System.out.print("Preis");
			System.out.println("  |  Produktspeziefische Eigenschaft -");
			
			for (int i = 0; i < (cu.myWarehouse.getMyProducts(thisCartType)).size(); i++) {
				System.out.print((i+1) + "  |  ");
				System.out.print(cu.myWarehouse.getMyProducts(thisCartType).get(i).getProductDesignation() + "  |  ");
				if (thisCartType == cartType.Employee) {
					System.out.print(cu.myWarehouse.getMyProducts(thisCartType).get(i).getPurchasePrice() + "\u20AC" + "  |  ");
				} else {
					System.out.print(cu.myWarehouse.getMyProducts(thisCartType).get(i).getSellingPrice() + "\u20AC" + "  |  ");
				}
				// Typecheck for product specific information
				if (cu.myWarehouse.getMyProducts(thisCartType).get(i) instanceof Groceries) {
					Groceries temp = (Groceries)cu.myWarehouse.getMyProducts(thisCartType).get(i);
					System.out.print("Mindestens Haltbar bis: " + temp.getBestByDate());
				}
				else if (cu.myWarehouse.getMyProducts(thisCartType).get(i) instanceof HouseholdItems) {
					HouseholdItems temp = (HouseholdItems)cu.myWarehouse.getMyProducts(thisCartType).get(i);
					System.out.print("Anteil an Recyclebaren Materialien: " + temp.getRecycleProportion());
				}
				else if (cu.myWarehouse.getMyProducts(thisCartType).get(i) instanceof GeneralItems) {
					if (cu.myWarehouse.getMyProducts(thisCartType).get(i).isAgeRestriction()) {
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
		System.out.println("1  |  Spar Einkaufswagen (Warenwert auf insgesamt 50\u20AC begrenzt)");
		System.out.println("2  |  Altersbeschraenkter Einfaufswagen (Nur jugendfreie Produkte)");
		System.out.println("3  |  Oeko Einkaufswagen (Nur oekologisch nachhaltige Produkte)");
		System.out.println("4  |  Mitarbeiter Einkaufswagen (Mitarbeiterangebot zum Einkaufspreis)");
		System.out.println("5  |  Geschenkoption: bis 10 Euro (zufaellige Produkte - nicht veraenderbar)");
		System.out.println("6  |  Geschenkoption: bis 20 Euro (zufaellige Produkte - nicht veraenderbar)");
		System.out.println("7  |  Geschenkoption: bis 50 Euro (zufaellige Produkte - nicht veraenderbar)");
	}
	
	private void printMyShoppingCarts() {
		if (cu.myShoppingCarts.size() > 0) {
			System.out.println("Meine Einkaufswaegen - Einkaufswagen waehlen");
			for (int i = 0; i < cu.myShoppingCarts.size(); i++) {
				//Index
				System.out.print((i+1) + "  |  ");
				//Name
				if (cu.myShoppingCarts.get(i).getCartType() == cartType.Age) {
					System.out.print("Altersbeschraenkter Einfaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).getCartType() == cartType.Bio) {
					System.out.print("Oeko Einkaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).getCartType() == cartType.Economy) {
					System.out.print("Spar Einkaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).getCartType() == cartType.Employee) {
					System.out.print("Mitarbeiter Einkaufswagen");
				}
				else if (cu.myShoppingCarts.get(i).getCartType() == cartType.Gift) {
					System.out.print("Geschekoption");
				}
				//Total
				if (cu.myShoppingCarts.get(i).getCartType() == cartType.Employee) {
					System.out.println(" - Total: " + cu.myShoppingCarts.get(i).getTotalPurchasePrice() + "\u20AC");
				} 
				else if (cu.myShoppingCarts.get(i).getCartType() == cartType.Economy) {
					System.out.println(" - Total: " + cu.myShoppingCarts.get(i).getTotalSellingPrice() + "\u20AC (max. 50\u20AC)");
				} 
				else {
					System.out.println(" - Total: " + cu.myShoppingCarts.get(i).getTotalSellingPrice() + "\u20AC");	
				}
				
			}
		} else {
			System.out.println("- Kein Einkaufswagen Vorhanden -");
		}
	}
	
	private void printShoppingCartContent(int cartIndex) {
		if (cu.myShoppingCarts.size() > 0) {
			System.out.println("- Alle Produkte im Einkaufswagen (" + (cartIndex+1) + "/" + cu.myShoppingCarts.size() + ") -");
			System.out.print("- Index  |  Produkt  |  ");
			System.out.print("Preis");
			System.out.println("  |  Produktspeziefische Eigenschaft -");
			for (int i = 0; i < cu.myShoppingCarts.get(cartIndex).getMyProducts().size(); i++) {
				System.out.print((i+1) + "  |  ");
				System.out.print(cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).getProductDesignation() + "  |  ");
				if (thisCartType == cartType.Employee) {
					System.out.print(cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).getPurchasePrice() + "\u20AC" + "  |  ");
				} else {
					System.out.print(cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).getSellingPrice() + "\u20AC" + "  |  ");
				}
				// Type Check for product specific information
				if (cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i) instanceof Groceries) {
					Groceries temp = (Groceries)cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i);
					System.out.print("Mindestens Haltbar bis: " + temp.getBestByDate());
				}
				else if (cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i) instanceof HouseholdItems) {
					HouseholdItems temp = (HouseholdItems)cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i);
					System.out.print("Anteil an Recyclebaren Materialien: " + temp.getRecycleProportion());
				}
				else if (cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i) instanceof GeneralItems) {
					if (cu.myShoppingCarts.get(cartIndex).getMyProducts().get(i).isAgeRestriction()) {
						System.out.print("Altersbeschraenkung Ü18");
					} else {
						System.out.print("Nicht Altersbeschraenkt");
					}
				}
				System.out.println("");
			}
			System.out.println("");
			
			// Total
			if (thisCartType == cartType.Employee) {
				System.out.println("Total: " + 
						cu.myShoppingCarts.get(cartIndex).getTotalPurchasePrice() + 
						"\u20AC");
			} else {
				System.out.println("Total: " + 
						cu.myShoppingCarts.get(cartIndex).getTotalSellingPrice() + 
						"\u20AC");
			}
			// Highest Special 
			System.out.print("Höchste(r/s): ");
			if (highestSpecialProperty == Property.None) {
				System.out.println("- Eigenschaft nicht gesetzt-");
			} else if (highestSpecialProperty == Property.BestBy){
				if (cu.getHighestSpecialBestByDateIndex(cu.myShoppingCarts.get(cartIndex)) == -1) {
					System.out.println( cu.propertyToString(highestSpecialProperty)
							+ " -> nicht im Warenkorb vorhanden");
				} else {
					System.out.println( cu.propertyToString(highestSpecialProperty) 
						+ " -> Produkt nr."
						+ (cu.getHighestSpecialBestByDateIndex(cu.myShoppingCarts.get(cartIndex))+ 1));
				}
			} else if (highestSpecialProperty == Property.Recycling){
				if (cu.getHighestSpecialRecyclingIndex(cu.myShoppingCarts.get(cartIndex)) == -1) {
					System.out.println( cu.propertyToString(highestSpecialProperty)
							+ " -> nicht im Warenkorb vorhanden");
				} else {
					System.out.println( cu.propertyToString(highestSpecialProperty) 
						+ " -> Produkt nr."
						+ (cu.getHighestSpecialRecyclingIndex(cu.myShoppingCarts.get(cartIndex))+ 1));
				}
			} else if (highestSpecialProperty == Property.Restricted){
				if (cu.getHighestSpecialRestrictedIndex(cu.myShoppingCarts.get(cartIndex)) == -1) {
					System.out.println( cu.propertyToString(highestSpecialProperty)
							+ " -> nicht im Warenkorb vorhanden");
				} else {
					System.out.println( cu.propertyToString(highestSpecialProperty) 
						+ " -> Produkt nr."
						+ (cu.getHighestSpecialRestrictedIndex(cu.myShoppingCarts.get(cartIndex))+ 1));
				}
			}
			
			// Lowest Special
			System.out.print("Niedrigste(r/s): ");
			if (lowestSpecialProperty == Property.None) {
				System.out.println("-Eigenschaft nicht gesetzt-");
			} else if (lowestSpecialProperty == Property.BestBy){
				if (cu.getLowestSpecialBestByDateIndex(cu.myShoppingCarts.get(cartIndex)) == -1) {
					System.out.println( cu.propertyToString(lowestSpecialProperty)
							+ " -> nicht im Warenkorb vorhanden");
				} else {
					System.out.println( cu.propertyToString(lowestSpecialProperty) 
						+ " -> Produkt nr."
						+ (cu.getLowestSpecialBestByDateIndex(cu.myShoppingCarts.get(cartIndex))+ 1));
				}
			} else if (lowestSpecialProperty == Property.Recycling){
				if (cu.getLowestSpecialRecyclingIndex(cu.myShoppingCarts.get(cartIndex)) == -1) {
					System.out.println( cu.propertyToString(lowestSpecialProperty)
							+ " -> nicht im Warenkorb vorhanden");
				} else {
					System.out.println( cu.propertyToString(lowestSpecialProperty) 
						+ " -> Produkt nr."
						+ (cu.getLowestSpecialRecyclingIndex(cu.myShoppingCarts.get(cartIndex))+ 1));
				}
			} else if (lowestSpecialProperty == Property.Restricted){
				if (cu.getLowestSpecialRestrictedIndex(cu.myShoppingCarts.get(cartIndex)) == -1) {
					System.out.println( cu.propertyToString(lowestSpecialProperty)
							+ " -> nicht im Warenkorb vorhanden");
				} else {
					System.out.println( cu.propertyToString(lowestSpecialProperty) 
						+ " -> Produkt nr."
						+ (cu.getLowestSpecialRestrictedIndex(cu.myShoppingCarts.get(cartIndex))+ 1));
				}
			}
		} else {
			System.out.println("- Kein Einkaufswagen Vorhanden -");
		}
	}
	
	private String printCommandLine(Scanner sc) {
		System.out.println();
		printLine();
		System.out.println("    Legende| ':' entsprechender Befehl, '/' naechster Befehl, ',' oder, '...' u.s.w., '()' b.z.w.");
		switch (myState) {
			case Home:
				if (cu.myShoppingCarts.size() > 0) {
					if (cu.myShoppingCarts.get(currentShoppingCartIndex).getCartType() != cartType.Gift) {
						System.out.println("--- Befehle| Einkaufswagen Anlegen: ea / Einkaufswagen Wechseln: ew / Produkt Hinzufuegen: ph");
						System.out.println("--- Befehle| Produkt Löschen: 1,2,... / Bezahlen: b / Einkaufswagen Löschen: l");
						System.out.println("--- Befehle| Höchstes-(Niedrigstes) Haltbarkeitsdatum: hh (nh) / Höchster-(Niedrigster) Recycleantiel: hr (nr)");
						System.out.println("--- Befehle| Höchste-(Niedrigste) Altersbeschränkung: ha (na) / Programm beenden: beenden");
					} else {
						System.out.println("--- Befehle| Einkaufswagen Anlegen: ea / Einkaufswagen Wechseln: ew /");
						System.out.println("--- Befehle| Bezahlen: b / Einkaufswagen Löschen: l");
						System.out.println("--- Befehle| Höchstes-(Niedrigstes) Haltbarkeitsdatum: hh (nh) / Höchster-(Niedrigster) Recycleantiel: hr (nr)");
						System.out.println("--- Befehle| Höchste-(Niedrigste) Altersbeschränkung: ha (na) / Programm beenden: beenden");
					}
				} else {
					System.out.println("--- Befehle| Einkaufswagen Anlegen: ea /");
				}
				
				break;
			case AddItems:
				System.out.println("--- Befehle| Zurueck - Home: z / Produkt Waehlen: 1,2,...");
				break;
			case AddShoppingCart:
				System.out.println("--- Befehle| Zurueck - Home: z / Einkaufswagen Waehlen: 1,2,3,4");
				break;
			case ViewShoppingCarts:
				System.out.println("--- Befehle| Zurueck - Home: z / Einkaufswagen Waehlen: 1,2,... / Einkaufswagen Anlegen: ea");
				break;	
			case PasswordEmployees:
				System.out.println("--- Befehle| Bitte Passwort eingeben:");
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
					if (cu.myShoppingCarts.get(currentShoppingCartIndex).getCartType() != cartType.Gift) {
						myState = State.AddItems;
					}
					break;
				case "hh":
					highestSpecialProperty = Property.BestBy;
					break;
				case "nh":
					lowestSpecialProperty = Property.BestBy;
					break;
				case "ha":
					highestSpecialProperty = Property.Restricted;
					break;
				case "na":
					lowestSpecialProperty = Property.Restricted;
					break;
				case "hr":
					highestSpecialProperty = Property.Recycling;
					break;
				case "nr":
					lowestSpecialProperty = Property.Recycling;
					break;
				case "b":
					if (thisCartType == cartType.Employee) {
						cu.addToDaylyIncome(
								cu.myShoppingCarts.get(currentShoppingCartIndex)
								.getTotalPurchasePrice());
						cu.myShoppingCarts.remove(currentShoppingCartIndex);
					} else {
						cu.addToDaylyIncome(
								cu.myShoppingCarts.get(currentShoppingCartIndex)
								.getTotalSellingPrice());
						cu.myShoppingCarts.remove(currentShoppingCartIndex);
					}
					
					currentShoppingCartIndex = 0;
					myState = State.ViewShoppingCarts;
					break;
				case "l":
					cu.myShoppingCarts.remove(currentShoppingCartIndex);
					currentShoppingCartIndex = 0;
					myState = State.ViewShoppingCarts;
					break;
				case "beenden":
					running = false;
					break;
				case "z":
					myState = State.Home;
				default:
					//delete products
					if (isStrInt(input) && cu.myShoppingCarts.get(currentShoppingCartIndex).getCartType() != cartType.Gift) {
						int i = Integer.parseInt(input);
							if (cu.myShoppingCarts.size() > 0 && i > 0) {
								if (cu.myShoppingCarts.get(currentShoppingCartIndex).getMyProducts().size() >= i) {
								cu.myShoppingCarts.get(currentShoppingCartIndex).removeProductByIndex(i-1);
							}
						}
						
					}
					break;
			}
		} else if (myState == State.AddShoppingCart) {
			switch (input) {
				case "1":
					// Economy
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(new ShoppingCart(cartType.Economy));
					break;
				case "2":
					// Restricted
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(new ShoppingCart(cartType.Age));
					break;
				case "3":
					// Bio
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(new ShoppingCart(cartType.Bio));
					break;
				case "4":
					// Employee
					myState = State.PasswordEmployees;
					break;
				case "5":
					// Gift 10
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(new ShoppingCart(cartType.Gift));
					ArrayList<Product> products = new ArrayList<Product>(cu.getRandomProductList(10));
					for (int i = 0; i < products.size(); i++) {
						cu.myShoppingCarts.get(cu.myShoppingCarts.size()-1).addProduct(products.get(i));
					}
					break;
				case "6":
					// Gift 20
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(new ShoppingCart(cartType.Gift));
					ArrayList<Product> products1 = new ArrayList<Product>(cu.getRandomProductList(20));
					for (int i = 0; i < products1.size(); i++) {
						cu.myShoppingCarts.get(cu.myShoppingCarts.size()-1).addProduct(products1.get(i));
					}
					break;
				case "7":
					// Gift 50
					myState = State.ViewShoppingCarts;
					cu.addShoppingCart(new ShoppingCart(cartType.Gift));
					ArrayList<Product> products2 = new ArrayList<Product>(cu.getRandomProductList(50));
					for (int i = 0; i < products2.size(); i++) {
						cu.myShoppingCarts.get(cu.myShoppingCarts.size()-1).addProduct(products2.get(i));
					}
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
					// If there are shopping carts and if its not a gift cart
					if (cu.myShoppingCarts.size() > 0 
						&& cu.myShoppingCarts.get(currentShoppingCartIndex).getCartType() != cartType.Gift) {
						for (int i = 1; i <= cu.myWarehouse.getMyProducts(thisCartType).size(); i++) {
							if (isStrInt(input)) {
								if (Integer.parseInt(input) == i) {
									// If Economy ...
									if (cu.myShoppingCarts.get(currentShoppingCartIndex).getCartType() == cartType.Economy) {
										// ... and If less than 50
										if (cu.myShoppingCarts.get(currentShoppingCartIndex).getTotalSellingPrice()
												+ cu.myWarehouse.getMyProducts(thisCartType).get(i-1).getSellingPrice()
												<= 50) {
											// ADD PRODUCT
											cu.myShoppingCarts.get(currentShoppingCartIndex)
													.addProduct(cu.myWarehouse.getMyProducts(thisCartType).get(i-1));
										}
									}
									// or If not Economy
									else {
										// ADD PRODUCT
										cu.myShoppingCarts.get(currentShoppingCartIndex)
										.addProduct(cu.myWarehouse.getMyProducts(thisCartType).get(i-1));
									}
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
		} else if (myState == State.PasswordEmployees) {
			if (cu.PasswordEmployeesShoppingCart(input)) {
				cu.addShoppingCart(new ShoppingCart(cartType.Employee));
				myState = State.ViewShoppingCarts;
			}else {
				myState = State.AddShoppingCart;
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
