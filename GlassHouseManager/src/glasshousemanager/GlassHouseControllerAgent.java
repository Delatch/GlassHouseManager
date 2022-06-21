package glasshousemanager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import com.sun.jdmk.comm.HtmlAdaptorServer;

public class GlassHouseControllerAgent {
    private MBeanServer mbs;

    public GlassHouseControllerAgent(){
        mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName("GlassHouseControllerAgent:name=Controller1");
            GlassHouseController controller = new GlassHouseController();
            mbs.registerMBean(controller, name);

            // Create an RMI connector and start it
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/glasshousecontroller");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);


            // Create an HTTP adapter and start it
            HtmlAdaptorServer adapter = new HtmlAdaptorServer();
            adapter.setPort(8088);
            name = new ObjectName("HtmlAdaptorServer:name=html,port=8088");
            mbs.registerMBean(adapter, name);
            adapter.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
