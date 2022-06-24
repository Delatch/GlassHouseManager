package glasshousemanager;

import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Classe de mise en forme du JSON reçu par l'API "station météo"
 */
public class WeatherResult implements Serializable {
    private String name;
    private double temp;
    private double temp_min;
    private double temp_max;
    private double feels_like;
    private int pressure;
    private int humidity;
    private String icon;
    private long dt;
    private long sunrise;
    private long sunset;
    private boolean rainingState;
    private String description;

    public WeatherResult(){}

    public String getName() {
        return name;
    }

    public double getTemp() {
        return temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getIcon() {
        return icon;
    }

    public long getDt() {
        return dt;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getRainingState() {
        return this.rainingState;
    }

    public void populateFromSensorResponse(JSONObject result) {
        JSONObject main = result.getJSONObject("main");
        JSONObject sys = result.getJSONObject("sys");
        JSONObject weather = result.getJSONArray("weather").getJSONObject(0);

        this.name = result.getString("name");
        this.dt = result.getLong("dt");
        this.sunrise = sys.getLong("sunrise");
        this.sunset = sys.getLong("sunset");
        this.feels_like = main.getDouble("feels_like");
        this.humidity = main.getInt("humidity");
        this.pressure = main.getInt("pressure");
        this.temp = main.getDouble("temp");
        this.temp_min = main.getDouble("temp_min");
        this.temp_max = main.getDouble("temp_max");
//        System.out.println(result.getJSONArray("weather").getJSONObject(0).getString("main"));
        this.rainingState = weather.getInt("id") < 800;//result.has("rain");
        this.description = weather.getString("description");
        this.icon = weather.getString("icon");
    }

    @Override
    public String toString() {
        String result;

        result = "Météo de " + name + " du " + getLastUpdateTime() + "\n" +
                "Température mesurée : " + temp + "°C\nTempérature ressentie : " + feels_like +
                "°C\nHumidité : " + humidity + "%\nPression : " + pressure + " hPa";

        result += "\n" + this.description;

        return result;
    }

    /**
     * Time the data was refreshed
     */
    public String getLastUpdateTime() {
        LocalDateTime date = new Timestamp(this.dt * 1000).toLocalDateTime();
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date);
    }
}
