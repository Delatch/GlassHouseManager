package glasshousemanager;

import javax.management.NotificationBroadcaster;

public interface WeatherChannelMBean extends NotificationBroadcaster {

    String getLastUpdateTime();

    double getTemp();

    String getStringInfos();

    String getDescription();

    String getCity();

    int getAcquisitionInterval();

    void setAcquisitionInterval(int interval);

    void reset();
}
