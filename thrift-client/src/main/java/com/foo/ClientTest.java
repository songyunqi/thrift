package com.foo;

import com.foo.thrift.Profile;
import com.foo.thrift.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.Map;

@Slf4j
public class ClientTest {

    public final static int PORT = 9999;
    public static final String address = "localhost";
    public static final int clientTimeout = 30000;

    public void test() {
        TTransport transport = new TFramedTransport(new TSocket(address, PORT, clientTimeout));
        TProtocol protocol = new TCompactProtocol(transport);
        ProfileService.Client client = new ProfileService.Client(protocol);

        try {
            transport.open();
            Profile profile = new Profile();
            profile.setName("Snowolf");
            Map<String, String> map = client.toMap(profile);
            log.info(map.toString());
        } catch (TApplicationException e) {
            System.out.println(e.getMessage() + " " + e.getType());
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        transport.close();
    }

    public static void main(String[] args) {
        ClientTest clientTest = new ClientTest();
        clientTest.test();
    }
}
