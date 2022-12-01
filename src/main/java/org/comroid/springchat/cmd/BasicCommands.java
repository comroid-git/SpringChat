package org.comroid.springchat.cmd;

import org.comroid.cmdr.CommandManager;
import org.comroid.cmdr.model.Command;
import org.comroid.springchat.model.StatusUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BasicCommands {
    @Autowired
    private CommandManager commandManager;

    @PostConstruct
    public void init() {
        commandManager.registerCommands(this);
    }

    @Command(description = "Lists all available commands")
    public String help(
            /*todo: CMDR needs to handle non-required arguments right
               @Command.Arg(required = false) String topic*/
    ) {
        var sb = new StringBuilder();
        sb.append("Available commands:\n");
        commandManager.getCommands().values().forEach(obj -> sb.append(obj).append("\n"));
        return sb.toString();
    }

    @Command(description = "Sends a private message")
    public StatusUpdate msg(@Command.Arg(ordinal = 0) String user, @Command.ExtraArgs Object[] msg) {
        return null; // todo
    }

    @Command(description = "Changes your nickname")
    public void nick(@Command.Arg String nickname) {
        // todo
    }

    @Command(description = "Kicks a user")
    public void kick(@Command.Arg String username) {
        // todo
    }
}
