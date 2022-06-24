package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.All;
import glasshousemanager.specifications.Not;
import glasshousemanager.specifications.Or;

/**
 * On eteint l'arrosage si
 * il est en marche
 * ET (qu'il pleut OU qu'on est sous le seuil de déclenchement)
 */
public class StopWatering extends All<GlassHouseControllerMBean>{
    public StopWatering() {
        this.add(new Not<GlassHouseControllerMBean>(new WateringIsOff()))
                .add(new Or<GlassHouseControllerMBean>(new IsRaining(), new TemperatureUnderSprinklingFloor()));
    }
}
