package com.example.application.views.main;

import com.example.application.views.MainLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import control.ControlUnit;
import objects.Product;
import objects.ShoppingCart;
import objects.ShoppingCart.cartType;
import objects.Warehouse;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;


import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;




@PageTitle("Goosemart")
@Route(value = "", layout=MainLayout.class)
public class MainView extends VerticalLayout{
    
	private ShoppingCart currentShoppingCart;
    private ControlUnit controlUnit = ControlUnit.getInstance();
    ArrayList<ShoppingCart> shoppingcars = controlUnit.myShoppingCarts;
    
    public ControlUnit getControlUnit() {
    	return controlUnit;
    }
    
    public MainView() {
    	 
    	
    	
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
        
        
        //Überschrift für die Produkttabelle
    	
        Span productHeader = new Span(); 
        productHeader.setText("Alle Produkte:");
        
    	//Warenkörbe
    	
        
        Tab shoppingcart1 = new Tab("Warenkorb1");
        Tab shoppingcart2 = new Tab("Warenkorb2");
        
        Tabs tabs = new Tabs(shoppingcart1, shoppingcart2);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setHeight("240px");
        tabs.setWidth("240px");
         
       
        
        
        // Alle Produkte (Tabelle)
        
        
        Grid<Product> grid = new Grid<>(Product.class, false);
        grid.addColumn(Product::getProductDesignation).setHeader("Produkt");
        grid.addColumn(Product::getSellingPrice).setHeader("Preis €");

        grid.setItemDetailsRenderer(createPersonDetailsRenderer());

        List<Product> products = controlUnit.getProducts();
        grid.setItems(products);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        
        
        //Warenkorb Filter + Geschenkoption zusammengefügt
        
        
        Select<String> filterSc = new Select<>();
        filterSc.setLabel("Warenkorb Filter");
        filterSc.setItems("Öko-Prinzip", "U18",
                "Spar-Korb", "höchster Recycling-Anteil",
                "FSK 18", "Mitarbeiterkaufprogramm", "Geschenkoption 10€", "Geschenkoption 20€", "Geschenkoption 50€" );

        
        
        //Neuen Warenkorb erstellen
        
        Button newShoppingCart = new Button("Neuer Warenkorb");
        newShoppingCart.setWidth("12em");
        
        
        //Tageneinnahme-Anzeige 
        
        Span dailyRevenue = new Span(); 
        dailyRevenue.setText("Tageseinkommen: " + controlUnit.getDaylyIncome()+"€");
        
        
        //Layout
        var productLayout = new HorizontalLayout(menuBar);
        add(productLayout);
        
        add(productHeader);
        
        var productLayout1 = new HorizontalLayout(grid, tabs);
        productLayout1.setWidthFull();
        tabs.getStyle().set("margin-left", "auto");
        add(productLayout1); 
        
        var productLayout2 = new HorizontalLayout(filterSc);
        filterSc.getStyle().set("margin-left", "auto");
        productLayout2.setWidthFull();
        add(productLayout2);
        
        newShoppingCart.getStyle().set("margin-left", "auto");
        var layoutShoppingCart3 = new HorizontalLayout(newShoppingCart);
        layoutShoppingCart3.setWidthFull();
        add(layoutShoppingCart3); 
        
        dailyRevenue.getStyle().set("margin-left", "auto");
        add(dailyRevenue);
        

    
    }
    //Aufklappfenster der einzelnen Produkten
    private static ComponentRenderer<ProductDetailsFormLayout, Product> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(ProductDetailsFormLayout::new,
        		ProductDetailsFormLayout::setProduct);
    }

    private static class ProductDetailsFormLayout extends FormLayout {
        private final TextField productID = new TextField("Produktnr.");
        private final TextField purchasePrice = new TextField("Einkaufspreis");
        
        //private ShoppingCart sc;
        //private ControlUnit cu;
       // ArrayList<ShoppingCart> shoppingcars = cu.myShoppingCarts;
      //  private ShoppingCart currentShoppingCart =new ShoppingCart(cartType.None);
        
       // private final TextField characteristics = new TextField("ZIP code");
        
        Button addToScButton = new Button("Zum Warenkorb hinzufügen",
        		event -> {
        			//currentShoppingCart.addProduct(null); // --> Denkfehler. Übertragung vom augewählten Objekt auf diese Klasse
        		});
       

        public ProductDetailsFormLayout() {
            Stream.of(productID, purchasePrice).forEach(field -> {
                        field.setReadOnly(true);
                        add(field, addToScButton);
                    });

            //setResponsiveSteps(new ResponsiveStep("0", 3));
           // setColspan(emailField, 3);
           // setColspan(phoneField, 3);
            //setColspan(streetField, 3);
        }

        public void setProduct(Product product) {
        	String pp =String.valueOf(product.getPurchasePrice());
        	
            productID.setValue(product.getProductId());
            purchasePrice.setValue(pp);
           
        }
    
    } 
}
