CreditAnalytics

Lakshmi Krishnamurthy
v2.1, 9 March 2013


CreditAnalytics and CreditProduct are part of CreditSuite – open suite analytics and trading/valuation system for credit products. Detailed documentation and downloads may be found here.

CreditProduct aims to define the functional and behavioral interfaces behind curves, products, and different parameter types (market, valuation, pricing, and product parameters). To facilitate this, it implements various day count conventions, holiday sets, period generators, and calculation outputs. 

CreditProduct library achieves its design goal by implementing its functionality over several packages: 
·	Dates and holidays coverage: Covers a variety of day count conventions, 120+ holiday locations, as well as custom user-defined holidays 
·	Curve and analytics definitions: Defines the base functional interfaces for the variants of discount curves, credit curves, and FX curves 
·	Market Parameter definitions: Defines quotes, component/basket market parameters, and custom scenario parameters 
·	Valuation and Pricing Parameters: Defines valuation, settlement/work-out, and pricing parameters of different variants 
·	Product and product parameter definitions: Defines the product creation and behavior interfaces for Cash/EDF/IRS (all rates), bonds/CDS (credit), and basket bond/CDS, and their feature parameters. 
·	Output measures container: Defines generalized component and basket outputs, as well customized outputs for specific products 

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

