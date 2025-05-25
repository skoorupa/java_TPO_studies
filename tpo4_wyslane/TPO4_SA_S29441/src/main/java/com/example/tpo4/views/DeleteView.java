package com.example.tpo4.views;

import com.example.tpo4.Osoba;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.Optional;

@Route("delete/:id")
@PageTitle("Usuń osobę")
public class DeleteView extends VerticalLayout implements BeforeEnterObserver {

    private final WebClient webClient = WebClient.create("http://localhost:8080");

    private Grid<Osoba> grid = new Grid<>(Osoba.class, false);
    private int id;

    private final Button deleteButton = new Button("Usuń");
    private final Button cancelButton = new Button("Anuluj");

    public DeleteView() {
        add(new H2("Czy na pewno chcesz usunąć tę osobę?"));

        deleteButton.addClickListener(e -> usunOsobe());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(""));

        add(new HorizontalLayout(deleteButton, cancelButton));

        grid.addColumn(Osoba::getId).setHeader("ID");
        grid.addColumn(Osoba::getImie).setHeader("Imię");
        grid.addColumn(Osoba::getNazwisko).setHeader("Nazwisko");
        grid.addColumn(o -> o.getData_urodzenia() != null ? o.getData_urodzenia().toLocalDate().toString() : "")
                .setHeader("Data urodzenia");

        add(grid);
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
                grid.setItems(osoba);
            } else {
                Notification.show("Nie znaleziono osoby.");
                UI.getCurrent().navigate("");
            }
        } else {
            Notification.show("Brak ID w adresie.");
            UI.getCurrent().navigate("");
        }
    }

    private void usunOsobe() {
        try {
            webClient.delete()
                    .uri("/data/" + id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            Notification.show("Osoba została usunięta.");
        } catch (WebClientResponseException e) {
            Notification.show("Błąd podczas usuwania: " + e.getStatusCode());
        }
        UI.getCurrent().navigate("");
    }
}
