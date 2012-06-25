
CreditAnalytics


Lakshmi Krishnamurthy
v1.3, 23 March 2012


If you are looking to ...

- Create IR discount curve from rates, or by calibration from quotes of cash/money market LIBOR/swap/future
- Create credit curve from hazard rate or survival probabilites, or from quotes of bonds/CDS/other credit sensitive instruments
- Create basis and correlation curves using single name and credit basket products
- Build FX Spot, forward, and FX basis curves from a variety of inputs
- Day counts of all kinds, holiday calendars for 150+ jurisdictions, and weirdest date adjustment rules
- Pull up a complete description of a bond using its ISIN/CUSIP/other standard identifier, and calc price, yield, G/I/Z Spread, bond credit basis or any other relative value metric, or
- Build your own bond or semi-funded note using any of the numerous bells
- Pull in all the ref data for a given bond (incl. non-valuation details such as issue dates, notional, domicile, option schedule), or for all the bonds for the given issuer/sector/rating etc
- Build a standard CDS/CDX/CDO instrument by code, or a bespoke one using the numerous built-in custom features
- Pull historical and live IR/CDS/CDX/CDO/bond quotes, analytics
_ Generate 1000+ measures for each of the product for a given scenario, or
- Generate valuations for fully customizable scenario adjustment
- All these in a set of very simple, elegant group of APIs

... CreditAnalytics does all this (and more)!


To learn more ...

Check out the detailed documentation of at the user documentation site. javadoc gives elaborate API usage information.


All you need is to install is a single jar file. Really.

It is called drip.jar (with version info appended). This contains the complete suite of the entire CreditAnalytics analytics. Drop this in the classpath.

You may optionally need Ojdbc14.jar - the Oracle JDBC drivers needed for access to the reference data. Again in your classpath it goes.


Configuring is simple too, 

Built-in configuration covers a humoungous variety of day count conventions, calendars, and date adjustment rules. Unhappy with any factory default? Examine it first by using one of the API calls, then muck with appropriate Config.xml entry in the config directory, and use this file as the start up.


After installation, start by

 ... altering the entries provided in the Config.xml file (you can rename it, as long you identify the full path in the initializer - see below). Of course, you don't even need a configuration file - in which case the settings default to the values provided in the previous section.

Any of the samples in the examples folder would be the place to start. They contain a comprehensive set of illustrated usage of all the CreditAnalytics API calls. They all start as:

		java.lang.String strConfig;

		boolean bFIInit = org.drip.service.api.FI.Init (strConfig);

FI.Init initializes the CreditAnalytics library - it takes the optional configuration file as an input. If the initialization is not successful, certain CreditAnalytics functionality will not be available, as the sample demonstrates.

Examples folder contains samples for bond/CDS/CDX, show historical/live data extractions, and their eventual analytics.


Licence

CreditAnalytics is distributed under the Apache 2.0 licence - please see the attached Licence for details.


Contributors

Lakshmi Krishnamurthy (lakshmi7977@gmail.com)
