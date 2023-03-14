package com.example.application.views.pages;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;


import com.example.application.views.MainLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import control.ControlUnit;
import objects.GeneralItems;
import objects.Groceries;
import objects.HouseholdItems;
import objects.Product;
import objects.ShoppingCart;
import objects.ShoppingCart.cartType;
import objects.Warehouse;
import com.example.application.views.main.*;

import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;




@PageTitle("Goosemart")
@Route(value = "shoppingcart", layout=MainLayout.class)
public class ShoppingCartView extends VerticalLayout {

	private ShoppingCart currentShoppingCart;
	private ControlUnit controlUnit = ControlUnit.getInstance();
	private Warehouse warehouse = new Warehouse();
	private int counter = 2;
	private String currentSelection = "";
	private String currentSelectionCharc = "";
	private ArrayList<ShoppingCart> shoppingcars = controlUnit.myShoppingCarts;
	private ShoppingCart shoppingCart1 = new ShoppingCart(cartType.None);
	private Product selectedProduct = null;
	private ArrayList<Product> allProductsInWarehouse = controlUnit.getProducts();
	private Product addingProduct;
	private Button buyButton;
	private Span dailyRevenue = new Span();
	private  Tabs tabs;

	ArrayList<Product> prod;
	
	
    public ShoppingCartView() {
    	
    	shoppingCart1.setName("Warenkorb1");
    	currentShoppingCart = shoppingCart1;
    	controlUnit.addShoppingCart(shoppingCart1);
    	shoppingcars.add(shoppingCart1);
    	
    	Tab shoppingcart1 = new Tab("Warenkorb1");
        shoppingcart1.setId("Warenkorb1");
        
        tabs = new Tabs(shoppingcart1);
 //Komponenten 

        
        //Logo
        
        //Image logo=new Image("src/main/resources/META-INF/resources/images/Goosemart.png","something");
        //var layoutShoppingCart11 = new VerticalLayout(logo);
        
        
        
        // Produkte im Warenkorb (Tabelle)   
            
        	Grid<Product> grid = new Grid<>(Product.class, false);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            
            grid.addColumn(Product::getProductDesignation).setHeader("Produkt")
            		.setWidth("15em").setFlexGrow(0);
            grid.addColumn(Product::getProductId).setHeader("Produktnr")
            		.setWidth("37em").setFlexGrow(0).setKey("Produktnr");
            grid.addColumn(Product::isAgeRestriction).setHeader("Weitere Eigenschaften")
            		.setWidth("27em").setKey("Weitere Eigenschaften").setFlexGrow(0).setVisible(false);
            grid.addColumn(Product::getPurchasePrice).setHeader("Einkaufspreis").setKey("Einkaufspreis")
            		.setWidth("10em").setVisible(false);
            grid.addColumn(Product::getSellingPrice).setHeader("Preis")
            		.setWidth("6em").setFlexGrow(0);
            
            
            ArrayList<Product> products = currentShoppingCart.getMyProducts();
            grid.setItems(products);
            grid.setWidth("100%");
        
        
        //Warenkorb Name
        
           
        Span scName = new Span(); 
        scName.setText(currentShoppingCart.getName());
        
      //Kauf-Button
        
        Button buyButton = new Button("Bezahlen "+currentShoppingCart.getTotalSellingPrice(),
                event -> {
                	controlUnit.addToDaylyIncome(currentShoppingCart.getTotalPurchasePrice());
                	shoppingcars.remove(currentShoppingCart);
                	
                	Tab toDeleteTab = tabs.getSelectedTab();
                	tabs.remove(toDeleteTab);
                	
                	int i = 0;
                	for(ShoppingCart s:controlUnit.myShoppingCarts) {
                		if(i == 0) {
                			currentShoppingCart = s;
                			grid.getDataProvider().refreshAll();
                    		prod = currentShoppingCart.getMyProducts();
                            grid.setItems(prod);
                		}
                		
                	}
                	
                	
                    dailyRevenue.setText("Tageseinkommen: " + controlUnit.getDaylyIncome());
                    
                	
                });
        buyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
    	//MenuBar Produkte einfügen + Pop Up
        ConfirmDialog warnung = new ConfirmDialog();
        warnung.setConfirmText("OK");
		warnung.setHeader("Achtung!");
        

        //Spar-Korb noch machen
    	MenuBar menuBar = new MenuBar();

        MenuItem groceriesMenu = menuBar.addItem("Lebensmittel");
        SubMenu groceriesSubMenu = groceriesMenu.getSubMenu();
        groceriesSubMenu.addItem("Mineralwasser", 
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift) {
	        			for(Product allP: allProductsInWarehouse) {
	        				if(allP.getProductDesignation().equals("Mineralwasser")) {
	        					addingProduct = allP;
	        				}
	        			}
	        			currentShoppingCart.addProduct(addingProduct);
	        			prod = currentShoppingCart.getMyProducts();
	                    grid.setItems(prod);
	                    if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        groceriesSubMenu.addItem("Toastbrot",
        (ComponentEventListener<ClickEvent<MenuItem>>) event -> {
			if(currentShoppingCart.getCartType() != cartType.Gift) {
    			for(Product allP: allProductsInWarehouse) {
    				if(allP.getProductDesignation().equals("Toastbrot")) {
    					addingProduct = allP;
    				}
    			}
    			currentShoppingCart.addProduct(addingProduct);
    			prod = currentShoppingCart.getMyProducts();
                grid.setItems(prod);
                if(currentShoppingCart.getCartType() != cartType.Employee) {
                	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
                }
                else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
                }
			}
			else {
				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
				warnung.open();
			}
		});
        groceriesSubMenu.addItem("Butter",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Butter")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        groceriesSubMenu.addItem("Wurst",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift &&currentShoppingCart.getCartType() != cartType.Bio) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Wurst")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        groceriesSubMenu.addItem("Käse",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Käse")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        groceriesSubMenu.addItem("Flasche Wein",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift && currentShoppingCart.getCartType() != cartType.Age) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Flasche Wein")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        

        MenuItem householdMenu = menuBar.addItem("Haushaltsartikel");
        SubMenu householdSubMenu = householdMenu.getSubMenu();
        householdSubMenu.addItem("Klobürste",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift &&currentShoppingCart.getCartType() != cartType.Age) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Klobürste")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        householdSubMenu.addItem("Plastikbesteck",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift &&currentShoppingCart.getCartType() != cartType.Bio) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Plastikbesteck")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});
        householdSubMenu.addItem("Putzlappen",
		        (ComponentEventListener<ClickEvent<MenuItem>>) event -> {
					if(currentShoppingCart.getCartType() != cartType.Gift) {
		    			for(Product allP: allProductsInWarehouse) {
		    				if(allP.getProductDesignation().equals("Putzlappen")) {
		    					addingProduct = allP;
		    				}
		    			}
		    			currentShoppingCart.addProduct(addingProduct);
		    			prod = currentShoppingCart.getMyProducts();
		                grid.setItems(prod);
		                if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
					}
					else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
				});
        householdSubMenu.addItem("Zahncreme",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
        			if(currentShoppingCart.getCartType() != cartType.Gift) {
            			for(Product allP: allProductsInWarehouse) {
            				if(allP.getProductDesignation().equals("Zahncreme")) {
            					addingProduct = allP;
            				}
            			}
            			currentShoppingCart.addProduct(addingProduct);
            			prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
        			}
        			else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
        		});

        MenuItem generalMenu = menuBar.addItem("Sonstige");
        SubMenu generalSubMenu = generalMenu.getSubMenu();
        generalSubMenu.addItem("DVD Actionfilm",
		        (ComponentEventListener<ClickEvent<MenuItem>>) event -> {
					if(currentShoppingCart.getCartType() != cartType.Gift && currentShoppingCart.getCartType() != cartType.Age) {
		    			for(Product allP: allProductsInWarehouse) {
		    				if(allP.getProductDesignation().equals("DVD Actionfilm")) {
		    					addingProduct = allP;
		    				}
		    			}
		    			currentShoppingCart.addProduct(addingProduct);
		    			prod = currentShoppingCart.getMyProducts();
		                grid.setItems(prod);
		                if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
					}
					else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
				});
        generalSubMenu.addItem("DVD Familienfilm",
        		(ComponentEventListener<ClickEvent<MenuItem>>) event -> {
					if(currentShoppingCart.getCartType() != cartType.Gift) {
		    			for(Product allP: allProductsInWarehouse) {
		    				if(allP.getProductDesignation().equals("DVD Familienfilm")) {
		    					addingProduct = allP;
		    				}
		    			}
		    			currentShoppingCart.addProduct(addingProduct);
		    			prod = currentShoppingCart.getMyProducts();
		                grid.setItems(prod);
		                if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
					}
					else {
        				warnung.setText("Sie können dieses Produkt nicht auswählen. Ihr Warenkorb unterstützt dieses Produkt nicht!");
        				warnung.open();
        			}
				});

        add(menuBar);
 
 //Alles zu Warenkörben
        
        
        //Warenkorb Filter + Geschenkoption zusammengefügt
        
        Select<String> filterSc = new Select<>();
        filterSc.setLabel("Warenkorb Filter");
        filterSc.setItems("Keine", "Öko-Prinzip", "U18",
        		"Spar-Korb", "Mitarbeiterkaufprogramm", "Geschenkoption 10€",
                "Geschenkoption 20€", "Geschenkoption 50€" );
        

        filterSc.addValueChangeListener(event -> {
            currentSelection= event.getValue();
        });
            
        //Warenkörbe-Auswahl
    	
        
        
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setHeight("240px");
        tabs.setWidth("240px");
        
        tabs.addSelectedChangeListener(
                event -> {
                	Tab currentTab = tabs.getSelectedTab();
                	
                	
                	if(currentTab == shoppingcart1) {
                		currentShoppingCart = shoppingCart1;
                		grid.getDataProvider().refreshAll();
                		prod = currentShoppingCart.getMyProducts();
                        grid.setItems(prod);
                        
                        if(currentShoppingCart.getCartType() != cartType.Employee) {
	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
	                    }
	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
	                    }
                		
                	}
                	for(ShoppingCart s: shoppingcars) {
						String[] splittedTab = currentTab.getLabel().split("-");
                		if(s.getName().equals(splittedTab[0])) {
                			scName.setText(currentTab.getLabel());
                			currentShoppingCart = s;
                			grid.getDataProvider().refreshAll(); 
                			prod = currentShoppingCart.getMyProducts();
                			grid.setItems(prod);
                            scName.setText(currentTab.getLabel()); 
                            
                            if(currentShoppingCart.getCartType() != cartType.Employee) {
    	                    	buyButton.setText("Bezahlen "+currentShoppingCart.getTotalSellingPrice());
    	                    }
    	                    else {buyButton.setText("Bezahlen "+currentShoppingCart.getTotalPurchasePrice());
    	                    }
   
                		}
                	}
                	if(currentShoppingCart.getCartType()== cartType.Employee) {
                        grid.getColumnByKey("Einkaufspreis").setVisible(true);
                        grid.getColumnByKey("Produktnr").setWidth("10em"); 
                        grid.getColumnByKey("Weitere Eigenschaften").setWidth("17em"); 
                    }
                }
            );
        
         
        
        
        //Neuen Warenkorb erstellen
         
        
        Button newShoppingCart = new Button("Neuer Warenkorb",
        		event -> { 
        			if(currentSelection =="Keine") {
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.None);
        				shoppingcartNew.setName("Warenkorb"+counter);
        				controlUnit.addShoppingCart(shoppingcartNew);
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter);
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
        				counter++;
        			}
        			if(currentSelection == "Öko-Prinzip") {
            			ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Bio);
						shoppingcartNew.setName("Warenkorb"+counter);
            			controlUnit.addShoppingCart(shoppingcartNew);
            			Tab shoppingcartTag = new Tab("Warenkorb"+counter + "-Öko");
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
        				counter++;
            		}
        			if(currentSelection == "U18") {
            			ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Age);
						shoppingcartNew.setName("Warenkorb"+counter);
            			controlUnit.addShoppingCart(shoppingcartNew);
            			Tab shoppingcartTag = new Tab("Warenkorb"+counter +"-U18");
        				tabs.add(shoppingcartTag);
        				counter++;
            		}
        			if(currentSelection == "Spar-Korb") {
            			ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Economy);
            			shoppingcartNew.setName("Warenkorb"+counter);
						controlUnit.addShoppingCart(shoppingcartNew);
            			Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-Spar");
        				tabs.add(shoppingcartTag);
        				counter++;
            		}
        			if(currentSelection == "Mitarbeiterkaufprogramm") {
            			ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Employee);
            			shoppingcartNew.setName("Warenkorb"+counter);
						controlUnit.addShoppingCart(shoppingcartNew);
            			Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-Mitarb.");
        				tabs.add(shoppingcartTag);
        				counter++;
            		}
        			if(currentSelection == "Geschenkoption 10€") {
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Gift);
        				shoppingcartNew.setName("Warenkorb"+counter);
        				controlUnit.addShoppingCart(shoppingcartNew);
        				
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-G10");
        				shoppingcartTag.setId("Warenkorb"+counter);
        				tabs.add(shoppingcartTag);
        				ShoppingCart a = currentShoppingCart;
        				currentShoppingCart = shoppingcartNew;
        				ArrayList <Product> randomProducts = controlUnit.getRandomProductList(10);
        				for(Product p:randomProducts) {
        					currentShoppingCart.addProduct(p);
        				}
        				currentShoppingCart = a;
        				counter++;
            		}
        			if(currentSelection == "Geschenkoption 20€") {
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Gift);
        				shoppingcartNew.setName("Warenkorb"+counter);
						controlUnit.addShoppingCart(shoppingcartNew);
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-G20");
        				tabs.add(shoppingcartTag);
        				ShoppingCart a = currentShoppingCart;
        				currentShoppingCart = shoppingcartNew;
        				ArrayList <Product> randomProducts = controlUnit.getRandomProductList(20);
        				for(Product p:randomProducts) {
        					currentShoppingCart.addProduct(p);
        				}
        				currentShoppingCart = a;
        				counter++;
            		}
        			if(currentSelection == "Geschenkoption 50€") {
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Gift);
        				shoppingcartNew.setName("Warenkorb"+counter);
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-G50");
        				tabs.add(shoppingcartTag);
        				controlUnit.addShoppingCart(shoppingcartNew);
        				ShoppingCart a = currentShoppingCart;
        				currentShoppingCart = shoppingcartNew;
        				ArrayList <Product> randomProducts = controlUnit.getRandomProductList(50);
        				for(Product p:randomProducts) {
        					currentShoppingCart.addProduct(p);
        				}
        				currentShoppingCart = a;
        				counter++;
            		}	
        			
        		});
        
        newShoppingCart.setWidth("12em");

        
        //Sortieren nach "Weiteren Eigenschaften"
        
        ConfirmDialog dialogProduct = new ConfirmDialog();
        dialogProduct.setConfirmText("OK");
        

            Select<String> select = new Select<>();
            select.setLabel("Eigenschaften");
            select.setItems("längstes Mindesthaltbarkeitsdatum", "kürzestes Mindesthaltbarkeitsdatum",
                    "geringster Recycling-Anteil", "höchster Recycling-Anteil",
                    "FSK 18", "FSK frei");
            
            //Pop-Up zur Sortierung     //Funktioniert noch nicht
 //               select.getElement().addEventListener("click", event ->{
//                	if(select.getIte) {
//                		
//                	}
//                	dialogProduct.open();});
                
                select.addValueChangeListener(event -> {
                    currentSelectionCharc= event.getValue();
                    String prodText = "";
                    if(currentSelectionCharc.equals("längstes Mindesthaltbarkeitsdatum")) {
                    	
                    	int i = controlUnit.getHighestSpecialBestByDateIndex(currentShoppingCart);
                    	int index = 0;
                    	
                    			for(Product p:currentShoppingCart.getMyProducts()) {
                    				if(index ==i) {
                    					prodText = p.getProductDesignation();
                    					break;
                    				}
                    				index++;
                    			}
                    	
                    	dialogProduct.setHeader("Längstes Mindesthaltbarkeitsdatum");
                        dialogProduct.setText(prodText);
                    	dialogProduct.open();
                    }
                });

       
     
            
            
            //Weitere Eigenschaften einsehen - Button
            
        
            Button characteristicButton = new Button("Weitere Eigenschaften",
                    event -> {grid.getColumnByKey("Weitere Eigenschaften").setVisible(true);
                    grid.getColumnByKey("Produktnr").setWidth("10em"); });
            
            
            
           //Produkt aus Warenkorb entfernen 
            
            
            grid.addItemClickListener(event -> {
	         	   selectedProduct = event.getItem();
	         	  
            });
            
            Button deleteButton = new Button("Auswahl entfernen",
                    event -> {
                    	
                    	//Eventuell Pop Up, dass man nichts öndern kann beim Geschenk
                    	if(currentShoppingCart.getCartType()!=cartType.Gift) {
	                    currentShoppingCart.removeProductByObject(selectedProduct);
	      	         	ArrayList<Product>refreshProd = currentShoppingCart.getMyProducts();
	      	         	grid.setItems(refreshProd);
                    	}
                    });
                    
            //Tageneinnahme-Anzeige 
            dailyRevenue.setText("Tageseinkommen: " + controlUnit.getDaylyIncome());        
            

            //Layout 
            
            
            var layoutShoppingCart0 = new HorizontalLayout(menuBar);
            add(layoutShoppingCart0);
            
            add(scName);
            
            var layoutShoppingCart = new HorizontalLayout(grid, tabs);
            layoutShoppingCart.setWidthFull();
            tabs.getStyle().set("margin-left", "auto");
            
            var layoutShoppingCart2 = new HorizontalLayout(select,deleteButton, buyButton, filterSc);
            layoutShoppingCart2.setAlignSelf(Alignment.END, buyButton, deleteButton);
            filterSc.getStyle().set("margin-left", "auto");
            layoutShoppingCart2.setWidthFull();
            add(layoutShoppingCart,layoutShoppingCart2);
            
            newShoppingCart.getStyle().set("margin-left", "auto");
            var layoutShoppingCart3 = new HorizontalLayout(characteristicButton, newShoppingCart);
            layoutShoppingCart3.setWidthFull();
            
            add(layoutShoppingCart3); 
            
            
            dailyRevenue.getStyle().set("margin-left", "auto");
            add(dailyRevenue);
    }
    
    

}
