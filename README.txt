
CreditAnalytics


Lakshmi Krishnamurthy
v1.1, 24 January 2012


Overview

CreditAnalytics is a full featured fixed income credit analytics library, developed with a special focus towards the needs of the credit products community (CDS, CDX, CDO, and bonds of all types and variants).


CreditAnalytics Features

CreditAnalytics captures the valuation, the analytics, and the risk measures calculation for the full set of liquid and semi-liquid credit products. The following is a comprehensive suite of credit products that it handles:
·	Single name credit default swaps (with amortizing coupon and notional schedules, and custom recovery schedules)
·	Portfolio credit default basket swaps (in particular, it covers the full range of liquid and custom CDX/iTRAXX across sectors, etc), again with variable coupon, notional, and custom recovery schedules
·	Basic structured credit products such as nth-to-default basket and its full set of variants, tranches on the standard indices as well as bespoke baskets, squared/cubed structured variants, both in funded and unfunded forms.
·	Comprehensive coverage of all bond types – fixed/floating rate bonds, support for different rate indices and fixings, amortizing/capitalizing bonds, perpetual bonds, European/Bermudan/American embedded option schedules and their variants, fix-to-float on exercise, custom bonds with principal, coupon, and recovery schedules.
·	Optionaly, CreditAnalytics also installs an initial set of bond reference data, bond marks, and IR, treasury, and credit curve closes. Once installed, it can also connect to this database to run analytics and valuation on CDS and bond positions.

CreditAnalytics contains the following built-in set of curve calibration functionality from market quotes:
·	Although not its primary function, CreditAnalytics can bootstrap discount curve from a variety of IR instruments and their quotes – cash/money market instruments, futures (e.g., EDSF), swaps, and treasury quotes.
·	Comprehensive calibration routines for single name credit curves such from CDS, bonds, or a mixture of quotes. Inputs can be in one of CDS quoted measures (fixed coupon flat spreads, upfront points, or fair premium/par spreads), one of bond quotes (e.g., yield/Z Spread, asset swap spread, spread to treasury, I spread, spread to the treasury curve (G Spread) etc), or a mixture of any instrument and their corresponding measure.
·	For basket products, CreditAnalytics provides a comprehensive set of basket basis calibration routines for the credit indices, correlation calibration routines for standard/bespoke tranches, as well as a suite of advanced correlation calibration functionality (such as multi-factor and random-factor correlation calibration, base correlation surface set up, and calibration to the Merton model).
·	With version 1.1 and above, coverage has been introduced for bond baskets and bond ETFs.

Finally, CreditAnalytics also calculates an elaborate sequence of measures relevant to each product. It is built with an enhanced sequence of standard scenario curves that can be used to generate very elaborate scenario measures.


Documentation

Detailed documentation of CreditAnalytics functionality, as well as the product/measures/risk scenarios covered may be found at the user documentation, coverage document, and the developer guide. Consult the javadoc for elaborate API usage information.


Installation and Dependencies

The core modules of CreditAnalytics are just two jars:
·	Drip.jar: This contains the complete suite of the entire CreditAnalytics analytics. Download and install this in your classpath.
·	Ojdbc14.jar: This jar file is entirely optional. This contains the Oracle JDBC drivers needed for access to the reference data (optional). Download and install this in your classpath.


Configuration

All the configuration entries are maintained in the provided Config.xml file. Configuration includes information on the location to the day count files, data tables for the bond static reference data, bond closing marks data, and IR/CDS/treasury closing quotes. Every one of this information is optional, and the consequence of not providing a configuration file is that the defaults will be used. The following are defaults:
·	Day count entries absent – CreditAnalytics uses a comprehensive set of built in day count conventions and holiday calendars across the overwhelming majority of locations, so day count entries are mostly not needed (unless specifically to overwrite the CreditAnalytics ‘s day count/holiday calendar).
·	Bond reference data tables absent – Will not be able to access a bond by its ISIN/CUSIP or any of the identifiers. You will still be able to create all variants of user-defined bonds, from simple fixed rate bonds to complex callable floaters (see next section).
·	Closing curve mark tables absent: Will not be able to retrieve closing IR/credit/treasury/FX curve marks and create those curves. Will still be able to calibrate user-defined curves from custom quotes (see next section).


Getting Started

Once you have downloaded and installed CreditAnalytics, the first step is to set up the configuration by altering the entries provided in the Config.xml file (you can rename it, as long you identify the full path in the initializer – see below). Of course, you don’t need to provide a configuration file – in which case the settings default to the values provided in the previous section.

The sample FIFull.java is the place to start. It contains a comprehensive set of illustrated usage of all the CreditAnalytics API calls.

		java.lang.String strConfig;

		boolean bFIInit = org.drip.service.api.FI.Init (strConfig);

FI.Init initializes the CreditAnalytics library - it takes the optional configuration file as an input. If the initialization is not successful, certain CreditAnalytics functionality will not be available, as the sample demonstrates.

