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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Route("/edit/:id")
@PageTitle("Dodaj nową osobę")
public class EditView extends VerticalLayout implements BeforeEnterObserver {
    private final WebClient webClient = WebClient.create("http://localhost:8080/");

    private final NumberField idField = new NumberField("ID");
    private final TextField imieField = new TextField("Imię");
    private final TextField nazwiskoField = new TextField("Nazwisko");
    private final DatePicker dataUrodzeniaPicker = new DatePicker("Data urodzenia");
    private final TextField numerTelefonuField = new TextField("Numer telefonu");

    private final Button saveButton = new Button("Zapisz");
    private final Button cancelButton = new Button("Anuluj");

    private int id;

    public EditView() {
        add(new H2("Edytuj osobę"));

        idField.setRequired(true);
        imieField.setRequired(true);
        nazwiskoField.setRequired(true);
        dataUrodzeniaPicker.setRequired(true);
        numerTelefonuField.setRequired(true);

        add(idField, imieField, nazwiskoField, dataUrodzeniaPicker, numerTelefonuField,
                new HorizontalLayout(saveButton, cancelButton));

        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(""));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> idParam = event.getRouteParameters().get("id");
        if (idParam.isPresent()) {
            id = Integer.parseInt(idParam.get());

            Osoba osoba = webClient.get()
                    .uri("/data/" + id)
                    .retrieve()
                    .bodyToMono(Osoba.class)
                    .block();

            if (osoba != null) {
                idField.setValue((double) osoba.getId());
                imieField.setValue(osoba.getImie());
                nazwiskoField.setValue(osoba.getNazwisko());
                dataUrodzeniaPicker.setValue(osoba.getData_urodzenia().toLocalDate());
                numerTelefonuField.setValue(osoba.getNumer_telefonu());
            }
        }
    }

    private void save() {
        if (idField.isEmpty() || imieField.isEmpty() || nazwiskoField.isEmpty() || dataUrodzeniaPicker.isEmpty() || numerTelefonuField.isEmpty()) {
            Notification.show("Wszystkie pola są wymagane.");
            return;
        }

        Osoba osoba = new Osoba();
        osoba.setId(idField.getValue().intValue());
        osoba.setImie(imieField.getValue());
        osoba.setNazwisko(nazwiskoField.getValue());
        osoba.setData_urodzenia(Date.valueOf(dataUrodzeniaPicker.getValue()));
        osoba.setNumer_telefonu(numerTelefonuField.getValue());

        Osoba wynik = webClient.put()
                .uri("/data/" + id)
                .bodyValue(osoba)
                .retrieve()
                .bodyToMono(Osoba.class)
                .block();

        Notification.show("Zapisano zmiany ID: " + wynik.getId());
        UI.getCurrent().navigate("");
    }
}
