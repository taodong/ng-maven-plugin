# ngcli-maven-plugin
This is a maven plug in for Angular client for integration purpose. This plugin runs ng command to build the front end pages, then copy files to the desired folder used by backend.

This plugin doesn't include functions to install any front end runner 

## Requirements
* Angular client installed
* NPM installed

## Installation
Include the plugin as a dependency in your Maven project. Change LATEST_VERSION to the latest tagged version.
```
<plugin>
    <groupId>com.github.taodong</groupId>
    <artifactId>ngcli-maven-plugin</artifactId>
    <version>LATEST_VERSION</version>
    ...
</plugin>
```

## Usage
This plugin includes two modules ng-cli-run and ng-cli-clean. To run this plugin, two parameters are required to be configured.

* sourceDir - should be the root folder of all the front end sources
* distDir - the folder which the front end files to be copy to 

### ng-cli-run
Run ng client to build front end. It will run command "ng test", "ng e2e", "ng build" under ${sourceDir} folder, then copy files under ${sourceDir}/dist to ${distDir}. Default phase is set to generate-resources. A sample usage is:
```$xslt
<execution>
    <id>ng-cli-run</id>
    <goals>
        <goal>ng-cli-run</goal>
    </goals>
    <configuration>
        <isProduct>false</isProduct>
        <runE2e>false</runE2e>
    </configuration>
</execution>
```

Optional configuration parameter include:
* isProduct: whether to build front end for production. Default is false.
* skipNgBuild: whether to skip the execution of this module. Default is false.
* skipTests: a shared flag with maven tests. Default is false.
* skipNgTests: whether to skip front end tests. Default is false.
* runE2e: whether to run front end E2E tests. Default is true.
* timeout: shell time out in minutes. Default is 10 minutes. You might want to increase it if your front end has many tests. It also can be used to force close testing Browsers.

### ng-cli-clean
Clean front end files. It will remove all files under ${sourceDir}/dist folder and ${distDir} folder. Default phase is set to clean. A sample usage is:
```$xslt
<execution>
    <id>ng-cli-clean</id>
    <goals>
        <goal>ng-cli-clean</goal>
    </goals>
    <configuration>
        <cleanNode>true</cleanNode>
    </configuration>
</execution>
```
Optional configuration parameter include:
* cleanNode: whether to remove all the files under ${sourceDir}/node_module. Default is false.

## License
MIT