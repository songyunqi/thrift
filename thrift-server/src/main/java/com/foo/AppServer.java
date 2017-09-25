package com.foo;

import com.foo.thrift.ProfileService;
import com.foo.thrift.ProfileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
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

    public static void main(String[] args) {
        int port = 9999;
        AppServer appServer = new AppServer(port);
        appServer.start();
    }
}
