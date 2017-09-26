package com.foo;

import com.foo.thrift.ProfileService;
import com.foo.thrift.ProfileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;

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


    public void go() {
        try {
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            TServerTransport t = new TServerSocket(port);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(t).processor(processor));
            //processor.registerProcessor("TopicService", new TopicService.Processor<TopicService.Iface>(new TopicImpl()));
            processor.registerProcessor("UserService", new ProfileService.Processor<ProfileService.Iface>(new ProfileServiceImpl()));
            //TSimpleServer server = new TSimpleServer(new THsHaServer.Args(t).processor(processor));
            System.out.println("the serveris started and is listening at 9090...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int port = 9999;
        AppServer appServer = new AppServer(port);
        appServer.start();
    }
}
