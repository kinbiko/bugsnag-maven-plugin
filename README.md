# bugsnag-maven-plugin

[![Build Status](https://travis-ci.org/kinbiko/bugsnag-maven-plugin.svg?branch=master)](https://travis-ci.org/kinbiko/bugsnag-maven-plugin)
[ ![Download](https://api.bintray.com/packages/kinbiko/bugsnag-maven-plugin/bugsnag-maven-plugin/images/download.svg) ](https://bintray.com/kinbiko/bugsnag-maven-plugin/bugsnag-maven-plugin/_latestVersion)

This [Apache Maven](https://maven.apache.org) plugin informs [Bugsnag](https://www.bugsnag.com) of a new deployment, requiring only minimum configuration.

## Usage

Under your `build>plugins` tag add the following.

```
<plugin>
  <groupId>com.kinbiko</groupId>
  <artifactId>bugsnag-maven-plugin</artifactId>
  <version>{See the GitHub releases tab for versions}</version>
  <configuration>
    <!-- Required: -->
    <apiKey>{Your API key here}</apiKey>
    <!-- Optional: -->
    <appVersion>{E.g. 4.1.5}</appVersion> <!-- default: project.version -->
    <releaseStage>{E.g. staging}</releaseStage> <!-- default: production -->
  </configuration>
</plugin>
```

You can then notify Bugsnag that you have deployed a new version of you application through

```mvn bugsnag:deploy```

## License

MIT
