package com.github.taodong.maven.plugins.ngcli;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "ng-cli-run", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class NgRunMojo extends NgAbstractMojo {

    @Component
    protected MojoExecution execution;

    /**
     * Build front end for production or not
     */
    @Parameter(property = "isProduct", required = false, defaultValue = "false")
    protected Boolean isProduct;

    /**
     * time out for shell commands in minutes
     */
    @Parameter(property = "shellTimeout", required = false, defaultValue = "10")
    protected long timeout;
    /**
     * Whether you should skip while running in the test phase (default is false)
     */
    @Parameter(property = "skipTests", required = false, defaultValue = "false")
    protected Boolean skipTests;

    /**
     * Skip the whole ng client build in Maven lifecycle
     */
    @Parameter(property = "skipNgBuild", required = false, defaultValue = "false")
    protected Boolean skipNg;

    /**
     * Whether you should skip only front end tests while running in the test phase (default is false)
     */
    @Parameter(property = "skipNgTests", required = false, defaultValue = "false")
    protected Boolean skipNgTests;

    /**
     * Whether you should skip only front end tests while running in the test phase (default is false)
     */
    @Parameter(property = "runE2e", required = false, defaultValue = "true")
    protected Boolean runE2e;

    /**
     * Default ng project name which is defined in angular.json
     */
    @Parameter(property = "rootNgProject")
    protected String rootNgProject;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            ShellExecutor shellExecutor = new ShellExecutor();

            if (!skipNg) {

                List<String> commands = new ArrayList<String>();
                // check whether need to rebuild mode modules
                File[] moduleFolders = sourceDir.listFiles(new FilenameFilter() {
                    public boolean accept(File current, String name) {
                        return StringUtils.endsWithIgnoreCase(name, "node_module") &&
                                (new File(current, name).isDirectory());
                    }
                });

                if (moduleFolders == null || moduleFolders.length != 1 || moduleFolders[0].list().length == 0) {
                    commands.add("npm install");
                }

                // build front end using ng client
                if (!skipTests()) {
                    commands.add("ng test");
                    if (runE2e) {
                        commands.add("ng e2e");
                    }
                }
                String command = Joiner.on(" ").skipNulls().join("ng build", isProduct ? "--prod" : null);
                commands.add(command);
                int rs = shellExecutor.executeCommands(getLog(), commands, sourceDir, timeout * 60);
                if (rs < 0) {
                    throw new MojoFailureException("Ng build failed");
                }

                // copy files from ${sourceDir}/dist to ${distDir}
                File[] dirs = sourceDir.listFiles(new FilenameFilter() {
                    public boolean accept(File current, String name) {
                        return StringUtils.endsWithIgnoreCase(name, "dist") &&
                                (new File(current, name).isDirectory());
                    }
                });

                if (dirs == null || dirs.length != 1) {
                    throw new MojoFailureException(Joiner.on(" ").skipNulls().join("Can't find dist folder under", sourceDir.getAbsolutePath()));
                }

                File ngDistFolder = dirs[0];

                getLog().info(Joiner.on(" ").useForNull("EMPTY").join("rootNgProject is set to", rootNgProject));

                if (StringUtils.isNotBlank(rootNgProject)) {
                    ngDistFolder = new File(ngDistFolder, rootNgProject);

                    if (ngDistFolder == null || !ngDistFolder.exists() || !ngDistFolder.isDirectory()) {
                        throw new MojoExecutionException(Joiner.on(" ").skipNulls().join("Can't find folder", rootNgProject, "under dist. Please check your angular.json and make sure your defaultProject is set to", rootNgProject));
                    }
                }

                getLog().info(Joiner.on("").skipNulls().join("Copy files from ", ngDistFolder.getPath(), " to ", distDir.getPath()));
                FileUtils.copyDirectory(ngDistFolder, distDir);

            }
        } catch (Exception e) {
            getLog().error(Joiner.on(" ").skipNulls().join("Failed to run task.", e.getMessage()));
            throw new MojoFailureException("Failed to run task", e);
        }
    }

    /**
     * Determines if this execution should be skipped.
     */
    private boolean skipTests() {
        return (skipNgTests || skipTests);
    }

}
