package com.github.taodong.maven.plugins.ngcli;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShellExecutorTests {

    @Test
    public void testExecuteCommands() {
        ShellExecutor shellExecutor = new ShellExecutor();
        ConsoleLogger consoleLogger = new ConsoleLogger(Logger.LEVEL_INFO, "Console");
        Log logger = new DefaultLog(consoleLogger);
        List<String> commands = new ArrayList<String>();
        commands.add("ls");
        commands.add("ls -l");
        int rs = shellExecutor.executeCommands(logger, commands, null, 0);
        assertEquals(1, rs);
    }
}
