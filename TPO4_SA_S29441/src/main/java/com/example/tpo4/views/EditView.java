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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.util.Optional;

@Route("/edit/:id")
@PageTitle("Dodaj nową osobę")
public class EditView extends VerticalLayout implements BeforeEnterObserver {
    private final WebClient webClient = WebClient.create("http://localhost:8080/");

    private final TextField imieField = new TextField("Imię");
    private final TextField nazwiskoField = new TextField("Nazwisko");
    private final DatePicker dataUrodzeniaPicker = new DatePicker("Data urodzenia");
    private final TextField numerTelefonuField = new TextField("Numer telefonu");

    private final Button saveButton = new Button("Zapisz");
    private final Button cancelButton = new Button("Anuluj");

    private int id;

    public EditView() {
        add(new H2("Edytuj osobę"));

        imieField.setRequired(true);
        nazwiskoField.setRequired(true);
        numerTelefonuField.setRequired(true);

        add(imieField, nazwiskoField, dataUrodzeniaPicker, numerTelefonuField,
                new HorizontalLayout(saveButton, cancelButton));

        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(""));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> idParam = event.getRouteParameters().get("id");
        if (idParam.isPresent()) {
            id = Integer.parseInt(idParam.get());

            Optional<Osoba> osobaOptional = webClient.get()
                    .uri("/data/" + id)
                    .retrieve()
                    .bodyToMono(Osoba.class).blockOptional();

            if (osobaOptional.isPresent()) {
                Osoba osoba = osobaOptional.get();
                imieField.setValue(osoba.getImie());
                nazwiskoField.setValue(osoba.getNazwisko());
                if (osoba.getData_urodzenia() != null)
                    dataUrodzeniaPicker.setValue(osoba.getData_urodzenia().toLocalDate());
                numerTelefonuField.setValue(osoba.getNr_telefonu());
            } else {
                Notification.show("Nie znaleziono osoby o id="+id);
                UI.getCurrent().navigate("");
            }
        } else {
            Notification.show("Brak ID w adresie.");
            UI.getCurrent().navigate("");
        }
    }

    private void save() {
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
