package com.example.application.views;

import com.example.application.views.main.MainView;
import com.example.application.views.pages.ShoppingCartView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;


public class MainLayout extends AppLayout { 

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("GOOSEmart");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
          new DrawerToggle(), 
          logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); 
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header); 

    }
    
    private void createDrawer() {
        RouterLink mainLink = new RouterLink("Produkte", MainView.class); 
        mainLink.setHighlightCondition(HighlightConditions.sameLocation()); 
        RouterLink warenkorb = new RouterLink("Warenkorb", ShoppingCartView.class); 
     
        addToDrawer(new VerticalLayout( 
            mainLink,
            warenkorb
        ));
    }
}