Overview
==
Java API for Google Analytics Measurement Protocol (part of Universal Analytics). This library
is released under liberal Apache Open source License 2.0

Google Analytics Measurement Protocol is new tracking protocol, which will replace the legacy Tracking protocol.
This protocol is documented at https://developers.google.com/analytics/devguides/collection/protocol/v1/

The library is available in Maven Central. Add the following dependency and you are good to go.

Maven:

    <dependency>
        <groupId>net.mikehardy</groupId>
        <artifactId>google-analytics-java</artifactId>
        <version>2.0.3</version>
    </dependency>

Gradle:

    implementation 'net.mikehardy:google-analytics-java:2.0.3'

Others: [Check Here](https://search.maven.org/#artifactdetails%7Cnet.mikehardy%7Cgoogle-analytics-java%7C2.0.0%7Cjar)

To get a local build, do

    git clone https://github.com/mikehardy/google-analytics-java.git
    mvn install

View Javadocs [here](https://www.javadoc.io/doc/net.mikehardy/google-analytics-java) 

1.x vs 2.x
==
Note that Version 2.x has different api than 1.x. Version 2.x is refactoring of majority of structure of the library with goal of making library
easy to use and make it feel fluently. It is based on Java 1.8.

Here is [V1 Readme](https://github.com/net.mikehardy/google-analytics-java/wiki/V1-Readme)

Features
==
This library implements the measurement protocol with following features.

* Supports all parameters and hit types.
* Able to configure default parameters, which would be used for each request.
* Type safe data types as appropriate (String, Integer, Double and Boolean)
* Convenient hit specific request types for easy construction.
* Synchronous or Asynchronous Event Processing.
* Support for delayed request construction.
* Asynchronous processing uses Java Concurrent Executor Service.
* Uses the latest Apache Http or OkHttp client for high performing event posting.
* Event posting can be enabled/disabled at run time at configuration level.
* Supports connections via Proxy
* Gathers some basic information from the underlying Jvm (File Encoding, User Language, Screen Size, Color Depth etc)
* Validates the request and can throw exception or log warning if validation fails (still wip)
* Logging uses SLF4J api
* Gathers basic stats (number of events posted for each hit type) if requested in the configuration.
* Implementation is Thread Safe
* Jar files are OSGi ready, so could be used with Eclipse
* Build against Java 1.8
* Supports batching of requests
* Complete Measurement Protocol parameter information is made available as Javadocs

Usage
==

Init
--
Before using the library to post events, `GoogleAnalytics` instance needs to be initialized. Once it is initialized, same instance can be used
to post events across multiple threads and instance is designed to be thread-safe.

It can be initialized with two types of information. Set of information called configuration (via `GoogleAnalyticsConfig`), which is used by the library and default request settings (`DefaultRequest`), which defines the default attributes for all subsequent requests.

Builder also provides typed methods to set most-relavent attributes of default request for readability.

Simplified initialization with all defaults is as follows.

    ga = GoogleAnalytics.builder()
            .withTrackingId("UA-00000000")
            .build();

To build with custom configuration:

    ga = GoogleAnalytics.builder()
            .withConfig(new GoogleAnalyticsConfig().setBatchingEnabled(true).setBatchSize(10))
            .withTrackingId("UA-00000000")
            .build();

To build with custom configuration and some default request attributes:

    ga = GoogleAnalytics.builder()
            .withConfig(new GoogleAnalyticsConfig().setBatchingEnabled(true).setBatchSize(10))
            .withDefaultRequest(new DefaultRequest().userIp("127.0.0.1").trackingId("UA-00000000"))
            .build();

Note that tracking id can be set to one value for all requests (using default request attributes) or it can be set on per request basis.

Sending Events
--
To send reqeusts, create one of the event type requests, configure the values for that event and call `send()`. 

Here are some examples:

    ga.screenView()
        .sessionControl("start")
        .send();

    ga.pageView()
        .documentTitle(entry.getPage())
        .documentPath("/" + entry.getPage())
        .clientId("Some Id")
        .customDimension(1, "Product")
        .customDimension(1, "Version")
        .userIp("198.165.0.1")
        .send();

    ga.exception()
        .exceptionDescription(e.getMessage())
        .send();

    ga.screenView()
        .sessionControl("end")
        .send();

Async Posting
--
Sending request to Google Analytics is network call and hence it may take a little bit of time. If you would like to avoid this overhead, you can opt in
to send requests asynchronously.

Executor is created to process the requests async with default config of `minThreads=0, maxThreads=5, threadFormat=googleanalyticsjava-thread-{0}, threadTimeoutSecs=300, queueSize=1000. rejectExecutor=CallerRunsPolicy`.

If you want to change these values, configure them before building `GoogleAnalytics` instance. You can also set your own executor in the config, in that case that executor will be used.

To send request async, call `.sendAsync()` instead of `.send()` as follows

    ga.screenView()
        .sessionControl("end")
        .sendAsync();

Batching
--
Google Analytics api supports sending events in batch to reduce the network overhead. Batching is disabled by default but it can be enabled using `batchingEnabled` config. This needs to be set before Google Analytics is built.

Once batching is enabled, usage is same as non-batching. Upon submission, request will be held in a internal list and upon reaching the batch limit, it will be posted to Google api. Note that batching can be used along with Async posting and it work in the same way.

Max batch size is 20 requests and that is the default, which can be changed using config `batchSize`

Master Switch
--
Library provides a master switch with config `enabled`. If set to `false` then requests will be accepted and silently dropped. This config variable can be changed before or after building the `ga` instance.

Discovering Request Parameters
--
Library tries to discover some default request parameters, which is controlled via config `discoverRequestParameters` with default value of `true`. Parameters are discoverd during the building process so it is one time activity.

It discovers following parameters:

* user agent
* user language
* docuemnt encoding
* screen resolution
* screen colors

To discover scren resolution and colors, it needs access to `java.awt`. Since not all environments have access to awt, it is not enabled by default. If would like to use it, set config `requestParameterDiscoverer` to instance of `AwtRequestParameterDiscoverer`

Http Client
--
Library abstracts http client interaction via `HttpClient` interface with default implementation based on Apache HttpClient. If you want to use your own version of http client, set config `httpClient`.


Release Notes
==
Version 2.0.3
--
Compatibility - Altered Core and OkHttpClientImpl so it worked with minSDK / API15 on Android

Version 2.0.2
--
Error - Fixed #11 - not closing OkHttp response body in postBatch()
Enhancement - Fixed #16 - implemented basic sampling strategy with GoogleAnalyticsConfig.setSamplePercentage(int)
Enhancement - request parameters are alphabetically ordered so they are predictable now 
Build - fix javadoc generation on JDK10+

Version 2.0.1 - Oct 02 2018
--
* Implement OkHttp transport as an option

Version 2.0.0 - Jan 24 2018
--
* API redesign based on builder and fluent pattern
* Added support for batching requests

Version 1.1.2 - Apr 29 2015
--
<to be updated>

Version 1.1.1 - May 21 2014
--
* Fixed the issue #14. Https Collection url has been updated to latest one.
* Fixed the issue #15. Added new parameter User Id (uid). As part of this, another change was made to move initializing the default ClientId parameter from GoogleAnalyticsRequest to DefaultRequest. This way, whatever the default clientid you use, will be used for all requests. Previously, default client id wasn't referred.

Version 1.1.0 - Apr 22 2014
--
* Fixed the issue #5. Fix changes some of the existing behavior. If you are using discover system parameters, then by default Screen Colors and Screen Resolution will not be populated. If you would like, you need to set AwtRequestParameterDiscoverer in the GoogleAnalyticsConfig before initializing the GoogleAnalytics. This change is to ensure that it can be used in a environment where JVM has no access to java.awt.* classes.

Version 1.0.5 - Apr 09 2014
--
* Fixed the issue #12

Version 1.0.4 - Mar 3 2014
--
* Fixed the issue #8

Version 1.0.3 - Jan 20 2014
--
* Fixed the issue #6

Other Implementations
==

This is a fork or what I still consider the "upstream" version here: https://github.com/brsanthu/google-analytics-java

Santosh Kumar created what I believe is the best open-source Java google analytics client. My only reason for forking was a desire for a large number of changes rapidly and I didn't see PRs being accepted in the main repo - no other reason and an eventual merge would be fine. Please fork this repo and move forward if I don't respond to you :-)

There are few Java implementation of Google Analytics api, but found some issues (or protocol mismatch) with each of them.

https://github.com/nhnopensource/universal-analytics-java
* Doesn't implement all parameters of Measurement Protocol.
* Cannot specify default parameters
* Only one unit test case and coverage is very minimal
* Uses Legacy Apache Http Client (3.x)

https://code.google.com/p/jgoogleanalyticstracker/
* Implements Legacy Google Analytics protocol

https://github.com/siddii/jgoogleanalytics
* Implements Legacy Google Analytics protocol
