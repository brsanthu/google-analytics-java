Java API for Google Analytics Measurement Protocol
---------------------------------------------------
Java API for Google Analytics Measurement Protocol (part of Universal Analytics). This library
is released under liberal Apache Open source License 2.0

Google Analytics Measurement Protocol is new tracking protocol, which will replace the legacy Tracking protocol.
This protocol is documented at https://developers.google.com/analytics/devguides/collection/protocol/v1/

Features
--------------
This library implements the measurement protocol with following features.

* Supports all parameters and hit types.
* Able to configure default parameters, which would be used for each request.
* Type safe data types as appropriate (String, Integer, Double and Boolean)
* Convenient hit specific request types for easy construction.
* Synchronous or Asynchronous Event Processing.
* Support for delayed request construction.
* Asynchronous processing uses Java Concurrent Executor Service.
* Uses the latest Apache Http Client (4.3) for high performing event posting.
* Event posting can be enabled/disabled at run time at configuration level.
* Supports connections via Proxy
* Gathers some basic information from the underlying Jvm (File Encoding, User Language, Screen Size, Color Depth etc)
* Validates the request and can throw exception or log warning if validation fails (still wip)
* Logging uses SLF4J api
* Gathers basic stats (number of events posted for each hit type) if requested in the configuration.
* Implementation is Thread Safe
* Jar files are OSGi ready, so could be used with Eclipse
* Build against Java 1.6
* Complete Measurement Protocol parameter information is made available as Javadocs

Examples
-------------

	GoogleAnalytics ga = new GoogleAnalytics("UA-12345678-1");
	ga.post(new PageView("https://www.google.com", "Google Search"));

Or

	GoogleAnalytics ga = new GoogleAnalytics("UA-12345678-1");
	ga.postAsync(new PageView("https://www.google.com", "Google Search"));

Or

	GoogleAnalytics ga = new GoogleAnalytics("UA-12345678-1");
	ga.postAsync(new RequestProvider() {
		public AbstractRequest getRequest() {
			return new PageView("https://www.google.com", "Google Search");
		}
	});

Other Implementations
---------------------

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
