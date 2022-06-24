package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.All;
import glasshousemanager.specifications.Not;

/**
 * On ouvre l'arrosage si
 * il est éteint
 * ET qu'il ne pleut pas
 * ET qu'on est au-dessus le seuil de déclenchement
 */
public class LaunchWatering extends All<GlassHouseControllerMBean>{
    public LaunchWatering(){
        this.add(new TemperatureAboveOrEqualsSprinklingFloor())
                .add(new Not<GlassHouseControllerMBean>(new WateringIsOn()))
                .add(new Not<GlassHouseControllerMBean>(new IsRaining()));
    }
}
