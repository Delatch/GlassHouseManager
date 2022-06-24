package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

// La température est-elle sous le seuil de déclenchement de l'ouverture ?
    public class TemperatureUnderSprinklingFloor implements SpecificationI<GlassHouseControllerMBean> {
        @Override
        public boolean isSatisfiedBy(GlassHouseControllerMBean ghs) {
            return ghs.getTemperature()<ghs.getWateringTemperature();
        }
    }


