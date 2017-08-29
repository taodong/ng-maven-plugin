package com.github.taodong.maven.plugins.ngcli;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class NgAbstractMojo extends AbstractMojo {
    @Component
    protected MojoExecution execution;

    /**
     * Angular source directory
     */
    @Parameter(property = "sourceDir", required = true)
    protected File sourceDir;

    /**
     * Front end dist directory
     */
    @Parameter(property = "distDir", required = true)
    protected File distDir;

    protected boolean isPhase(String mvnPhase) {
        String phase = execution.getLifecyclePhase();
        return mvnPhase != null && mvnPhase.equalsIgnoreCase(phase);
    }
}
