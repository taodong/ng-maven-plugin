package com.github.taodong.maven.plugins.ngcli;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FilenameFilter;

@Mojo(name = "ng-cli-clean", defaultPhase = LifecyclePhase.CLEAN, threadSafe = true)
public class NgCleanMojo extends NgAbstractMojo {

    /**
     * whether to clear
     */
    @Parameter(property = "cleanNode", required = false, defaultValue = "false")
    protected Boolean cleanNode;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            FileUtils.cleanDirectory(distDir);

            File[] dirs = sourceDir.listFiles(new FilenameFilter() {
                public boolean accept(File current, String name) {
                    return StringUtils.endsWithIgnoreCase(name, "dist") &&
                            (new File(current, name).isDirectory());
                }
            });

            if (dirs != null && dirs.length == 1) {
                FileUtils.cleanDirectory(dirs[0]);
            }

            if (cleanNode) {
                dirs = sourceDir.listFiles(new FilenameFilter() {
                    public boolean accept(File current, String name) {
                        return StringUtils.endsWithIgnoreCase(name, "node_modules") &&
                                (new File(current, name).isDirectory());
                    }
                });

                if (dirs != null && dirs.length == 1) {
                    FileUtils.cleanDirectory(dirs[0]);
                }
            }
        }  catch (Exception e) {
            getLog().error(Joiner.on(" ").skipNulls().join("Failed to run task.", e.getMessage()));
            throw new MojoFailureException("Failed to run task", e);
        }
    }

}
