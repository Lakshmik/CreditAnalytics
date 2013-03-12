
package org.drip.analytics.holset;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*
 *    GENERATED on Fri Jan 11 19:54:06 EST 2013 ---- DO NOT DELETE
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

public class BBDHoliday implements org.drip.analytics.holset.LocationHoliday {
	public BBDHoliday()
	{
	}

	public java.lang.String getHolidayLoc()
	{
		return "BBD";
	}

	public org.drip.analytics.holiday.Locale getHolidaySet()
	{
		org.drip.analytics.holiday.Locale lh = new
			org.drip.analytics.holiday.Locale();

		lh.addStaticHoliday ("01-JAN-1998", "New Years Day");

		lh.addStaticHoliday ("21-JAN-1998", "Errol Barrow Day");

		lh.addStaticHoliday ("10-APR-1998", "Good Friday");

		lh.addStaticHoliday ("13-APR-1998", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-1998", "Labour Day");

		lh.addStaticHoliday ("01-JUN-1998", "Whit Monday");

		lh.addStaticHoliday ("03-AUG-1998", "Kadooment Day");

		lh.addStaticHoliday ("05-OCT-1998", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-1998", "Independence Day");

		lh.addStaticHoliday ("25-DEC-1998", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-1999", "New Years Day");

		lh.addStaticHoliday ("21-JAN-1999", "Errol Barrow Day");

		lh.addStaticHoliday ("02-APR-1999", "Good Friday");

		lh.addStaticHoliday ("05-APR-1999", "Easter Monday");

		lh.addStaticHoliday ("24-MAY-1999", "Whit Monday");

		lh.addStaticHoliday ("02-AUG-1999", "Kadooment Day");

		lh.addStaticHoliday ("03-AUG-1999", "Emancipation Day Observed");

		lh.addStaticHoliday ("04-OCT-1999", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-1999", "Independence Day");

		lh.addStaticHoliday ("27-DEC-1999", "Boxing Day Observed");

		lh.addStaticHoliday ("21-JAN-2000", "Errol Barrow Day");

		lh.addStaticHoliday ("21-APR-2000", "Good Friday");

		lh.addStaticHoliday ("24-APR-2000", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2000", "Labour Day");

		lh.addStaticHoliday ("12-JUN-2000", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2000", "Emancipation Day");

		lh.addStaticHoliday ("07-AUG-2000", "Kadooment Day");

		lh.addStaticHoliday ("02-OCT-2000", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2000", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2000", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2000", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2001", "New Years Day");

		lh.addStaticHoliday ("22-JAN-2001", "Errol Barrow Day Observed");

		lh.addStaticHoliday ("13-APR-2001", "Good Friday");

		lh.addStaticHoliday ("16-APR-2001", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2001", "Labour Day");

		lh.addStaticHoliday ("04-JUN-2001", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2001", "Emancipation Day");

		lh.addStaticHoliday ("06-AUG-2001", "Kadooment Day");

		lh.addStaticHoliday ("01-OCT-2001", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2001", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2001", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2001", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2002", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2002", "Errol Barrow Day");

		lh.addStaticHoliday ("29-MAR-2002", "Good Friday");

		lh.addStaticHoliday ("01-APR-2002", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2002", "Labour Day");

		lh.addStaticHoliday ("20-MAY-2002", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2002", "Emancipation Day");

		lh.addStaticHoliday ("05-AUG-2002", "Kadooment Day");

		lh.addStaticHoliday ("07-OCT-2002", "United Nations Day");

		lh.addStaticHoliday ("25-DEC-2002", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2002", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2003", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2003", "Errol Barrow Day");

		lh.addStaticHoliday ("18-APR-2003", "Good Friday");

		lh.addStaticHoliday ("21-APR-2003", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2003", "Labour Day");

		lh.addStaticHoliday ("09-JUN-2003", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2003", "Emancipation Day");

		lh.addStaticHoliday ("04-AUG-2003", "Kadooment Day");

		lh.addStaticHoliday ("06-OCT-2003", "United Nations Day");

		lh.addStaticHoliday ("01-DEC-2003", "Independence Day Observed");

		lh.addStaticHoliday ("25-DEC-2003", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2003", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2004", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2004", "Errol Barrow Day");

		lh.addStaticHoliday ("09-APR-2004", "Good Friday");

		lh.addStaticHoliday ("12-APR-2004", "Easter Monday");

		lh.addStaticHoliday ("31-MAY-2004", "Whit Monday");

		lh.addStaticHoliday ("02-AUG-2004", "Kadooment Day");

		lh.addStaticHoliday ("03-AUG-2004", "Emancipation Day Observed");

		lh.addStaticHoliday ("04-OCT-2004", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2004", "Independence Day");

		lh.addStaticHoliday ("27-DEC-2004", "Boxing Day Observed");

		lh.addStaticHoliday ("21-JAN-2005", "Errol Barrow Day");

		lh.addStaticHoliday ("25-MAR-2005", "Good Friday");

		lh.addStaticHoliday ("28-MAR-2005", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2005", "Labour Day Observed");

		lh.addStaticHoliday ("16-MAY-2005", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2005", "Kadooment Day");

		lh.addStaticHoliday ("02-AUG-2005", "Emancipation Day Observed");

		lh.addStaticHoliday ("03-OCT-2005", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2005", "Independence Day");

		lh.addStaticHoliday ("26-DEC-2005", "Boxing Day");

		lh.addStaticHoliday ("27-DEC-2005", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2006", "New Years Day Observed");

		lh.addStaticHoliday ("14-APR-2006", "Good Friday");

		lh.addStaticHoliday ("17-APR-2006", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2006", "Labour Day");

		lh.addStaticHoliday ("05-JUN-2006", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2006", "Emancipation Day");

		lh.addStaticHoliday ("07-AUG-2006", "Kadooment Day");

		lh.addStaticHoliday ("02-OCT-2006", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2006", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2006", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2006", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2007", "New Years Day");

		lh.addStaticHoliday ("22-JAN-2007", "Errol Barrow Day Observed");

		lh.addStaticHoliday ("06-APR-2007", "Good Friday");

		lh.addStaticHoliday ("09-APR-2007", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2007", "Labour Day");

		lh.addStaticHoliday ("28-MAY-2007", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2007", "Emancipation Day");

		lh.addStaticHoliday ("06-AUG-2007", "Kadooment Day");

		lh.addStaticHoliday ("01-OCT-2007", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2007", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2007", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2007", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2008", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2008", "Errol Barrow Day");

		lh.addStaticHoliday ("21-MAR-2008", "Good Friday");

		lh.addStaticHoliday ("24-MAR-2008", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2008", "Labour Day");

		lh.addStaticHoliday ("12-MAY-2008", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2008", "Emancipation Day");

		lh.addStaticHoliday ("04-AUG-2008", "Kadooment Day");

		lh.addStaticHoliday ("06-OCT-2008", "United Nations Day");

		lh.addStaticHoliday ("01-DEC-2008", "Independence Day Observed");

		lh.addStaticHoliday ("25-DEC-2008", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2008", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2009", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2009", "Errol Barrow Day");

		lh.addStaticHoliday ("10-APR-2009", "Good Friday");

		lh.addStaticHoliday ("13-APR-2009", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2009", "Labour Day");

		lh.addStaticHoliday ("01-JUN-2009", "Whit Monday");

		lh.addStaticHoliday ("03-AUG-2009", "Kadooment Day");

		lh.addStaticHoliday ("05-OCT-2009", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2009", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2009", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2010", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2010", "Errol Barrow Day");

		lh.addStaticHoliday ("02-APR-2010", "Good Friday");

		lh.addStaticHoliday ("05-APR-2010", "Easter Monday");

		lh.addStaticHoliday ("24-MAY-2010", "Whit Monday");

		lh.addStaticHoliday ("02-AUG-2010", "Kadooment Day");

		lh.addStaticHoliday ("03-AUG-2010", "Emancipation Day Observed");

		lh.addStaticHoliday ("04-OCT-2010", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2010", "Independence Day");

		lh.addStaticHoliday ("27-DEC-2010", "Boxing Day Observed");

		lh.addStaticHoliday ("21-JAN-2011", "Errol Barrow Day");

		lh.addStaticHoliday ("22-APR-2011", "Good Friday");

		lh.addStaticHoliday ("25-APR-2011", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2011", "Labour Day Observed");

		lh.addStaticHoliday ("13-JUN-2011", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2011", "Kadooment Day");

		lh.addStaticHoliday ("02-AUG-2011", "Emancipation Day Observed");

		lh.addStaticHoliday ("03-OCT-2011", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2011", "Independence Day");

		lh.addStaticHoliday ("26-DEC-2011", "Boxing Day");

		lh.addStaticHoliday ("27-DEC-2011", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2012", "New Years Day Observed");

		lh.addStaticHoliday ("06-APR-2012", "Good Friday");

		lh.addStaticHoliday ("09-APR-2012", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2012", "Labour Day");

		lh.addStaticHoliday ("28-MAY-2012", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2012", "Emancipation Day");

		lh.addStaticHoliday ("06-AUG-2012", "Kadooment Day");

		lh.addStaticHoliday ("01-OCT-2012", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2012", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2012", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2012", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2013", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2013", "Errol Barrow Day");

		lh.addStaticHoliday ("29-MAR-2013", "Good Friday");

		lh.addStaticHoliday ("01-APR-2013", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2013", "Labour Day");

		lh.addStaticHoliday ("20-MAY-2013", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2013", "Emancipation Day");

		lh.addStaticHoliday ("05-AUG-2013", "Kadooment Day");

		lh.addStaticHoliday ("07-OCT-2013", "United Nations Day");

		lh.addStaticHoliday ("25-DEC-2013", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2013", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2014", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2014", "Errol Barrow Day");

		lh.addStaticHoliday ("18-APR-2014", "Good Friday");

		lh.addStaticHoliday ("21-APR-2014", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2014", "Labour Day");

		lh.addStaticHoliday ("09-JUN-2014", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2014", "Emancipation Day");

		lh.addStaticHoliday ("04-AUG-2014", "Kadooment Day");

		lh.addStaticHoliday ("06-OCT-2014", "United Nations Day");

		lh.addStaticHoliday ("01-DEC-2014", "Independence Day Observed");

		lh.addStaticHoliday ("25-DEC-2014", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2014", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2015", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2015", "Errol Barrow Day");

		lh.addStaticHoliday ("03-APR-2015", "Good Friday");

		lh.addStaticHoliday ("06-APR-2015", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2015", "Labour Day");

		lh.addStaticHoliday ("25-MAY-2015", "Whit Monday");

		lh.addStaticHoliday ("03-AUG-2015", "Kadooment Day");

		lh.addStaticHoliday ("05-OCT-2015", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2015", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2015", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2016", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2016", "Errol Barrow Day");

		lh.addStaticHoliday ("25-MAR-2016", "Good Friday");

		lh.addStaticHoliday ("28-MAR-2016", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2016", "Labour Day Observed");

		lh.addStaticHoliday ("16-MAY-2016", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2016", "Kadooment Day");

		lh.addStaticHoliday ("02-AUG-2016", "Emancipation Day Observed");

		lh.addStaticHoliday ("03-OCT-2016", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2016", "Independence Day");

		lh.addStaticHoliday ("26-DEC-2016", "Boxing Day");

		lh.addStaticHoliday ("27-DEC-2016", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2017", "New Years Day Observed");

		lh.addStaticHoliday ("14-APR-2017", "Good Friday");

		lh.addStaticHoliday ("17-APR-2017", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2017", "Labour Day");

		lh.addStaticHoliday ("05-JUN-2017", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2017", "Emancipation Day");

		lh.addStaticHoliday ("07-AUG-2017", "Kadooment Day");

		lh.addStaticHoliday ("02-OCT-2017", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2017", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2017", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2017", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2018", "New Years Day");

		lh.addStaticHoliday ("22-JAN-2018", "Errol Barrow Day Observed");

		lh.addStaticHoliday ("30-MAR-2018", "Good Friday");

		lh.addStaticHoliday ("02-APR-2018", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2018", "Labour Day");

		lh.addStaticHoliday ("21-MAY-2018", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2018", "Emancipation Day");

		lh.addStaticHoliday ("06-AUG-2018", "Kadooment Day");

		lh.addStaticHoliday ("01-OCT-2018", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2018", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2018", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2018", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2019", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2019", "Errol Barrow Day");

		lh.addStaticHoliday ("19-APR-2019", "Good Friday");

		lh.addStaticHoliday ("22-APR-2019", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2019", "Labour Day");

		lh.addStaticHoliday ("10-JUN-2019", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2019", "Emancipation Day");

		lh.addStaticHoliday ("05-AUG-2019", "Kadooment Day");

		lh.addStaticHoliday ("07-OCT-2019", "United Nations Day");

		lh.addStaticHoliday ("25-DEC-2019", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2019", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2020", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2020", "Errol Barrow Day");

		lh.addStaticHoliday ("10-APR-2020", "Good Friday");

		lh.addStaticHoliday ("13-APR-2020", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2020", "Labour Day");

		lh.addStaticHoliday ("01-JUN-2020", "Whit Monday");

		lh.addStaticHoliday ("03-AUG-2020", "Kadooment Day");

		lh.addStaticHoliday ("05-OCT-2020", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2020", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2020", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2021", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2021", "Errol Barrow Day");

		lh.addStaticHoliday ("02-APR-2021", "Good Friday");

		lh.addStaticHoliday ("05-APR-2021", "Easter Monday");

		lh.addStaticHoliday ("24-MAY-2021", "Whit Monday");

		lh.addStaticHoliday ("02-AUG-2021", "Kadooment Day");

		lh.addStaticHoliday ("03-AUG-2021", "Emancipation Day Observed");

		lh.addStaticHoliday ("04-OCT-2021", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2021", "Independence Day");

		lh.addStaticHoliday ("27-DEC-2021", "Boxing Day Observed");

		lh.addStaticHoliday ("21-JAN-2022", "Errol Barrow Day");

		lh.addStaticHoliday ("15-APR-2022", "Good Friday");

		lh.addStaticHoliday ("18-APR-2022", "Easter Monday");

		lh.addStaticHoliday ("02-MAY-2022", "Labour Day Observed");

		lh.addStaticHoliday ("06-JUN-2022", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2022", "Kadooment Day");

		lh.addStaticHoliday ("02-AUG-2022", "Emancipation Day Observed");

		lh.addStaticHoliday ("03-OCT-2022", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2022", "Independence Day");

		lh.addStaticHoliday ("26-DEC-2022", "Boxing Day");

		lh.addStaticHoliday ("27-DEC-2022", "Christmas Day Observed");

		lh.addStaticHoliday ("02-JAN-2023", "New Years Day Observed");

		lh.addStaticHoliday ("07-APR-2023", "Good Friday");

		lh.addStaticHoliday ("10-APR-2023", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2023", "Labour Day");

		lh.addStaticHoliday ("29-MAY-2023", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2023", "Emancipation Day");

		lh.addStaticHoliday ("07-AUG-2023", "Kadooment Day");

		lh.addStaticHoliday ("02-OCT-2023", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2023", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2023", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2023", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2024", "New Years Day");

		lh.addStaticHoliday ("22-JAN-2024", "Errol Barrow Day Observed");

		lh.addStaticHoliday ("29-MAR-2024", "Good Friday");

		lh.addStaticHoliday ("01-APR-2024", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2024", "Labour Day");

		lh.addStaticHoliday ("20-MAY-2024", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2024", "Emancipation Day");

		lh.addStaticHoliday ("05-AUG-2024", "Kadooment Day");

		lh.addStaticHoliday ("07-OCT-2024", "United Nations Day");

		lh.addStaticHoliday ("25-DEC-2024", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2024", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2025", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2025", "Errol Barrow Day");

		lh.addStaticHoliday ("18-APR-2025", "Good Friday");

		lh.addStaticHoliday ("21-APR-2025", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2025", "Labour Day");

		lh.addStaticHoliday ("09-JUN-2025", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2025", "Emancipation Day");

		lh.addStaticHoliday ("04-AUG-2025", "Kadooment Day");

		lh.addStaticHoliday ("06-OCT-2025", "United Nations Day");

		lh.addStaticHoliday ("01-DEC-2025", "Independence Day Observed");

		lh.addStaticHoliday ("25-DEC-2025", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2025", "Boxing Day");

		lh.addStaticHoliday ("01-JAN-2026", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2026", "Errol Barrow Day");

		lh.addStaticHoliday ("03-APR-2026", "Good Friday");

		lh.addStaticHoliday ("06-APR-2026", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2026", "Labour Day");

		lh.addStaticHoliday ("25-MAY-2026", "Whit Monday");

		lh.addStaticHoliday ("03-AUG-2026", "Kadooment Day");

		lh.addStaticHoliday ("05-OCT-2026", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2026", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2026", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2027", "New Years Day");

		lh.addStaticHoliday ("21-JAN-2027", "Errol Barrow Day");

		lh.addStaticHoliday ("26-MAR-2027", "Good Friday");

		lh.addStaticHoliday ("29-MAR-2027", "Easter Monday");

		lh.addStaticHoliday ("17-MAY-2027", "Whit Monday");

		lh.addStaticHoliday ("02-AUG-2027", "Kadooment Day");

		lh.addStaticHoliday ("03-AUG-2027", "Emancipation Day Observed");

		lh.addStaticHoliday ("04-OCT-2027", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2027", "Independence Day");

		lh.addStaticHoliday ("27-DEC-2027", "Boxing Day Observed");

		lh.addStaticHoliday ("21-JAN-2028", "Errol Barrow Day");

		lh.addStaticHoliday ("14-APR-2028", "Good Friday");

		lh.addStaticHoliday ("17-APR-2028", "Easter Monday");

		lh.addStaticHoliday ("01-MAY-2028", "Labour Day");

		lh.addStaticHoliday ("05-JUN-2028", "Whit Monday");

		lh.addStaticHoliday ("01-AUG-2028", "Emancipation Day");

		lh.addStaticHoliday ("07-AUG-2028", "Kadooment Day");

		lh.addStaticHoliday ("02-OCT-2028", "United Nations Day");

		lh.addStaticHoliday ("30-NOV-2028", "Independence Day");

		lh.addStaticHoliday ("25-DEC-2028", "Christmas Day");

		lh.addStaticHoliday ("26-DEC-2028", "Boxing Day");

		lh.addStandardWeekend();

		return lh;
	}
}
