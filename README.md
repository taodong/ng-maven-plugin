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
Run ng client to build front end. 

### ng-cli-clean
Clean front end files