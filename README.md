Java API for Google Analytics Measurement Protocol
==================================================

Java API for Google Analytics Measurement Protocol (part of Universal Analytics). This library
is released under liberal Apache Open source License 2.0

Google Analytics Measurement Protocol is new tracking protocol, which will replace the legacy Tracking protocol.
This protocol is documented at https://developers.google.com/analytics/devguides/collection/protocol/v1/

There are few Java implementation of Google Analytics api, but found some issues with each of them.

https://github.com/nhnopensource/universal-analytics-java
* Doesn't implement all parameters
* Cannot specify default parameters

https://github.com/siddii/jgoogleanalytics
*

https://code.google.com/p/jgoogleanalyticstracker/
*

This library implements the measurement protocol with following features.

* Supports all parameters and hit types.
* Default parameters, which would be used for each request
* Type safe data types as appropriate (String, Integer, Double and Boolean)
* Convenient hit specific request types for easy construction
* Synchronous or Asynchronous Event Processing
* Asynchronous processing uses Java Concurrent Executor Service
* Enable or disable the event posting with single configuration value
* Supports Event posting via Proxy
* Gathers some basic information from the underlying Jvm (File Encoding, User Language, Screen Size, Color Depth etc)
* Validates the request and can throw or log warning if validation fails
* Logging uses SLF4J api
