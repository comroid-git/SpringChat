package org.comroid.springchat;

import org.comroid.cmdr.CommandManager;
import org.comroid.springchat.cmd.BasicCommands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"org.comroid.springchat.config", "org.comroid.springchat.controller", "org.comroid.springchat.cmd", "org.comroid.cmdr.spring"})
@EntityScan(basePackages = {"org.comroid.springchat.entity"})
public class SpringChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }
}

