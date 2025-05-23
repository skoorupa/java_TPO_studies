package com.example.tpo4.views;

import com.example.tpo4.Osoba;
import com.example.tpo4.OsobaDAO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Route("")
@PageTitle("Osoby")
public class HomeView extends VerticalLayout {
    private final WebClient webClient;

    public HomeView() {
        this.webClient = WebClient.create("http://localhost:8080/");

        add(new H1("Osoby"));

        Button addNew = new Button("Dodaj");
        addNew.addClickListener(e -> {
            UI.getCurrent().navigate(AddNewView.class);
        });

        add(addNew);

        Grid<Osoba> grid = new Grid<>(Osoba.class);
        List<Osoba> osobaList = webClient.get()
                .uri("/data")
                .retrieve()
                .bodyToFlux(Osoba.class)
                .collectList()
                .block();

        grid.setItems(osobaList);
        grid.setColumns("id", "imie", "nazwisko", "data_urodzenia");
        grid.addComponentColumn(osoba -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Button editButton = new Button("Edytuj");
            editButton.addClickListener(e ->
                    UI.getCurrent().navigate("edit/" + osoba.getId())
            );
            Button deleteButton = new Button("Usuń");
            deleteButton.addClickListener(e ->
                    UI.getCurrent().navigate("delete/" + osoba.getId())
            );
            horizontalLayout.add(editButton, deleteButton);
            return horizontalLayout;
        }).setHeader("Akcje");

        add(grid);
    }
}
