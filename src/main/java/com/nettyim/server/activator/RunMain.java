package com.nettyim.server.activator;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.nettyim.server.server.TcpChatServer;

/**
 * <p>Title: 程序入口  </p>
 * <p>Description: RunMain </p>
 * <p>Create Time: 2016年7月20日           </p>
 * @author lianggz
 */
@SpringBootApplication
@ComponentScan("com.nettyim.server")
public class RunMain implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RunMain.class);

    @Resource(name = "tcpChatServer")
    private TcpChatServer tcpChatServer;
    
    public static void main(String[] args) {
        SpringApplication.run(RunMain.class, args);
    }
    
    public void run(String... strings) throws Exception {
        try {
            tcpChatServer.start();
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error("startup error!", e);
        }
    }
}
