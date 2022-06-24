package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

/**
 * c'est fermé ?
 */
public class IsClosed implements SpecificationI<GlassHouseControllerMBean> {
    @Override
    public boolean isSatisfiedBy(GlassHouseControllerMBean ghc) {
        return ghc.getOpeningState().equals("Closed");
    }
}
