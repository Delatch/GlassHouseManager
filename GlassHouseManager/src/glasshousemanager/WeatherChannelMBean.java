package glasshousemanager;

import javax.management.NotificationBroadcaster;

public interface WeatherChannelMBean  extends NotificationBroadcaster {

    public double getTemp();
    public String getStringInfos();

    public String getCity();

    public int getAcquisitionInterval();
    public void setAcquisitionInterval(int interval);

    public void reset();
}
