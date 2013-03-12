
package org.drip.analytics.holset;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*
 *    GENERATED on Fri Jan 11 19:54:07 EST 2013 ---- DO NOT DELETE
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
 *
 * This file is part of CreditAnalytics, a free-software/open-source library for
 *		fixed income analysts and developers - http://www.credit-trader.org
 *
 * CreditAnalytics is a free, full featured, fixed income credit analytics library, developed with a special focus
 * 		towards the needs of the bonds and credit products community.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

public class LUFHoliday implements org.drip.analytics.holset.LocationHoliday {
	public LUFHoliday()
	{
	}

	public java.lang.String getHolidayLoc()
	{
		return "LUF";
	}

	public org.drip.analytics.holiday.Locale getHolidaySet()
	{
		org.drip.analytics.holiday.Locale lh = new
			org.drip.analytics.holiday.Locale();

		lh.addStaticHoliday ("01-JAN-1998", "New Years Day");

		lh.addStaticHoliday ("13-APR-1998", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-1998", "May Day");

		lh.addStaticHoliday ("21-MAY-1998", "Ascension Day");

		lh.addStaticHoliday ("01-JUN-1998", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-1998", "National Holiday");

		lh.addStaticHoliday ("02-NOV-1998", "All Saints Day Observed");

		lh.addStaticHoliday ("25-DEC-1998", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-1999", "New Years Day");

		lh.addStaticHoliday ("05-APR-1999", "Easter Monday");

		lh.addStaticHoliday ("13-MAY-1999", "Ascension Day");

		lh.addStaticHoliday ("24-MAY-1999", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-1999", "National Holiday");

		lh.addStaticHoliday ("16-AUG-1999", "Assumption Day Observed");

		lh.addStaticHoliday ("01-NOV-1999", "All Saints Day");

		lh.addStaticHoliday ("27-DEC-1999", "St. Stephens Day Observed");

		lh.addStaticHoliday ("24-APR-2000", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2000", "May Day");

		lh.addStaticHoliday ("01-JUN-2000", "Ascension Day");

		lh.addStaticHoliday ("12-JUN-2000", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2000", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2000", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2000", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2000", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2000", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2001", "New Years Day");

		lh.addStaticHoliday ("16-APR-2001", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2001", "May Day");

		lh.addStaticHoliday ("24-MAY-2001", "Ascension Day");

		lh.addStaticHoliday ("04-JUN-2001", "Whit Monday");

		lh.addStaticHoliday ("15-AUG-2001", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2001", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2001", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2001", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2002", "New Years Day");

		lh.addStaticHoliday ("01-APR-2002", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2002", "May Day");

		lh.addStaticHoliday ("09-MAY-2002", "Ascension Day");

		lh.addStaticHoliday ("20-MAY-2002", "Whit Monday");

		lh.addStaticHoliday ("24-JUN-2002", "National Holiday Observed");

		lh.addStaticHoliday ("15-AUG-2002", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2002", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2002", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2002", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2003", "New Years Day");

		lh.addStaticHoliday ("21-APR-2003", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2003", "May Day");

		lh.addStaticHoliday ("29-MAY-2003", "Ascension Day");

		lh.addStaticHoliday ("09-JUN-2003", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2003", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2003", "Assumption Day");

		lh.addStaticHoliday ("25-DEC-2003", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2003", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2004", "New Years Day");

		lh.addStaticHoliday ("12-APR-2004", "Easter Monday");

		lh.addStaticHoliday ("20-MAY-2004", "Ascension Day");

		lh.addStaticHoliday ("31-MAY-2004", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2004", "National Holiday");

		lh.addStaticHoliday ("16-AUG-2004", "Assumption Day Observed");

		lh.addStaticHoliday ("01-NOV-2004", "All Saints Day");

		lh.addStaticHoliday ("27-DEC-2004", "St. Stephens Day Observed");

		lh.addStaticHoliday ("28-MAR-2005", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2005", "May Day Observed");

		lh.addStaticHoliday ("05-MAY-2005", "Ascension Day");

		lh.addStaticHoliday ("16-MAY-2005", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2005", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2005", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2005", "All Saints Day");

		lh.addStaticHoliday ("26-DEC-2005", "St. Stephens Day");

		lh.addStaticHoliday ("27-DEC-2005", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2006", "New Years Day Observed");

		lh.addStaticHoliday ("17-APR-2006", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2006", "May Day");

		lh.addStaticHoliday ("25-MAY-2006", "Ascension Day");

		lh.addStaticHoliday ("05-JUN-2006", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2006", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2006", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2006", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2006", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2006", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2007", "New Years Day");

		lh.addStaticHoliday ("09-APR-2007", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2007", "May Day");

		lh.addStaticHoliday ("17-MAY-2007", "Ascension Day");

		lh.addStaticHoliday ("28-MAY-2007", "Whit Monday");

		lh.addStaticHoliday ("15-AUG-2007", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2007", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2007", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2007", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2008", "New Years Day");

		lh.addStaticHoliday ("24-MAR-2008", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2008", "May Day");

		lh.addStaticHoliday ("12-MAY-2008", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2008", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2008", "Assumption Day");

		lh.addStaticHoliday ("25-DEC-2008", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2008", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2009", "New Years Day");

		lh.addStaticHoliday ("13-APR-2009", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2009", "May Day");

		lh.addStaticHoliday ("21-MAY-2009", "Ascension Day");

		lh.addStaticHoliday ("01-JUN-2009", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2009", "National Holiday");

		lh.addStaticHoliday ("02-NOV-2009", "All Saints Day Observed");

		lh.addStaticHoliday ("25-DEC-2009", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2010", "New Years Day");

		lh.addStaticHoliday ("05-APR-2010", "Easter Monday");

		lh.addStaticHoliday ("13-MAY-2010", "Ascension Day");

		lh.addStaticHoliday ("24-MAY-2010", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2010", "National Holiday");

		lh.addStaticHoliday ("16-AUG-2010", "Assumption Day Observed");

		lh.addStaticHoliday ("01-NOV-2010", "All Saints Day");

		lh.addStaticHoliday ("27-DEC-2010", "St. Stephens Day Observed");

		lh.addStaticHoliday ("25-APR-2011", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2011", "May Day Observed");

		lh.addStaticHoliday ("02-JUN-2011", "Ascension Day");

		lh.addStaticHoliday ("13-JUN-2011", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2011", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2011", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2011", "All Saints Day");

		lh.addStaticHoliday ("26-DEC-2011", "St. Stephens Day");

		lh.addStaticHoliday ("27-DEC-2011", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2012", "New Years Day Observed");

		lh.addStaticHoliday ("09-APR-2012", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2012", "May Day");

		lh.addStaticHoliday ("17-MAY-2012", "Ascension Day");

		lh.addStaticHoliday ("28-MAY-2012", "Whit Monday");

		lh.addStaticHoliday ("15-AUG-2012", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2012", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2012", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2012", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2013", "New Years Day");

		lh.addStaticHoliday ("01-APR-2013", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2013", "May Day");

		lh.addStaticHoliday ("09-MAY-2013", "Ascension Day");

		lh.addStaticHoliday ("20-MAY-2013", "Whit Monday");

		lh.addStaticHoliday ("24-JUN-2013", "National Holiday Observed");

		lh.addStaticHoliday ("15-AUG-2013", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2013", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2013", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2013", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2014", "New Years Day");

		lh.addStaticHoliday ("21-APR-2014", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2014", "May Day");

		lh.addStaticHoliday ("29-MAY-2014", "Ascension Day");

		lh.addStaticHoliday ("09-JUN-2014", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2014", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2014", "Assumption Day");

		lh.addStaticHoliday ("25-DEC-2014", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2014", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2015", "New Years Day");

		lh.addStaticHoliday ("06-APR-2015", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2015", "May Day");

		lh.addStaticHoliday ("14-MAY-2015", "Ascension Day");

		lh.addStaticHoliday ("25-MAY-2015", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2015", "National Holiday");

		lh.addStaticHoliday ("02-NOV-2015", "All Saints Day Observed");

		lh.addStaticHoliday ("25-DEC-2015", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2016", "New Years Day");

		lh.addStaticHoliday ("28-MAR-2016", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2016", "May Day Observed");

		lh.addStaticHoliday ("05-MAY-2016", "Ascension Day");

		lh.addStaticHoliday ("16-MAY-2016", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2016", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2016", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2016", "All Saints Day");

		lh.addStaticHoliday ("26-DEC-2016", "St. Stephens Day");

		lh.addStaticHoliday ("27-DEC-2016", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2017", "New Years Day Observed");

		lh.addStaticHoliday ("17-APR-2017", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2017", "May Day");

		lh.addStaticHoliday ("25-MAY-2017", "Ascension Day");

		lh.addStaticHoliday ("05-JUN-2017", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2017", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2017", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2017", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2017", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2017", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2018", "New Years Day");

		lh.addStaticHoliday ("02-APR-2018", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2018", "May Day");

		lh.addStaticHoliday ("10-MAY-2018", "Ascension Day");

		lh.addStaticHoliday ("21-MAY-2018", "Whit Monday");

		lh.addStaticHoliday ("15-AUG-2018", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2018", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2018", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2018", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2019", "New Years Day");

		lh.addStaticHoliday ("22-APR-2019", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2019", "May Day");

		lh.addStaticHoliday ("30-MAY-2019", "Ascension Day");

		lh.addStaticHoliday ("10-JUN-2019", "Whit Monday");

		lh.addStaticHoliday ("24-JUN-2019", "National Holiday Observed");

		lh.addStaticHoliday ("15-AUG-2019", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2019", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2019", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2019", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2020", "New Years Day");

		lh.addStaticHoliday ("13-APR-2020", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2020", "May Day");

		lh.addStaticHoliday ("21-MAY-2020", "Ascension Day");

		lh.addStaticHoliday ("01-JUN-2020", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2020", "National Holiday");

		lh.addStaticHoliday ("02-NOV-2020", "All Saints Day Observed");

		lh.addStaticHoliday ("25-DEC-2020", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2021", "New Years Day");

		lh.addStaticHoliday ("05-APR-2021", "Easter Monday");

		lh.addStaticHoliday ("13-MAY-2021", "Ascension Day");

		lh.addStaticHoliday ("24-MAY-2021", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2021", "National Holiday");

		lh.addStaticHoliday ("16-AUG-2021", "Assumption Day Observed");

		lh.addStaticHoliday ("01-NOV-2021", "All Saints Day");

		lh.addStaticHoliday ("27-DEC-2021", "St. Stephens Day Observed");

		lh.addStaticHoliday ("18-APR-2022", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2022", "May Day Observed");

		lh.addStaticHoliday ("26-MAY-2022", "Ascension Day");

		lh.addStaticHoliday ("06-JUN-2022", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2022", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2022", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2022", "All Saints Day");

		lh.addStaticHoliday ("26-DEC-2022", "St. Stephens Day");

		lh.addStaticHoliday ("27-DEC-2022", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2023", "New Years Day Observed");

		lh.addStaticHoliday ("10-APR-2023", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2023", "May Day");

		lh.addStaticHoliday ("18-MAY-2023", "Ascension Day");

		lh.addStaticHoliday ("29-MAY-2023", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2023", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2023", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2023", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2023", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2023", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2024", "New Years Day");

		lh.addStaticHoliday ("01-APR-2024", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2024", "May Day");

		lh.addStaticHoliday ("09-MAY-2024", "Ascension Day");

		lh.addStaticHoliday ("20-MAY-2024", "Whit Monday");

		lh.addStaticHoliday ("24-JUN-2024", "National Holiday Observed");

		lh.addStaticHoliday ("15-AUG-2024", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2024", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2024", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2024", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2025", "New Years Day");

		lh.addStaticHoliday ("21-APR-2025", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2025", "May Day");

		lh.addStaticHoliday ("29-MAY-2025", "Ascension Day");

		lh.addStaticHoliday ("09-JUN-2025", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2025", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2025", "Assumption Day");

		lh.addStaticHoliday ("25-DEC-2025", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2025", "St. Stephens Day");

		lh.addStaticHoliday ("01-JAN-2026", "New Years Day");

		lh.addStaticHoliday ("06-APR-2026", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2026", "May Day");

		lh.addStaticHoliday ("14-MAY-2026", "Ascension Day");

		lh.addStaticHoliday ("25-MAY-2026", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2026", "National Holiday");

		lh.addStaticHoliday ("02-NOV-2026", "All Saints Day Observed");

		lh.addStaticHoliday ("25-DEC-2026", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2027", "New Years Day");

		lh.addStaticHoliday ("29-MAR-2027", "Easter Monday");

		lh.addStaticHoliday ("06-MAY-2027", "Ascension Day");

		lh.addStaticHoliday ("17-MAY-2027", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2027", "National Holiday");

		lh.addStaticHoliday ("16-AUG-2027", "Assumption Day Observed");

		lh.addStaticHoliday ("01-NOV-2027", "All Saints Day");

		lh.addStaticHoliday ("27-DEC-2027", "St. Stephens Day Observed");

		lh.addStaticHoliday ("17-APR-2028", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2028", "May Day");

		lh.addStaticHoliday ("25-MAY-2028", "Ascension Day");

		lh.addStaticHoliday ("05-JUN-2028", "Whit Monday");

		lh.addStaticHoliday ("23-JUN-2028", "National Holiday");

		lh.addStaticHoliday ("15-AUG-2028", "Assumption Day");

		lh.addStaticHoliday ("01-NOV-2028", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2028", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2028", "St. Stephens Day");

		lh.addStandardWeekend();

		return lh;
	}
}
