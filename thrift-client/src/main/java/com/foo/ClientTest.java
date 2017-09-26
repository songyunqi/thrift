package com.foo;

import com.foo.thrift.Profile;
import com.foo.thrift.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientTest {

    public final static int PORT = 9999;
    public static final String ADDRESS = "localhost";
    public static final int CLIENT_TIMEOUT = 30000;

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

            CountDownLatch latch = new CountDownLatch(1);
            AsyncCallback callBack = new AsyncCallback(latch);
            //System.out.println("call method sayHello start ...");
            // 调用服务
            asyncClient.toMap(profile, callBack);
            //System.out.println("call method sayHello .... end");
            //等待完成异步调用
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("Client calls .....");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientTest clientTest = new ClientTest();
        //clientTest.go();
        clientTest.goAsyn();
    }
}
