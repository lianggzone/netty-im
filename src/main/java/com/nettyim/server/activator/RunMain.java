package com.nettyim.server.activator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.nettyim.server.server.ChatServer;

/**
 * 程序入口
 * @author 粱桂钊
 * @since 
 * <p>更新时间: 2016年7月31日  v0.1</p><p>版本内容: 创建</p>
 */
@SpringBootApplication
@ComponentScan("com.nettyim.server")
public class RunMain implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RunMain.class);

    @Autowired
    private ChatServer chatServer;
    
    public static void main(String[] args) {
        SpringApplication.run(RunMain.class, args);
    }
    
    public void run(String... strings) throws Exception {
        try {
        	chatServer.start();
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error("startup error!", e);
        }
    }
}
