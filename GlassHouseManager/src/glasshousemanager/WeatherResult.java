package glasshousemanager;

import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    public WeatherResult(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
    
    public boolean getRainingState() {
        return this.rainingState;
    }
    
    public void populateFromSensorResponse(JSONObject result) {
        JSONObject main = result.getJSONObject("main");
        JSONObject sys = result.getJSONObject("sys");

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
        this.rainingState = result.has("rain");

//        this.icon = result.getString("icon");
    }

    @Override
    public String toString(){
        LocalDateTime date = new Timestamp(dt * 1000).toLocalDateTime();
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date);
        String timeStamp = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date);
        String result;

        result = "Météo de " + name + " du " + timeStamp + "\n"+
                "Température mesurée : " + temp + "°C\nTempérature ressentie : " + feels_like +
                "°C\nHumidité : " + humidity + "%\nPression : " + pressure + " hPa";

        if(this.rainingState)
            result += "\nIl pleut.";
        else
            result += "\nIl ne pleut pas.";

        return result;

    }

}
