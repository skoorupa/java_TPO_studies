package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class GUIApp extends JFrame {
    // input
    private JPanel inputPanel, countryPanel, cityPanel, currencyPanel, runButtonPanel;
    private JLabel countryLabel, cityLabel, currencyLabel;
    private JTextField countryField, cityField, currencyField;
    private JButton runButton;
    // output
    private JPanel temparaturePanel, weatherPanel, ratePanel, nbpRatePanel;
    private JLabel temparatureLabel, weatherLabel, rateLabel, nbpRateLabel;
    private JTextField temparatureField, weatherField, rateField, nbpRateField;
    private Service service;
    // wiki
//    private JFXPanel wikipediaPanel;
//    private WebView wikipediaView;
//    private WebEngine wikipediaEngine;
//    private Scene wikipediaScene;

    public GUIApp() {
        super("S_WEBCLIENTS");

        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setVisible(true);

        // INPUT
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0,1));

        countryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        countryLabel = new JLabel("Country:");
        countryField = new JTextField("Poland",15);
        cityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cityLabel = new JLabel("City:");
        cityField = new JTextField("Warsaw",15);
        currencyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        currencyLabel = new JLabel("Currency:");
        currencyField = new JTextField("USD",15);

        runButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        runButton = new JButton("Run");
        runButton.addActionListener(l -> showInfo());

        countryPanel.add(countryLabel);
        countryPanel.add(countryField);
        cityPanel.add(cityLabel);
        cityPanel.add(cityField);
        currencyPanel.add(currencyLabel);
        currencyPanel.add(currencyField);
        runButtonPanel.add(runButton);

        inputPanel.add(countryPanel);
        inputPanel.add(cityPanel);
        inputPanel.add(currencyPanel);
        inputPanel.add(runButtonPanel);

        // OUTPUT

        temparaturePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        temparatureLabel = new JLabel("Temperature:");
        temparatureField = new JTextField(15);
        temparatureField.setEditable(false);

        weatherPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        weatherLabel = new JLabel("Weather:");
        weatherField = new JTextField(15);
        weatherField.setEditable(false);

        ratePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rateLabel = new JLabel("Rate:");
        rateField = new JTextField(15);
        rateField.setEditable(false);

        nbpRatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nbpRateLabel = new JLabel("NBP rate:");
        nbpRateField = new JTextField(15);
        nbpRateField.setEditable(false);

        temparaturePanel.add(temparatureLabel);
        temparaturePanel.add(temparatureField);
        weatherPanel.add(weatherLabel);
        weatherPanel.add(weatherField);
        ratePanel.add(rateLabel);
        ratePanel.add(rateField);
        nbpRatePanel.add(nbpRateLabel);
        nbpRatePanel.add(nbpRateField);

        inputPanel.add(temparaturePanel);
        inputPanel.add(weatherPanel);
        inputPanel.add(ratePanel);
        inputPanel.add(nbpRatePanel);

        add(inputPanel, BorderLayout.NORTH);

//        Platform.runLater(()->{
//            wikipediaPanel = new JFXPanel();
//            wikipediaView = new WebView();
//            wikipediaEngine = wikipediaView.getEngine();
//            wikipediaScene = new Scene(wikipediaView);
//            wikipediaPanel.setScene(wikipediaScene);
//        });
//
//
//        add(wikipediaPanel, BorderLayout.SOUTH);
    }

    public void showInfo() {
        service = new Service(countryField.getText());
        service.getWeather(cityField.getText());

        double temperature = service.getTemperature();
        String weatherRaw = service.getWeatherRaw();
        double rate1 = service.getRateFor(currencyField.getText());
        double rate2 = service.getNBPRate();

        temparatureField.setText(Double.toString(temperature));
        weatherField.setText(weatherRaw);
        rateField.setText(Double.toString(rate1));
        nbpRateField.setText(Double.toString(rate2));

//        wikipediaEngine.load("https://en.wikipedia.org/wiki/"+cityField.getText());
    }
}
