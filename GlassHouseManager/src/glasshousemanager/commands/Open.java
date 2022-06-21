package glasshousemanager.commands;

import glasshousemanager.GlassHouseControllerMBean;

public class Open extends Command<GlassHouseControllerMBean, Boolean> {
    public Boolean execute(GlassHouseControllerMBean ghc, Boolean b){
        ghc.open();
        return true;
    }
}
