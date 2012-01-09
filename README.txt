
CreditAnalytics


Lakshmi Krishnamurthy
v1.0, 8 January 2012


Overview

CreditAnalytics is a full featured financial fixed-income credit analytics, credit risk, bond analytics and bond risk library, developed with a special focus towards the needs of the credit trading and bond trading community (CDS, CDX, CDO, and bonds of all types and variants).


Documentation

Detailed documentation of CreditAnalytics functionality may be found at the user documentation. Consult the javadoc for elaborate API usage information.


Installation and Dependencies

The core modules of CreditAnalytics are just two jars:
·	Drip.jar: This contains the complete suite of the entire CreditAnalytics analytics. Download and install this in your classpath.
·	Ojdbc14.jar: This contains the Oracle JDBC drivers needed for access to the reference data (optional). Download and install this in your classpath.


Configuration

All the configuration entries are maintained in the provided Config.xml file. Configuration includes information on the location to the day count files, data tables for the bond static reference data, bond closing marks data, and IR/CDS/treasury closing quotes. Each of this is optional, however, and the consequence of not providing a configuration file is that the defaults will be used. The following are defaults:
·	Day count entries absent - CreditAnalytics uses a comprehensive set of built in day count conventions and holiday calendars across the overwhelming majority of locations, so day count entries are mostly not needed (unless specifically to overwrite the CreditAnalytics's day count/holiday calendar).
·	Bond reference data tables absent - Will not be able to access a bond by its ISIN/CUSIP or any of the identifiers. You will still be able to create user-defined bonds (see next section).
·	Closing curve mark tables absent: Will not be able to retrieve closing IR/credit/treasury/FX curve marks and create those curves. Will still be able to calibrate user-defined curves from custom quotes (see next section).


Getting Started

Once you have downloaded and installed CreditAnalytics, the first step is to set up the configuration by altering the entries provided in the Config.xml file (you can rename it, as long you identify the full path in the initializer - see below). Of course, you don't even need a configuration file - in which case the settings default to the values provided in the previous section.

The sample FIFull.java is the place to start. It contains a comprehensive set of illustrated usage of all the CreditAnalytics API calls.

		java.lang.String strConfig;

		boolean bFIInit = org.drip.service.api.FI.Init (strConfig);

FI.Init initializes the CreditAnalytics library - it takes the optional configuration file as an input. If the initialization is not successful, certain CreditAnalytics functionality will not be available, as the sample demonstrates.


Licence

CreditAnalytics is distributed under the Apache 2.0 licence - please see the attached Licence for details.


Contributors

Lakshmi Krishnamurthy (lakshmi7977@gmail.com)
Kedhar Narayan (kedhar@synergicdesign.com)

