CreditAnalytics

Lakshmi Krishnamurthy
v1.6, 19 July 2012


CreditAnalytics provides the functionality behind creation, calibration, and implementation of the curve, the parameter, and the product interfaces defined in CreditProduct. It also implements a curve/parameter/product/analytics management environment, and has packaged samples and testers.

CreditAnalytics library achieves its design goal by implementing its functionality over several packages:
·	Curve calibration and creation: Functional implementation and creation factories for rates curves, credit curves, and FX curves of al types
·	Market Parameter implementation and creation: Implementation and creation of quotes, component/basket market parameters, as well as scenario parameters.
·	Product implementation and creation: Implementation and creation factories for rates products (cash/EDF/IRS), credit products (bonds/CDS), as well as basket products.
·	Reference data/marks loaders: Loaders for bond/CDX, as well a sub-universe of closing marks
·	Calculation Environment Manager: Implementation of the market parameter container, manager for live/closing curves, stub/client functionality for serverization/distribution, input/output serialization.
·	Samples: Samples for curve, parameters, product, and analytics creation and usage
·	Unit functional testers: Detailed unit scenario test of various analytics, curve, parameter, and product functionality.

Download CreditAnalytics binary along with the complete CreditSuite source from the link here.

To install CreditAnalytics, drop it into the class-path. Use Config.xml to configure custom holidays.

CreditAnalytics depends on the CreditProduct jar to provide the definitions behind the curves, the parameters, and the products. The Oracle ODBC driver is optional – it is used for the ref data connection.

CreditAnalytics is part of CreditSuite – open suite analytics and trading/valuation system for credit products. Detailed documentation and downloads may be found here.

