package org.comroid.springchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"org.comroid.springchat.config", "org.comroid.springchat.controller"})
@EntityScan(basePackages = {"org.comroid.springchat.entity"})
public class SpringChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }
}

/*
TODO: Client Klasse
TODO: ReaderThread liest Input vom Server
TODO: WriteThread Klasse sendet Input zum Server
 */

