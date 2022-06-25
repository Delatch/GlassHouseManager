package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.*;

import java.awt.*;

/**
 * On eteint l'arrosage si
 * il est en marche
 * ET (qu'il pleut OU qu'on est sous le seuil de déclenchement)
 */
public class StopWatering extends All<GlassHouseControllerMBean>{
    public StopWatering() {
        this.add(new And<GlassHouseControllerMBean>((new Not<GlassHouseControllerMBean>(new WateringIsOff())),
                new Or<GlassHouseControllerMBean>(new IsRaining(), new TemperatureUnderSprinklingFloor())));
    }
}
