package glasshousemanager.commands;

import glasshousemanager.GlassHouseControllerMBean;

public class Close extends Command<GlassHouseControllerMBean, Boolean> {
    public Boolean execute(GlassHouseControllerMBean ghc, Boolean b){
        ghc.close();
        return true;
    }
}
