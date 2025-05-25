package com.example.tpo4.views;

import com.example.tpo4.Osoba;
import com.example.tpo4.OsobaDAO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.Date;
import java.util.List;

@Route("/add")
@PageTitle("Dodaj nową osobę")
public class AddNewView extends VerticalLayout {
    private final WebClient webClient = WebClient.create("http://localhost:8080/");

    private final NumberField idField = new NumberField("ID");
    private final TextField imieField = new TextField("Imię");
    private final TextField nazwiskoField = new TextField("Nazwisko");
    private final DatePicker dataUrodzeniaPicker = new DatePicker("Data urodzenia");

    private final Button saveButton = new Button("Dodaj");
    private final Button cancelButton = new Button("Anuluj");

    public AddNewView() {
        add(new H2("Dodaj nową osobę"));

        idField.setRequired(true);
        imieField.setRequired(true);
        nazwiskoField.setRequired(true);
        dataUrodzeniaPicker.setRequired(true);

        add(idField, imieField, nazwiskoField, dataUrodzeniaPicker,
                new HorizontalLayout(saveButton, cancelButton));

        saveButton.addClickListener(e -> addOsoba());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(""));
    }

    private void addOsoba() {
        if (idField.isEmpty() || imieField.isEmpty() || nazwiskoField.isEmpty() || dataUrodzeniaPicker.isEmpty()) {
            Notification.show("Wszystkie pola są wymagane.");
            return;
        }

        Osoba osoba = new Osoba();
        osoba.setId(idField.getValue().intValue());
        osoba.setImie(imieField.getValue());
        osoba.setNazwisko(nazwiskoField.getValue());
        osoba.setData_urodzenia(Date.valueOf(dataUrodzeniaPicker.getValue()));

        try {
            Osoba wynik = webClient.post()
                    .uri("/data")
                    .bodyValue(osoba)
                    .retrieve()
                    .bodyToMono(Osoba.class)
                    .block();

            Notification.show("Dodano osobę ID: " + wynik.getId());
            UI.getCurrent().navigate("");
        } catch (WebClientResponseException e) {
            Notification.show("Błąd: " + e.getStatusCode() + " – " + e.getResponseBodyAsString());
        }
    }
}
