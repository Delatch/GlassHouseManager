package glasshousemanager;

import com.sun.jdmk.comm.HtmlAdaptorServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GlassHouseControllerAgent {

    public GlassHouseControllerAgent() throws UnknownHostException {
        this(InetAddress.getLocalHost().getHostAddress(), "Lorient");
    }

    public GlassHouseControllerAgent(String weatherChannelAddress, String city){
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName("GlassHouseControllerAgent:name=Controller1");
            GlassHouseController controller = new GlassHouseController(weatherChannelAddress, city);
            mbs.registerMBean(controller, name);

            // Create an RMI connector and start it
            Registry registry;
            try{
                registry = LocateRegistry.getRegistry(9999);
            }
            catch(Exception e) {
                registry = LocateRegistry.createRegistry(9999);
            }

            try {
                registry.unbind("glasshousecontroller");
            } catch (Exception e) {

            }

            //String localHost = InetAddress.getLocalHost().getHostAddress();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + weatherChannelAddress +":9999/glasshousecontroller");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
            cs.start();

            // Create an HTTP adapter and start it
            HtmlAdaptorServer adapter = new HtmlAdaptorServer();
            adapter.setPort(8088);
            name = new ObjectName("HtmlAdaptorServer:name=html,port=8088");
//            mbs.unregisterMBean(name);
            mbs.registerMBean(adapter, name);
            adapter.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String weatherStationAddress = "localhost";
        String city = "Lorient";

        try{
            if(args[0] != null)
                weatherStationAddress = args[0];
        }
        catch(Exception e){}

        try{
            if(args[1] != null)
                city = args[1];
        }
        catch(Exception e){}

        GlassHouseControllerAgent agent2 = new GlassHouseControllerAgent(weatherStationAddress, city);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
