package com.example.application.views.pages;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Goosemart")
@Route(value = "newpage", layout=MainLayout.class)
public class NewPage extends HorizontalLayout {

    private static final long serialVersionUID = 1L;
	private TextField name;
    private Button sayHello;

    public NewPage() {
        name = new TextField("Zweite Seite");
        sayHello = new Button("Sage etwas");
        sayHello.addClickListener(e -> {
            Notification.show("Etwas " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
    }

}
