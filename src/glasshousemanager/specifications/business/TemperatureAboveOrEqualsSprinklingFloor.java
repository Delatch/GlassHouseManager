package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

/**
 * La température dépasse-t-elle le seuil de déclenchement de l'aspersion ?
 */
public class TemperatureAboveOrEqualsSprinklingFloor implements SpecificationI<GlassHouseControllerMBean> {
    @Override
    public boolean isSatisfiedBy(GlassHouseControllerMBean ghs) {
        return ghs.getTemperature()>ghs.getWateringTemperature();
    }
}
