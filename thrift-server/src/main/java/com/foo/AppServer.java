package com.foo;

import com.foo.thrift.ProfileService;
import com.foo.thrift.ProfileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

@Slf4j
public class AppServer {

    private int port;

    public AppServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            TNonblockingServerSocket socket = new TNonblockingServerSocket(port);
            final ProfileService.Processor processor = new ProfileService.Processor(new ProfileServiceImpl());
            THsHaServer.Args args = new THsHaServer.Args(socket);
            // 高效率的、密集的二进制编码格式进行数据传输
            // 使用非阻塞方式，按块的大小进行传输，类似于 Java 中的 NIO
            args.protocolFactory(new TCompactProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            args.processorFactory(new TProcessorFactory(processor));
            TServer server = new THsHaServer(args);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void goMulti() {
        try {
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("UserService", new ProfileService.Processor<ProfileService.Iface>(new ProfileServiceImpl()));

            TNonblockingServerSocket socket = new TNonblockingServerSocket(port);
            //processor.registerProcessor("TopicService", new TopicService.Processor<TopicService.Iface>(new TopicImpl()));

            TThreadPoolServer.Args args = new TThreadPoolServer.Args(socket);
            args.processor(processor);
            args.protocolFactory(new TBinaryProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            args.processorFactory(new TProcessorFactory(processor));

            TServer server = new TThreadPoolServer(args);
            server.serve();

//            TProcessor tprocessor = new Hello.Processor<>(new HelloImpl());
//            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
//            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(
//                    serverTransport);
//            tArgs.processor(tprocessor);
//            tArgs.protocolFactory(new TBinaryProtocol.Factory());
//
//            TThreadPoolServer ttps = new TThreadPoolServer(tArgs);
//            System.out.println("init...");
//            ttps.serve();


        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int port = 9999;
        AppServer appServer = new AppServer(port);
        appServer.goMulti();
    }
}
