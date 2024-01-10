import com.yubico.hsm.YHSession;
import com.yubico.hsm.YubiHsm;
import com.yubico.hsm.backend.Backend;
import com.yubico.hsm.backend.HttpBackend;
import com.yubico.hsm.yhdata.YHObjectInfo;
import com.yubico.hsm.yhobjects.YHObject;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {

            //http URL along with the Port Number and the Timeout Seconds should be passed to the HttpBackend constructor.
            Backend backend = new HttpBackend("http://127.0.0.1:12345",60);
            YubiHsm yubihsm = new YubiHsm(backend);
            System.out.println("Connecting to YubiHSM device:");

            //Create a Session by providing the appropriate key and its password.
            //Default key would be 1 and the password would be password.
            YHSession session = new YHSession(yubihsm, (short) 1, "password".toCharArray());
            session.authenticateSession();

            // List all objects
            System.out.println("Listing all objects");
            List<YHObjectInfo> objects = YHObject.getObjectList(session, null);
            printObjects(session, objects);

            System.out.println("---------------------------");

            // Close the session and the backend
            session.closeSession();
            yubihsm.close();
            backend.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printObjects(YHSession session, List<YHObjectInfo> objects) throws Exception {
        System.out.println("Found " + objects.size() + " items:");
        int count = 1;
        for (YHObjectInfo info : objects) {
            System.out.println("Object " + count + " - Simple info: ");
            System.out.println(info.toSimpleString());
            YHObjectInfo detailedInfo = YHObject.getObjectInfo(session, info.getId(), info.getType());
            System.out.println("Object " + count + " - Detailed info: ");
            System.out.println(detailedInfo.toString());
            count++;
        }
    }
}