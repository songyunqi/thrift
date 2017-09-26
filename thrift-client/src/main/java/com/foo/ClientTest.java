package com.foo;

import com.foo.thrift.Profile;
import com.foo.thrift.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class ClientTest {

    public final static int PORT = 9999;
    public static final String ADDRESS = "localhost";
    public static final int CLIENT_TIMEOUT = 30000;

    public void test() {
        TTransport transport = new TFramedTransport(new TSocket(ADDRESS, PORT, CLIENT_TIMEOUT));
        TProtocol protocol = new TCompactProtocol(transport);
        ProfileService.Client client = new ProfileService.Client(protocol);

        try {
            transport.open();
            Profile profile = new Profile();
            profile.setName("Snowolf");
            Map<String, String> map = client.toMap(profile);
            System.out.println(map);
        } catch (TApplicationException e) {
            System.out.println(e.getMessage() + " " + e.getType());
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        transport.close();
    }

    //go
    public void go() {
        TTransport transport = new TFramedTransport(new TSocket(ADDRESS, PORT, CLIENT_TIMEOUT));
        TProtocol protocol = new TCompactProtocol(transport);

        ProfileService.Client client = new ProfileService.Client(protocol);
        try {
            transport.open();
            Profile profile = new Profile();
            profile.setName("Snowolf");
            Map<String, String> result = client.toMap(profile);
            System.out.println("result : " + result);
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    //test
    public void goAsyn() {
        try {
            TAsyncClientManager clientManager = new TAsyncClientManager();
            TNonblockingTransport transport = new TNonblockingSocket(ADDRESS, PORT, CLIENT_TIMEOUT);
            //TTransport transport = new TFramedTransport(new TSocket(ADDRESS, PORT, CLIENT_TIMEOUT));
            TProtocolFactory protocolFactory = new TCompactProtocol.Factory();

            ProfileService.AsyncClient asyncClient = new ProfileService.AsyncClient(protocolFactory, clientManager, transport);
            Profile profile = new Profile();
            profile.setName("Snowolf");
            asyncClient.toMap(profile, new AsyncCallback());
            System.out.println("Client calls .....");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientTest clientTest = new ClientTest();
        //clientTest.test();
        //clientTest.go();
        clientTest.goAsyn();
    }
}
