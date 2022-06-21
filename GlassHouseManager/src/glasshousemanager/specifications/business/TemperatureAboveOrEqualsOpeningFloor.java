package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

// La température dépasse-t-elle le seuil de déclenchement de l'ouverture ?
    public class TemperatureAboveOrEqualsOpeningFloor implements SpecificationI<GlassHouseControllerMBean> {
        @Override
        public boolean isSatisfiedBy(GlassHouseControllerMBean ghs) {
            return ghs.getTemperature()>ghs.getOpeningTemperature();
        }
    }


