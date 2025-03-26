/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private Locale locale;
    private String kraj;
    private String krajCode;
    private String zrodlowa_waluta;
    private WeatherJSON weather;
    private NBPJSON nbpjson;
    private static String weatherApi = "http://api.openweathermap.org/data/2.5/weather?q=%s,%s&units=metric&APPID=6940a577d4f80e42cee5060a835e7063";
    private static String rateApi = "https://v6.exchangerate-api.com/v6/5c166bca8974888e4bf19aa7/pair/%s/%s";
    private static String nbpTableA = "https://api.nbp.pl/api/exchangerates/rates/a/%s/last/1?format=json";
    private static String nbpTableB = "https://api.nbp.pl/api/exchangerates/rates/b/%s/last/1?format=json";
    private static String nbpTableC = "https://api.nbp.pl/api/exchangerates/rates/c/%s/last/1?format=json";

    public Service(String kraj) {
        this.kraj = kraj;

        Optional<Locale> localeOptional = Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(kraj))
                .findFirst();

        if (!localeOptional.isPresent())
            throw new NullPointerException();

        locale = localeOptional.get();
        krajCode = locale.getCountry();

        zrodlowa_waluta = Currency.getInstance(locale).getCurrencyCode();
    }

    public String getWeather(String miasto) {
        InputStream weatherInputStream;
        try {
            weatherInputStream = new URL(String.format(weatherApi, miasto, krajCode)).openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String weatherJSON = new BufferedReader(new InputStreamReader(weatherInputStream)).lines().collect(Collectors.joining());

        Gson gson = new Gson();
        weather = gson.fromJson(weatherJSON, WeatherJSON.class);
        return weatherJSON;
    }

    public Double getRateFor(String kod_waluty) {
        InputStream rateInputStream;
        try {
            rateInputStream = new URL(String.format(rateApi, zrodlowa_waluta, kod_waluty)).openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String jsonString = new BufferedReader(new InputStreamReader(rateInputStream)).lines().collect(Collectors.joining());
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        double output = jsonObject.get("conversion_rate").getAsDouble();
        return output;
    }

    public Double getNBPRate() {
        if(zrodlowa_waluta.equals("PLN"))
            return 1d;

        InputStream nbpRateAInputStream, nbpRateBInputStream;
        try {
            HttpURLConnection nbpRateAConnection = (HttpURLConnection) new URL(String.format(nbpTableA, zrodlowa_waluta)).openConnection();
            HttpURLConnection nbpRateBConnection = (HttpURLConnection) new URL(String.format(nbpTableB, zrodlowa_waluta)).openConnection();

            String nbpRateJSON;
            if (nbpRateAConnection.getResponseCode() != 404) {
                nbpRateAInputStream = nbpRateAConnection.getInputStream();
                nbpRateJSON = new BufferedReader(new InputStreamReader(nbpRateAInputStream)).lines().collect(Collectors.joining());
            } else {
                nbpRateBInputStream = nbpRateBConnection.getInputStream();
                nbpRateJSON = new BufferedReader(new InputStreamReader(nbpRateBInputStream)).lines().collect(Collectors.joining());
            }

            Gson gson = new Gson();
            nbpjson = gson.fromJson(nbpRateJSON, NBPJSON.class);

            double output = nbpjson.rates[0].mid;
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getTemperature() {
        return weather.main.temp;
    }

    public String getWeatherRaw() {
        return weather.weather[0].main;
    }
}

// WEATHER

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

// NBP

class NBPJSON {
    Rate[] rates;
}

class Rate {
    double mid;
}
