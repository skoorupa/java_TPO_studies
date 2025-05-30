package com.example.tpo4.views;

import com.example.tpo4.models.Osoba;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.Date;

@Route("/add")
@PageTitle("Dodaj nową osobę")
public class AddNewView extends VerticalLayout {
    private final WebClient webClient = WebClient.create("http://localhost:8080/");

    private final TextField imieField = new TextField("Imię");
    private final TextField nazwiskoField = new TextField("Nazwisko");
    private final DatePicker dataUrodzeniaPicker = new DatePicker("Data urodzenia");
    private final TextField numerTelefonuField = new TextField("Numer telefonu");

    private final Button saveButton = new Button("Dodaj");
    private final Button cancelButton = new Button("Anuluj");

    public AddNewView() {
        add(new H2("Dodaj nową osobę"));

        imieField.setRequired(true);
        nazwiskoField.setRequired(true);
        numerTelefonuField.setRequired(true);

        add(imieField, nazwiskoField, dataUrodzeniaPicker, numerTelefonuField,
                new HorizontalLayout(saveButton, cancelButton));

        saveButton.addClickListener(e -> addOsoba());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(""));
    }

    private void addOsoba() {
        if (imieField.isEmpty() || nazwiskoField.isEmpty() || numerTelefonuField.isEmpty()) {
            Notification.show("Wszystkie pola są wymagane.");
            return;
        }

        Osoba osoba = new Osoba();
        osoba.setImie(imieField.getValue());
        osoba.setNazwisko(nazwiskoField.getValue());
        if (dataUrodzeniaPicker.getValue() != null)
            osoba.setData_urodzenia(Date.valueOf(dataUrodzeniaPicker.getValue()));
        osoba.setNr_telefonu(numerTelefonuField.getValue());

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
