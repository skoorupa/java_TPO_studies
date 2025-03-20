/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.htmlunit.BrowserVersion;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.SilentJavaScriptErrorListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Service {
    private Locale locale;
    private String kraj;
    private String zrodlowa_waluta;
    private WeatherJSON weather;
    private static String weatherApi = "http://api.openweathermap.org/data/2.5/weather?q=%s,%s&units=metric&APPID=6940a577d4f80e42cee5060a835e7063";
    private static String exchangeApi = "https://v6.exchangerate-api.com/v6/5c166bca8974888e4bf19aa7/pair/%s/%s";
    private static String nbpTableA = "https://nbp.pl/statystyka-i-sprawozdawczosc/kursy/tabela-a/";
    private static String nbpTableB = "https://nbp.pl/statystyka-i-sprawozdawczosc/kursy/tabela-b/";
    private static String nbpTableC = "https://nbp.pl/statystyka-i-sprawozdawczosc/kursy/tabela-c/";

    public Service(String kraj) {
        this.kraj = kraj;

        Optional<Locale> localeOptional = Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(kraj))
                .findFirst();

        if (!localeOptional.isPresent())
            throw new NullPointerException();

        locale = localeOptional.get();

        zrodlowa_waluta = Currency.getInstance(locale).getCurrencyCode();
    }

    public String getWeather(String miasto) {
        InputStream weatherInputStream;
        try {
            weatherInputStream = new URL(String.format(weatherApi, miasto, kraj)).openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String weatherJSON = new BufferedReader(new InputStreamReader(weatherInputStream)).lines().collect(Collectors.joining());

        Gson gson = new Gson();
        weather = gson.fromJson(weatherJSON, WeatherJSON.class);
        return weatherJSON;
    }

    public Double getRateFor(String kod_waluty) {
        InputStream exchangeInputStream;
        try {
            exchangeInputStream = new URL(String.format(exchangeApi, zrodlowa_waluta, kod_waluty)).openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String jsonString = new BufferedReader(new InputStreamReader(exchangeInputStream)).lines().collect(Collectors.joining());
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return jsonObject.get("conversion_rate").getAsDouble();
    }

    public Double getNBPRate() {
        if(zrodlowa_waluta.equals("PLN"))
            return 1d;

        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            LogManager.getLogManager().reset();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
//            webClient.setCssErrorHandler(new SilentCssErrorHandler());

            HtmlPage docA = webClient.getPage(nbpTableA);
            HtmlPage docB = webClient.getPage(nbpTableB);

            ArrayList<DomNode> rows = new ArrayList<>();
            DomNodeList<DomNode> rowsA = docA.querySelectorAll(".main-container tbody tr");
            DomNodeList<DomNode> rowsB = docB.querySelectorAll(".main-container tbody tr");
            rows.addAll(rowsA);
            rows.addAll(rowsB);

            for (DomNode tr : rows) {
                DomNodeList<DomNode> tds = tr.querySelectorAll("td");
                String sourceRateTd = tds.get(1).getTextContent();
                String plnRateTd = tds.get(2).getTextContent();
                if (sourceRateTd.contains(zrodlowa_waluta)) {
                    int sourceRate = Integer.parseInt(sourceRateTd.replace(" "+zrodlowa_waluta,""));
                    double plnRate = Double.parseDouble(plnRateTd.replaceAll(",","."));
                    return plnRate/sourceRate;
                }
            }
        } catch (IOException e) {
            System.out.println("nie udało się połączyć z NBP");
        }

        return 0d;
    }

    public double getTemperature() {
        return weather.main.temp;
    }

    public String getWeatherRaw() {
        return weather.weather[0].main;
    }
}

class WeatherJSON {
    Weather[] weather;
    WeatherMain main;
}

class Weather {
    String main;
}

class WeatherMain {
    double temp;
}
