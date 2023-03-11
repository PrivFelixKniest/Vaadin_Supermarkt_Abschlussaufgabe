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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

//import logo from '/src/main/resources/META-INF/resources/images/Goosemart.png';



@PageTitle("Goosemart")
@Route(value = "shoppingcart", layout=MainLayout.class)
public class ShoppingCartView extends VerticalLayout {

	private ShoppingCart currentShoppingCart;
	private MainView mv = new MainView();
	private ControlUnit controlUnit = mv.getControlUnit();
	private Warehouse warehouse = new Warehouse();
	private int counter = 2;
	String currentSelection = "";
	Grid<Product> grid = new Grid<>(Product.class, false);
	ArrayList<ShoppingCart> shoppingcars = controlUnit.myShoppingCarts;
	ArrayList<Product> selectedItems;
	
	
    public ShoppingCartView() {
    	
    	ArrayList<Product> list = new ArrayList<Product>();
    	ShoppingCart shoppingCart1 = new ShoppingCart(cartType.None);
    	shoppingCart1.setName("Warenkorb1");
    	currentShoppingCart = shoppingCart1;
    	controlUnit.addShoppingCart(shoppingCart1);
    	shoppingcars.add(shoppingCart1);
    

    	
   //Komponenten 
    	//MenuBar

    	MenuBar menuBar = new MenuBar();

        MenuItem groceriesMenu = menuBar.addItem("Lebensmittel");
        SubMenu groceriesSubMenu = groceriesMenu.getSubMenu();
        groceriesSubMenu.addItem("Mineralwasser");
        groceriesSubMenu.addItem("Toastbrot");
        groceriesSubMenu.addItem("Butter");
        groceriesSubMenu.addItem("Wurst");
        groceriesSubMenu.addItem("Käse");
        groceriesSubMenu.addItem("Flasche Wein");

        MenuItem householdMenu = menuBar.addItem("Haushaltsartikel");
        SubMenu householdSubMenu = householdMenu.getSubMenu();
        householdSubMenu.addItem("Klobürste");
        householdSubMenu.addItem("Plastikbesteck");
        householdSubMenu.addItem("Putzlappen");
        householdSubMenu.addItem("Zahncreme");

        MenuItem generalMenu = menuBar.addItem("Sonstige");
        SubMenu generalSubMenu = generalMenu.getSubMenu();
        generalSubMenu.addItem("DVD Actionfilm");
        generalSubMenu.addItem("DVD Familienfilm");

        add(menuBar);
        
        //Logo
        
        //Image logo=new Image("src/main/resources/META-INF/resources/images/Goosemart.png","something");
        //var layoutShoppingCart11 = new VerticalLayout(logo);
        
        
        
        // Produkte im Warenkorb (Tabelle)   
            
            
            grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            grid.addColumn(Product::getProductDesignation).setHeader("Produkt")
            		.setWidth("10em").setFlexGrow(0);
            grid.addColumn(Product::getProductId).setHeader("Produktnr")
            		.setWidth("37em").setFlexGrow(0).setKey("Produktnr");
            grid.addColumn(Product::isAgeRestriction).setHeader("Weitere Eigenschaften")
            		.setWidth("27em").setKey("Weitere Eigenschaften").setFlexGrow(0).setVisible(false);
           // grid.addColumn(Product::getPurchasePrice).setHeader("Einkaufspreis");
            grid.addColumn(Product::getSellingPrice).setHeader("Preis")
            		.setWidth("6em").setFlexGrow(0);
            
            
            ArrayList<Product> products = currentShoppingCart.getMyProducts();
            grid.setItems(products);
            grid.setWidth("100%");
        
        
        //Warenkorb Name
        
           
        Span scName = new Span(); 
        scName.setText(currentShoppingCart.getName());
 
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
    	
        
        Tab shoppingcart1 = new Tab("Warenkorb1");
        shoppingcart1.setId("Warenkorb1");
        
        Tabs tabs = new Tabs(shoppingcart1);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setHeight("240px");
        tabs.setWidth("240px");
        
        tabs.addSelectedChangeListener(
                event -> {
                	Tab currentTab = tabs.getSelectedTab();
                	
                	
                	if(currentTab == shoppingcart1) {
                		currentShoppingCart = shoppingCart1;
                		grid.getDataProvider().refreshAll();
                		 //ArrayList<Product> prod = currentShoppingCart.getMyProducts();
                         //grid.setItems(prod);
                		
                	}
                	for(ShoppingCart s: shoppingcars) {
						String[] splittedTab = currentTab.getLabel().split("-");
                		if(s.getName().equals(splittedTab[0])) {
                			scName.setText(currentTab.getLabel());
                			currentShoppingCart = s;
                			grid.getDataProvider().refreshAll(); 
                			ArrayList<Product> prod = currentShoppingCart.getMyProducts();
                            grid.setItems(prod);
                            scName.setText(currentTab.getLabel());
                            
                           
                            
                            
                		}
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
        				//shoppingcars.add(shoppingcartNew);
        				counter++;
            		}
        			if(currentSelection == "Spar-Korb") {
            			ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Economy);
            			shoppingcartNew.setName("Warenkorb"+counter);
						controlUnit.addShoppingCart(shoppingcartNew);
            			Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-Spar");
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
        				counter++;
            		}
        			if(currentSelection == "Mitarbeiterkaufprogramm") {
            			ShoppingCart shoppingcartNew = new ShoppingCart(cartType.Employee);
            			shoppingcartNew.setName("Warenkorb"+counter);
						controlUnit.addShoppingCart(shoppingcartNew);
            			Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-Mitarb.");
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
            		}
        			if(currentSelection == "Geschenkoption 10€") {
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.None);
        				shoppingcartNew.setName("Warenkorb"+counter);
        				controlUnit.addShoppingCart(shoppingcartNew);
        				
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-G10");
        				shoppingcartTag.setId("Warenkorb"+counter);
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
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
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.None);
        				shoppingcartNew.setName("Warenkorb"+counter);
						controlUnit.addShoppingCart(shoppingcartNew);
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-G20");
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
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
        				ShoppingCart shoppingcartNew = new ShoppingCart(cartType.None);
        				shoppingcartNew.setName("Warenkorb"+counter);
        				Tab shoppingcartTag = new Tab("Warenkorb"+counter+ "-G50");
        				tabs.add(shoppingcartTag);
        				//shoppingcars.add(shoppingcartNew);
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
        dialogProduct.setHeader("überschrift");
        dialogProduct.setText("hbk");

        dialogProduct.setConfirmText("OK");
        

            Select<String> select = new Select<>();
            select.setLabel("Eigenschaften");
            select.setItems("längstes Mindesthaltbarkeitsdatum", "kürzestes Mindesthaltbarkeitsdatum",
                    "geringster Recycling-Anteil", "höchster Recycling-Anteil",
                    "FSK 18", "FSK frei");
            
            //Pop-Up zur Sortierung     //Funktioniert noch nicht
                select.getElement().addEventListener("längstes Mindesthaltbarkeitsdatum", 
                	    event -> dialogProduct.open());
                	
                

            
       
            
            
            //Weitere Eigenschaften einsehen - Button
            
        
            Button characteristicButton = new Button("Weitere Eigenschaften",
                    event -> {grid.getColumnByKey("Weitere Eigenschaften").setVisible(true);
                    grid.getColumnByKey("Produktnr").setWidth("10em"); });
            
            
            
           //Produkt aus Warenkorb entfernen 
            
            grid.addSelectionListener(event -> {
         	   Set<Product> selected = event.getAllSelectedItems();
         	   selectedItems = (ArrayList<Product>) selected;
            });
            
            Button deleteButton = new Button("Auswahl entfernen",
                    event -> {
                    	for(int i = 0; i<selectedItems.size();i++) {
                    		currentShoppingCart.removeProductByIndex(i);
                    	}
                    });
                    
                    
            //Kauf-Button
            
            Button buyButton = new Button("Bezahlen "+currentShoppingCart.getTotalSellingPrice(),
                    event -> {
                    	controlUnit.addToDaylyIncome(currentShoppingCart.getTotalPurchasePrice());
                    	shoppingcars.remove(currentShoppingCart);
                    	currentShoppingCart = null;
                    	//Tab entfernen
                    	
                    });
            buyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            
            
            //Tageneinnahme-Anzeige 
            
            Span dailyRevenue = new Span(); 
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

           // add(layoutShoppingCart11);
            
            
    }

}
