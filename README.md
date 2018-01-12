# bugsnag-maven-plugin

[![Build Status](https://travis-ci.org/kinbiko/bugsnag-maven-plugin.svg?branch=master)](https://travis-ci.org/kinbiko/bugsnag-maven-plugin)
[ ![Download](https://api.bintray.com/packages/kinbiko/bugsnag-maven-plugin/bugsnag-maven-plugin/images/download.svg) ](https://bintray.com/kinbiko/bugsnag-maven-plugin/bugsnag-maven-plugin/_latestVersion)

This [Apache Maven](https://maven.apache.org) plugin informs [Bugsnag](https://www.bugsnag.com) of a new release of your application, requiring only minimum configuration.

## Usage

Under your `build>plugins` tag add the following.

Please see [The official docs](https://bugsnagbuildapi.docs.apiary.io/#reference/0/build/notify-of-a-build)
for information around what each property means.

```xml
<plugin>
  <groupId>com.kinbiko</groupId>
  <artifactId>bugsnag-maven-plugin</artifactId>
  <version>{See the GitHub releases tab for versions}</version>
  <configuration>
    <!-- Required: -->
    <apiKey>{Your API key here}</apiKey>
    <appVersion>{E.g. 4.1.5}</appVersion> <!-- default: project.version -->

    <!-- Optional: -->
    <builderName>{E.g. Malcolm Reynolds}</builderName> <!-- default: {user.name} -->
    <metadata> <!-- no default value -->
      <!-- Add any metadata you want here. For example: -->
      <myProp>my value</myProp>
    </metadata>
    <sourceControl> <!-- no default value -->
      <repository>{E.g. https://github.com/kinbiko/bugsnag-maven-plugin}</repository>
      <provider>{E.g. github}</provider> <!-- one of: 'github', 'github-enterprise', 'bitbucket', 'bitbucket-server', 'gitlab', 'gitlab-onpremise' -->
      <revision>{E.g. d2a7b36}</revision> <!-- Short or long SHA-1 hash both supported -->
    </sourceControl>
    <releaseStage>{E.g. staging}</releaseStage> <!-- default: production -->
    <autoAssignRelease>true</autoAssignRelease> <!-- no default value -->
  </configuration>
</plugin>
```

You can notify Bugsnag about a new release of your application through:

```bash
mvn bugsnag:release
```

You may also optionally drop the default `releaseStage` with `-Drelease.skipReleaseStage=true`

```bash
mvn bugsnag:release -Drelease.skipReleaseStage=true
```

## License

MIT
