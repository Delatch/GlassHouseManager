package glasshousemanager;

import glasshousemanager.utils.Logger;
import org.json.JSONObject;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Classe "station météo"
 *
 * récupère les informations météorologiques d'une ville à intervalle régulier au format JSON
 * et les émet à destination d'éventuels observateurs
 */
public class WeatherChannel extends NotificationBroadcasterSupport implements WeatherChannelMBean {
    private WeatherResult wr;
    private double temp;
    private Acquisition local;
    private int acquisitionInterval;
    private final String city;
    private long sequenceNumber = 0;
    private static final int DEFAULT_INTERVAL = 12 * 60 * 1000; // 12 minutes, le temps de rafraichissement des données de l'API
    private static final String DEFAULT_CITY = "Lorient";
    /**
     * Constructeur par défaut
     * Lance le thread d'acquisition avec les paramètres par défaut
     */
    public WeatherChannel() {
        this(DEFAULT_INTERVAL, DEFAULT_CITY); // 12 minutes
    }

    /**
     * Station météo
     * @param interval  : délai, exprimé en millisecondes, entre 2 interrogations
     * @param city      : ville observée
     */
    public WeatherChannel(int interval, String city) {
        this.acquisitionInterval = interval;
        this.city = city;
        this.local = new Acquisition(this.getCity());
    }

    // entraine l'envoi d'une notification
    private void setDatas(String json){
        this.sequenceNumber++;
        Logger.log("Notification sent ! (" + json.substring(0, 15) + "...)");
        sendNotification(
                new Notification("json",// un nom
                        this,
                        sequenceNumber, // un numéro
                        System.currentTimeMillis(), // une estampille
                        json
                )
        );
    }

    @Override
    public String getStringInfos() {
        return wr.toString();
    }

    @Override
    public String getDescription() {
        return this.wr.getDescription();
    }

    @Override
    public String getLastUpdateTime() {
        return this.wr.getLastUpdateTime();
    }

    @Override
    public double getTemp() {
        return this.wr.getTemp();
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setAcquisitionInterval(int interval) {
        acquisitionInterval = interval;
    }

    @Override
    public int getAcquisitionInterval() {
        return acquisitionInterval;
    }

    @Override
    public void reset() {
        if(this.local == null) return;

        Logger.log("Station relancée !!");
        this.local.interrupt();

        try {
            local.join();
        } catch (InterruptedException ie) {
        }

        local = new Acquisition(city);
    }

    // Classe privée chargée de l'acquisition des données de l'API via un thread indépendant
    private class Acquisition extends Thread implements java.io.Serializable {
        HttpClient client;
        HttpRequest request;

        public Acquisition(String city) {
            client = HttpClient.newBuilder().build();
            setRequest();
            start();
        }

        public void run() {
            try {
                while (!isInterrupted()) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject weatherResult = new JSONObject(response.body());

                    WeatherResult wr = new WeatherResult();
                    wr.populateFromSensorResponse(weatherResult);
                    WeatherChannel.this.wr = wr;
                    WeatherChannel.this.setDatas(response.body());

                    Thread.sleep(WeatherChannel.this.acquisitionInterval);
                }
            } catch (Exception e) {
                Logger.log(e.getMessage());
            }
        }

        public void setRequest(){
            request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=960729656d9e37a9ceee3a25b8c4cea6&units=metric&lang=fr"))
                    .timeout(Duration.ofSeconds(30))
                    .build();
        }
    }
}
