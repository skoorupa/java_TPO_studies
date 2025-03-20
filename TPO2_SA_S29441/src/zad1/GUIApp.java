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
    private JPanel outputPanel, temparaturePanel, weatherPanel, ratePanel, nbpRatePanel;
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
//        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        setVisible(true);

        // INPUT
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        countryPanel = new JPanel();
        countryLabel = new JLabel("Country:");
        countryField = new JTextField("Poland",15);
        cityPanel = new JPanel();
        cityLabel = new JLabel("City:");
        cityField = new JTextField("Warsaw",15);
        currencyPanel = new JPanel();
        currencyLabel = new JLabel("Currency:");
        currencyField = new JTextField("USD",15);

        runButtonPanel = new JPanel();
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

        add(inputPanel, BorderLayout.NORTH);

        // OUTPUT
        outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));

        temparaturePanel = new JPanel();
        temparatureLabel = new JLabel("Temperature:");
        temparatureField = new JTextField(15);
        temparatureField.setEditable(false);

        weatherPanel = new JPanel();
        weatherLabel = new JLabel("Weather:");
        weatherField = new JTextField(15);
        weatherField.setEditable(false);

        ratePanel = new JPanel();
        rateLabel = new JLabel("Rate:");
        rateField = new JTextField(15);
        rateField.setEditable(false);

        nbpRatePanel = new JPanel();
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

        outputPanel.add(temparaturePanel);
        outputPanel.add(weatherPanel);
        outputPanel.add(ratePanel);
        outputPanel.add(nbpRatePanel);
        outputPanel.add(Box.createVerticalGlue());

        outputPanel.setVisible(false);
        add(outputPanel, BorderLayout.CENTER);

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

        outputPanel.setVisible(true);
    }
}
