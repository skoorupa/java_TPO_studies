/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;


import javafx.embed.swing.JFXPanel;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Hungary");
    String weatherJson = s.getWeather("Budapest");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI

    SwingUtilities.invokeLater(()->{
//      new JFXPanel();
      new GUIApp();
    });
  }
}
