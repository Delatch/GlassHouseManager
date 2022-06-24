package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

/**
 * C'est ouvert ?
 */
public class IsOpened implements SpecificationI<GlassHouseControllerMBean> {
    @Override
    public boolean isSatisfiedBy(GlassHouseControllerMBean ghc) {
        return ghc.getOpeningState().equals("Openened");
    }
}
