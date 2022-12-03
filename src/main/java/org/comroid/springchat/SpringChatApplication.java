package org.comroid.springchat;

import org.comroid.api.ContentParser;
import org.comroid.api.io.FileHandle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"org.comroid.springchat.config", "org.comroid.springchat.controller"})
@EntityScan(basePackages = {"org.comroid.springchat.entity"})
public class SpringChatApplication {
    public static final FileHandle DATA_DIR = new FileHandle("/srv/chat/", true);
    public static final FileHandle OAUTH_SECRET_FILE = DATA_DIR.createSubFile("oauth_secret.txt");

    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }
}

