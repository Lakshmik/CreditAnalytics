
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

public class PENHoliday implements org.drip.analytics.holset.LocationHoliday {
	public PENHoliday()
	{
	}

	public java.lang.String getHolidayLoc()
	{
		return "PEN";
	}

	public org.drip.analytics.holiday.Locale getHolidaySet()
	{
		org.drip.analytics.holiday.Locale lh = new
			org.drip.analytics.holiday.Locale();

		lh.addStaticHoliday ("01-JAN-1998", "New Years Day");

		lh.addStaticHoliday ("09-APR-1998", "Holy Thursday");

		lh.addStaticHoliday ("10-APR-1998", "Good Friday");

		lh.addStaticHoliday ("01-MAY-1998", "Labour Day");

		lh.addStaticHoliday ("29-JUN-1998", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-1998", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-1998", "Independence Day");

		lh.addStaticHoliday ("08-OCT-1998", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-1998", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-1998", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-1999", "New Years Day");

		lh.addStaticHoliday ("01-APR-1999", "Holy Thursday");

		lh.addStaticHoliday ("02-APR-1999", "Good Friday");

		lh.addStaticHoliday ("29-JUN-1999", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-1999", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-1999", "Independence Day");

		lh.addStaticHoliday ("30-AUG-1999", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-1999", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-1999", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-1999", "Immaculate Conception");

		lh.addStaticHoliday ("20-APR-2000", "Holy Thursday");

		lh.addStaticHoliday ("21-APR-2000", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2000", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2000", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2000", "Peruvian Day");

		lh.addStaticHoliday ("30-AUG-2000", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2000", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2000", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2000", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2001", "New Years Day");

		lh.addStaticHoliday ("12-APR-2001", "Holy Thursday");

		lh.addStaticHoliday ("13-APR-2001", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2001", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2001", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("30-AUG-2001", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2001", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2001", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2001", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2002", "New Years Day");

		lh.addStaticHoliday ("28-MAR-2002", "Holy Thursday");

		lh.addStaticHoliday ("29-MAR-2002", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2002", "Labour Day");

		lh.addStaticHoliday ("29-JUL-2002", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2002", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2002", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2002", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2002", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2003", "New Years Day");

		lh.addStaticHoliday ("17-APR-2003", "Holy Thursday");

		lh.addStaticHoliday ("18-APR-2003", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2003", "Labour Day");

		lh.addStaticHoliday ("28-JUL-2003", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2003", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2003", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2003", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2003", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2004", "New Years Day");

		lh.addStaticHoliday ("08-APR-2004", "Holy Thursday");

		lh.addStaticHoliday ("09-APR-2004", "Good Friday");

		lh.addStaticHoliday ("29-JUN-2004", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2004", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2004", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2004", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2004", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2004", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2004", "Immaculate Conception");

		lh.addStaticHoliday ("24-MAR-2005", "Holy Thursday");

		lh.addStaticHoliday ("25-MAR-2005", "Good Friday");

		lh.addStaticHoliday ("29-JUN-2005", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2005", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2005", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2005", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2005", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2005", "Immaculate Conception");

		lh.addStaticHoliday ("13-APR-2006", "Holy Thursday");

		lh.addStaticHoliday ("14-APR-2006", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2006", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2006", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2006", "Peruvian Day");

		lh.addStaticHoliday ("30-AUG-2006", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2006", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2006", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2006", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2007", "New Years Day");

		lh.addStaticHoliday ("05-APR-2007", "Holy Thursday");

		lh.addStaticHoliday ("06-APR-2007", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2007", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2007", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("30-AUG-2007", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2007", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2007", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2007", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2008", "New Years Day");

		lh.addStaticHoliday ("20-MAR-2008", "Holy Thursday");

		lh.addStaticHoliday ("21-MAR-2008", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2008", "Labour Day");

		lh.addStaticHoliday ("15-MAY-2008", "");

		lh.addStaticHoliday ("16-MAY-2008", "");

		lh.addStaticHoliday ("28-JUL-2008", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2008", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2008", "Combate de Angamos");

		lh.addStaticHoliday ("20-NOV-2008", "APEC Summit");

		lh.addStaticHoliday ("21-NOV-2008", "APEC Summit");

		lh.addStaticHoliday ("08-DEC-2008", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2008", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2009", "New Years Day");

		lh.addStaticHoliday ("09-APR-2009", "Holy Thursday");

		lh.addStaticHoliday ("10-APR-2009", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2009", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2009", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("27-JUL-2009", "Civil Servants holiday");

		lh.addStaticHoliday ("28-JUL-2009", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2009", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2009", "Combate de Angamos");

		lh.addStaticHoliday ("09-OCT-2009", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2009", "Immaculate Conception");

		lh.addStaticHoliday ("24-DEC-2009", "Christmas Eve Bank Holiday");

		lh.addStaticHoliday ("25-DEC-2009", "Christmas Day");

		lh.addStaticHoliday ("31-DEC-2009", "New Years Eve Bank Holiday");

		lh.addStaticHoliday ("01-JAN-2010", "New Years Day");

		lh.addStaticHoliday ("01-APR-2010", "Holy Thursday");

		lh.addStaticHoliday ("02-APR-2010", "Good Friday");

		lh.addStaticHoliday ("28-JUN-2010", "Country Holiday");

		lh.addStaticHoliday ("29-JUN-2010", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2010", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2010", "Independence Day");

		lh.addStaticHoliday ("30-JUL-2010", "Country Holiday");

		lh.addStaticHoliday ("30-AUG-2010", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2010", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2010", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2010", "Immaculate Conception");

		lh.addStaticHoliday ("21-APR-2011", "Holy Thursday");

		lh.addStaticHoliday ("22-APR-2011", "Good Friday");

		lh.addStaticHoliday ("02-MAY-2011", "Labor Day");

		lh.addStaticHoliday ("29-JUN-2011", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2011", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2011", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2011", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2011", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2011", "Immaculate Conception");

		lh.addStaticHoliday ("05-APR-2012", "Holy Thursday");

		lh.addStaticHoliday ("06-APR-2012", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2012", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2012", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("30-AUG-2012", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2012", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2012", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2012", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2013", "New Years Day");

		lh.addStaticHoliday ("28-MAR-2013", "Holy Thursday");

		lh.addStaticHoliday ("29-MAR-2013", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2013", "Labour Day");

		lh.addStaticHoliday ("29-JUL-2013", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2013", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2013", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2013", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2013", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2014", "New Years Day");

		lh.addStaticHoliday ("17-APR-2014", "Holy Thursday");

		lh.addStaticHoliday ("18-APR-2014", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2014", "Labour Day");

		lh.addStaticHoliday ("28-JUL-2014", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2014", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2014", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2014", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2014", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2015", "New Years Day");

		lh.addStaticHoliday ("02-APR-2015", "Holy Thursday");

		lh.addStaticHoliday ("03-APR-2015", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2015", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2015", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2015", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2015", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2015", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2015", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2015", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2016", "New Years Day");

		lh.addStaticHoliday ("24-MAR-2016", "Holy Thursday");

		lh.addStaticHoliday ("25-MAR-2016", "Good Friday");

		lh.addStaticHoliday ("29-JUN-2016", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2016", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2016", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2016", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2016", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2016", "Immaculate Conception");

		lh.addStaticHoliday ("13-APR-2017", "Holy Thursday");

		lh.addStaticHoliday ("14-APR-2017", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2017", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2017", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2017", "Peruvian Day");

		lh.addStaticHoliday ("30-AUG-2017", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2017", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2017", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2017", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2018", "New Years Day");

		lh.addStaticHoliday ("29-MAR-2018", "Holy Thursday");

		lh.addStaticHoliday ("30-MAR-2018", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2018", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2018", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("30-AUG-2018", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2018", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2018", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2018", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2019", "New Years Day");

		lh.addStaticHoliday ("18-APR-2019", "Holy Thursday");

		lh.addStaticHoliday ("19-APR-2019", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2019", "Labour Day");

		lh.addStaticHoliday ("29-JUL-2019", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2019", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2019", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2019", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2019", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2020", "New Years Day");

		lh.addStaticHoliday ("09-APR-2020", "Holy Thursday");

		lh.addStaticHoliday ("10-APR-2020", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2020", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2020", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2020", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2020", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2020", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2020", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2020", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2021", "New Years Day");

		lh.addStaticHoliday ("01-APR-2021", "Holy Thursday");

		lh.addStaticHoliday ("02-APR-2021", "Good Friday");

		lh.addStaticHoliday ("29-JUN-2021", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2021", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2021", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2021", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2021", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2021", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2021", "Immaculate Conception");

		lh.addStaticHoliday ("14-APR-2022", "Holy Thursday");

		lh.addStaticHoliday ("15-APR-2022", "Good Friday");

		lh.addStaticHoliday ("29-JUN-2022", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2022", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2022", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2022", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2022", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2022", "Immaculate Conception");

		lh.addStaticHoliday ("06-APR-2023", "Holy Thursday");

		lh.addStaticHoliday ("07-APR-2023", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2023", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2023", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2023", "Peruvian Day");

		lh.addStaticHoliday ("30-AUG-2023", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2023", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2023", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2023", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2024", "New Years Day");

		lh.addStaticHoliday ("28-MAR-2024", "Holy Thursday");

		lh.addStaticHoliday ("29-MAR-2024", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2024", "Labour Day");

		lh.addStaticHoliday ("29-JUL-2024", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2024", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2024", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2024", "All Saints Day");

		lh.addStaticHoliday ("25-DEC-2024", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2025", "New Years Day");

		lh.addStaticHoliday ("17-APR-2025", "Holy Thursday");

		lh.addStaticHoliday ("18-APR-2025", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2025", "Labour Day");

		lh.addStaticHoliday ("28-JUL-2025", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2025", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2025", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2025", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2025", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2026", "New Years Day");

		lh.addStaticHoliday ("02-APR-2026", "Holy Thursday");

		lh.addStaticHoliday ("03-APR-2026", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2026", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2026", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2026", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2026", "Independence Day");

		lh.addStaticHoliday ("08-OCT-2026", "Combate de Angamos");

		lh.addStaticHoliday ("08-DEC-2026", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2026", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2027", "New Years Day");

		lh.addStaticHoliday ("25-MAR-2027", "Holy Thursday");

		lh.addStaticHoliday ("26-MAR-2027", "Good Friday");

		lh.addStaticHoliday ("29-JUN-2027", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2027", "Peruvian Day");

		lh.addStaticHoliday ("29-JUL-2027", "Independence Day");

		lh.addStaticHoliday ("30-AUG-2027", "St. Roses Day");

		lh.addStaticHoliday ("08-OCT-2027", "Combate de Angamos");

		lh.addStaticHoliday ("01-NOV-2027", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2027", "Immaculate Conception");

		lh.addStaticHoliday ("13-APR-2028", "Holy Thursday");

		lh.addStaticHoliday ("14-APR-2028", "Good Friday");

		lh.addStaticHoliday ("01-MAY-2028", "Labour Day");

		lh.addStaticHoliday ("29-JUN-2028", "Saints Peter and Paul Day");

		lh.addStaticHoliday ("28-JUL-2028", "Peruvian Day");

		lh.addStaticHoliday ("30-AUG-2028", "St. Roses Day");

		lh.addStaticHoliday ("01-NOV-2028", "All Saints Day");

		lh.addStaticHoliday ("08-DEC-2028", "Immaculate Conception");

		lh.addStaticHoliday ("25-DEC-2028", "Christmas Day");

		lh.addStaticHoliday ("01-JAN-2029", "PEN");

		lh.addStaticHoliday ("29-MAR-2029", "PEN");

		lh.addStaticHoliday ("30-MAR-2029", "PEN");

		lh.addStaticHoliday ("01-MAY-2029", "PEN");

		lh.addStaticHoliday ("29-JUN-2029", "PEN");

		lh.addStaticHoliday ("30-AUG-2029", "PEN");

		lh.addStaticHoliday ("08-OCT-2029", "PEN");

		lh.addStaticHoliday ("01-NOV-2029", "PEN");

		lh.addStaticHoliday ("25-DEC-2029", "PEN");

		lh.addStaticHoliday ("01-JAN-2030", "PEN");

		lh.addStaticHoliday ("18-APR-2030", "PEN");

		lh.addStaticHoliday ("19-APR-2030", "PEN");

		lh.addStaticHoliday ("01-MAY-2030", "PEN");

		lh.addStaticHoliday ("29-JUL-2030", "PEN");

		lh.addStaticHoliday ("30-AUG-2030", "PEN");

		lh.addStaticHoliday ("08-OCT-2030", "PEN");

		lh.addStaticHoliday ("01-NOV-2030", "PEN");

		lh.addStaticHoliday ("25-DEC-2030", "PEN");

		lh.addStaticHoliday ("01-JAN-2031", "PEN");

		lh.addStaticHoliday ("10-APR-2031", "PEN");

		lh.addStaticHoliday ("11-APR-2031", "PEN");

		lh.addStaticHoliday ("01-MAY-2031", "PEN");

		lh.addStaticHoliday ("28-JUL-2031", "PEN");

		lh.addStaticHoliday ("29-JUL-2031", "PEN");

		lh.addStaticHoliday ("08-OCT-2031", "PEN");

		lh.addStaticHoliday ("08-DEC-2031", "PEN");

		lh.addStaticHoliday ("25-DEC-2031", "PEN");

		lh.addStaticHoliday ("01-JAN-2032", "PEN");

		lh.addStaticHoliday ("25-MAR-2032", "PEN");

		lh.addStaticHoliday ("26-MAR-2032", "PEN");

		lh.addStaticHoliday ("29-JUN-2032", "PEN");

		lh.addStaticHoliday ("28-JUL-2032", "PEN");

		lh.addStaticHoliday ("29-JUL-2032", "PEN");

		lh.addStaticHoliday ("30-AUG-2032", "PEN");

		lh.addStaticHoliday ("08-OCT-2032", "PEN");

		lh.addStaticHoliday ("01-NOV-2032", "PEN");

		lh.addStaticHoliday ("08-DEC-2032", "PEN");

		lh.addStaticHoliday ("14-APR-2033", "PEN");

		lh.addStaticHoliday ("15-APR-2033", "PEN");

		lh.addStaticHoliday ("29-JUN-2033", "PEN");

		lh.addStaticHoliday ("28-JUL-2033", "PEN");

		lh.addStaticHoliday ("29-JUL-2033", "PEN");

		lh.addStaticHoliday ("30-AUG-2033", "PEN");

		lh.addStaticHoliday ("01-NOV-2033", "PEN");

		lh.addStaticHoliday ("08-DEC-2033", "PEN");

		lh.addStaticHoliday ("06-APR-2034", "PEN");

		lh.addStaticHoliday ("07-APR-2034", "PEN");

		lh.addStaticHoliday ("01-MAY-2034", "PEN");

		lh.addStaticHoliday ("29-JUN-2034", "PEN");

		lh.addStaticHoliday ("28-JUL-2034", "PEN");

		lh.addStaticHoliday ("30-AUG-2034", "PEN");

		lh.addStaticHoliday ("01-NOV-2034", "PEN");

		lh.addStaticHoliday ("08-DEC-2034", "PEN");

		lh.addStaticHoliday ("25-DEC-2034", "PEN");

		lh.addStaticHoliday ("01-JAN-2035", "PEN");

		lh.addStaticHoliday ("22-MAR-2035", "PEN");

		lh.addStaticHoliday ("23-MAR-2035", "PEN");

		lh.addStaticHoliday ("01-MAY-2035", "PEN");

		lh.addStaticHoliday ("29-JUN-2035", "PEN");

		lh.addStaticHoliday ("30-AUG-2035", "PEN");

		lh.addStaticHoliday ("08-OCT-2035", "PEN");

		lh.addStaticHoliday ("01-NOV-2035", "PEN");

		lh.addStaticHoliday ("25-DEC-2035", "PEN");

		lh.addStaticHoliday ("01-JAN-2036", "PEN");

		lh.addStaticHoliday ("10-APR-2036", "PEN");

		lh.addStaticHoliday ("11-APR-2036", "PEN");

		lh.addStaticHoliday ("01-MAY-2036", "PEN");

		lh.addStaticHoliday ("28-JUL-2036", "PEN");

		lh.addStaticHoliday ("29-JUL-2036", "PEN");

		lh.addStaticHoliday ("08-OCT-2036", "PEN");

		lh.addStaticHoliday ("08-DEC-2036", "PEN");

		lh.addStaticHoliday ("25-DEC-2036", "PEN");

		lh.addStaticHoliday ("01-JAN-2037", "PEN");

		lh.addStaticHoliday ("02-APR-2037", "PEN");

		lh.addStaticHoliday ("03-APR-2037", "PEN");

		lh.addStaticHoliday ("01-MAY-2037", "PEN");

		lh.addStaticHoliday ("29-JUN-2037", "PEN");

		lh.addStaticHoliday ("28-JUL-2037", "PEN");

		lh.addStaticHoliday ("29-JUL-2037", "PEN");

		lh.addStaticHoliday ("08-OCT-2037", "PEN");

		lh.addStaticHoliday ("08-DEC-2037", "PEN");

		lh.addStaticHoliday ("25-DEC-2037", "PEN");

		lh.addStaticHoliday ("01-JAN-2038", "PEN");

		lh.addStaticHoliday ("22-APR-2038", "PEN");

		lh.addStaticHoliday ("23-APR-2038", "PEN");

		lh.addStaticHoliday ("29-JUN-2038", "PEN");

		lh.addStaticHoliday ("28-JUL-2038", "PEN");

		lh.addStaticHoliday ("29-JUL-2038", "PEN");

		lh.addStaticHoliday ("30-AUG-2038", "PEN");

		lh.addStaticHoliday ("08-OCT-2038", "PEN");

		lh.addStaticHoliday ("01-NOV-2038", "PEN");

		lh.addStaticHoliday ("08-DEC-2038", "PEN");

		lh.addStaticHoliday ("07-APR-2039", "PEN");

		lh.addStaticHoliday ("08-APR-2039", "PEN");

		lh.addStaticHoliday ("29-JUN-2039", "PEN");

		lh.addStaticHoliday ("28-JUL-2039", "PEN");

		lh.addStaticHoliday ("29-JUL-2039", "PEN");

		lh.addStaticHoliday ("30-AUG-2039", "PEN");

		lh.addStaticHoliday ("01-NOV-2039", "PEN");

		lh.addStaticHoliday ("08-DEC-2039", "PEN");

		lh.addStaticHoliday ("29-MAR-2040", "PEN");

		lh.addStaticHoliday ("30-MAR-2040", "PEN");

		lh.addStaticHoliday ("01-MAY-2040", "PEN");

		lh.addStaticHoliday ("29-JUN-2040", "PEN");

		lh.addStaticHoliday ("30-AUG-2040", "PEN");

		lh.addStaticHoliday ("08-OCT-2040", "PEN");

		lh.addStaticHoliday ("01-NOV-2040", "PEN");

		lh.addStaticHoliday ("25-DEC-2040", "PEN");

		lh.addStaticHoliday ("01-JAN-2041", "PEN");

		lh.addStaticHoliday ("18-APR-2041", "PEN");

		lh.addStaticHoliday ("19-APR-2041", "PEN");

		lh.addStaticHoliday ("01-MAY-2041", "PEN");

		lh.addStaticHoliday ("29-JUL-2041", "PEN");

		lh.addStaticHoliday ("30-AUG-2041", "PEN");

		lh.addStaticHoliday ("08-OCT-2041", "PEN");

		lh.addStaticHoliday ("01-NOV-2041", "PEN");

		lh.addStaticHoliday ("25-DEC-2041", "PEN");

		lh.addStaticHoliday ("01-JAN-2042", "PEN");

		lh.addStaticHoliday ("03-APR-2042", "PEN");

		lh.addStaticHoliday ("04-APR-2042", "PEN");

		lh.addStaticHoliday ("01-MAY-2042", "PEN");

		lh.addStaticHoliday ("28-JUL-2042", "PEN");

		lh.addStaticHoliday ("29-JUL-2042", "PEN");

		lh.addStaticHoliday ("08-OCT-2042", "PEN");

		lh.addStaticHoliday ("08-DEC-2042", "PEN");

		lh.addStaticHoliday ("25-DEC-2042", "PEN");

		lh.addStaticHoliday ("01-JAN-2043", "PEN");

		lh.addStaticHoliday ("26-MAR-2043", "PEN");

		lh.addStaticHoliday ("27-MAR-2043", "PEN");

		lh.addStaticHoliday ("01-MAY-2043", "PEN");

		lh.addStaticHoliday ("29-JUN-2043", "PEN");

		lh.addStaticHoliday ("28-JUL-2043", "PEN");

		lh.addStaticHoliday ("29-JUL-2043", "PEN");

		lh.addStaticHoliday ("08-OCT-2043", "PEN");

		lh.addStaticHoliday ("08-DEC-2043", "PEN");

		lh.addStaticHoliday ("25-DEC-2043", "PEN");

		lh.addStaticHoliday ("01-JAN-2044", "PEN");

		lh.addStaticHoliday ("14-APR-2044", "PEN");

		lh.addStaticHoliday ("15-APR-2044", "PEN");

		lh.addStaticHoliday ("29-JUN-2044", "PEN");

		lh.addStaticHoliday ("28-JUL-2044", "PEN");

		lh.addStaticHoliday ("29-JUL-2044", "PEN");

		lh.addStaticHoliday ("30-AUG-2044", "PEN");

		lh.addStaticHoliday ("01-NOV-2044", "PEN");

		lh.addStaticHoliday ("08-DEC-2044", "PEN");

		lh.addStaticHoliday ("06-APR-2045", "PEN");

		lh.addStaticHoliday ("07-APR-2045", "PEN");

		lh.addStaticHoliday ("01-MAY-2045", "PEN");

		lh.addStaticHoliday ("29-JUN-2045", "PEN");

		lh.addStaticHoliday ("28-JUL-2045", "PEN");

		lh.addStaticHoliday ("30-AUG-2045", "PEN");

		lh.addStaticHoliday ("01-NOV-2045", "PEN");

		lh.addStaticHoliday ("08-DEC-2045", "PEN");

		lh.addStaticHoliday ("25-DEC-2045", "PEN");

		lh.addStaticHoliday ("01-JAN-2046", "PEN");

		lh.addStaticHoliday ("22-MAR-2046", "PEN");

		lh.addStaticHoliday ("23-MAR-2046", "PEN");

		lh.addStaticHoliday ("01-MAY-2046", "PEN");

		lh.addStaticHoliday ("29-JUN-2046", "PEN");

		lh.addStaticHoliday ("30-AUG-2046", "PEN");

		lh.addStaticHoliday ("08-OCT-2046", "PEN");

		lh.addStaticHoliday ("01-NOV-2046", "PEN");

		lh.addStaticHoliday ("25-DEC-2046", "PEN");

		lh.addStaticHoliday ("01-JAN-2047", "PEN");

		lh.addStaticHoliday ("11-APR-2047", "PEN");

		lh.addStaticHoliday ("12-APR-2047", "PEN");

		lh.addStaticHoliday ("01-MAY-2047", "PEN");

		lh.addStaticHoliday ("29-JUL-2047", "PEN");

		lh.addStaticHoliday ("30-AUG-2047", "PEN");

		lh.addStaticHoliday ("08-OCT-2047", "PEN");

		lh.addStaticHoliday ("01-NOV-2047", "PEN");

		lh.addStaticHoliday ("25-DEC-2047", "PEN");

		lh.addStaticHoliday ("01-JAN-2048", "PEN");

		lh.addStaticHoliday ("02-APR-2048", "PEN");

		lh.addStaticHoliday ("03-APR-2048", "PEN");

		lh.addStaticHoliday ("01-MAY-2048", "PEN");

		lh.addStaticHoliday ("29-JUN-2048", "PEN");

		lh.addStaticHoliday ("28-JUL-2048", "PEN");

		lh.addStaticHoliday ("29-JUL-2048", "PEN");

		lh.addStaticHoliday ("08-OCT-2048", "PEN");

		lh.addStaticHoliday ("08-DEC-2048", "PEN");

		lh.addStaticHoliday ("25-DEC-2048", "PEN");

		lh.addStaticHoliday ("01-JAN-2049", "PEN");

		lh.addStaticHoliday ("15-APR-2049", "PEN");

		lh.addStaticHoliday ("16-APR-2049", "PEN");

		lh.addStaticHoliday ("29-JUN-2049", "PEN");

		lh.addStaticHoliday ("28-JUL-2049", "PEN");

		lh.addStaticHoliday ("29-JUL-2049", "PEN");

		lh.addStaticHoliday ("30-AUG-2049", "PEN");

		lh.addStaticHoliday ("08-OCT-2049", "PEN");

		lh.addStaticHoliday ("01-NOV-2049", "PEN");

		lh.addStaticHoliday ("08-DEC-2049", "PEN");

		lh.addStaticHoliday ("07-APR-2050", "PEN");

		lh.addStaticHoliday ("08-APR-2050", "PEN");

		lh.addStaticHoliday ("29-JUN-2050", "PEN");

		lh.addStaticHoliday ("28-JUL-2050", "PEN");

		lh.addStaticHoliday ("29-JUL-2050", "PEN");

		lh.addStaticHoliday ("30-AUG-2050", "PEN");

		lh.addStaticHoliday ("01-NOV-2050", "PEN");

		lh.addStaticHoliday ("08-DEC-2050", "PEN");

		lh.addStaticHoliday ("30-MAR-2051", "PEN");

		lh.addStaticHoliday ("31-MAR-2051", "PEN");

		lh.addStaticHoliday ("01-MAY-2051", "PEN");

		lh.addStaticHoliday ("29-JUN-2051", "PEN");

		lh.addStaticHoliday ("28-JUL-2051", "PEN");

		lh.addStaticHoliday ("30-AUG-2051", "PEN");

		lh.addStaticHoliday ("01-NOV-2051", "PEN");

		lh.addStaticHoliday ("08-DEC-2051", "PEN");

		lh.addStaticHoliday ("25-DEC-2051", "PEN");

		lh.addStaticHoliday ("01-JAN-2052", "PEN");

		lh.addStaticHoliday ("18-APR-2052", "PEN");

		lh.addStaticHoliday ("19-APR-2052", "PEN");

		lh.addStaticHoliday ("01-MAY-2052", "PEN");

		lh.addStaticHoliday ("29-JUL-2052", "PEN");

		lh.addStaticHoliday ("30-AUG-2052", "PEN");

		lh.addStaticHoliday ("08-OCT-2052", "PEN");

		lh.addStaticHoliday ("01-NOV-2052", "PEN");

		lh.addStaticHoliday ("25-DEC-2052", "PEN");

		lh.addStaticHoliday ("01-JAN-2053", "PEN");

		lh.addStaticHoliday ("03-APR-2053", "PEN");

		lh.addStaticHoliday ("04-APR-2053", "PEN");

		lh.addStaticHoliday ("01-MAY-2053", "PEN");

		lh.addStaticHoliday ("28-JUL-2053", "PEN");

		lh.addStaticHoliday ("29-JUL-2053", "PEN");

		lh.addStaticHoliday ("08-OCT-2053", "PEN");

		lh.addStaticHoliday ("08-DEC-2053", "PEN");

		lh.addStaticHoliday ("25-DEC-2053", "PEN");

		lh.addStaticHoliday ("01-JAN-2054", "PEN");

		lh.addStaticHoliday ("26-MAR-2054", "PEN");

		lh.addStaticHoliday ("27-MAR-2054", "PEN");

		lh.addStaticHoliday ("01-MAY-2054", "PEN");

		lh.addStaticHoliday ("29-JUN-2054", "PEN");

		lh.addStaticHoliday ("28-JUL-2054", "PEN");

		lh.addStaticHoliday ("29-JUL-2054", "PEN");

		lh.addStaticHoliday ("08-OCT-2054", "PEN");

		lh.addStaticHoliday ("08-DEC-2054", "PEN");

		lh.addStaticHoliday ("25-DEC-2054", "PEN");

		lh.addStaticHoliday ("01-JAN-2055", "PEN");

		lh.addStaticHoliday ("15-APR-2055", "PEN");

		lh.addStaticHoliday ("16-APR-2055", "PEN");

		lh.addStaticHoliday ("29-JUN-2055", "PEN");

		lh.addStaticHoliday ("28-JUL-2055", "PEN");

		lh.addStaticHoliday ("29-JUL-2055", "PEN");

		lh.addStaticHoliday ("30-AUG-2055", "PEN");

		lh.addStaticHoliday ("08-OCT-2055", "PEN");

		lh.addStaticHoliday ("01-NOV-2055", "PEN");

		lh.addStaticHoliday ("08-DEC-2055", "PEN");

		lh.addStaticHoliday ("30-MAR-2056", "PEN");

		lh.addStaticHoliday ("31-MAR-2056", "PEN");

		lh.addStaticHoliday ("01-MAY-2056", "PEN");

		lh.addStaticHoliday ("29-JUN-2056", "PEN");

		lh.addStaticHoliday ("28-JUL-2056", "PEN");

		lh.addStaticHoliday ("30-AUG-2056", "PEN");

		lh.addStaticHoliday ("01-NOV-2056", "PEN");

		lh.addStaticHoliday ("08-DEC-2056", "PEN");

		lh.addStaticHoliday ("25-DEC-2056", "PEN");

		lh.addStaticHoliday ("01-JAN-2057", "PEN");

		lh.addStaticHoliday ("19-APR-2057", "PEN");

		lh.addStaticHoliday ("20-APR-2057", "PEN");

		lh.addStaticHoliday ("01-MAY-2057", "PEN");

		lh.addStaticHoliday ("29-JUN-2057", "PEN");

		lh.addStaticHoliday ("30-AUG-2057", "PEN");

		lh.addStaticHoliday ("08-OCT-2057", "PEN");

		lh.addStaticHoliday ("01-NOV-2057", "PEN");

		lh.addStaticHoliday ("25-DEC-2057", "PEN");

		lh.addStaticHoliday ("01-JAN-2058", "PEN");

		lh.addStaticHoliday ("11-APR-2058", "PEN");

		lh.addStaticHoliday ("12-APR-2058", "PEN");

		lh.addStaticHoliday ("01-MAY-2058", "PEN");

		lh.addStaticHoliday ("29-JUL-2058", "PEN");

		lh.addStaticHoliday ("30-AUG-2058", "PEN");

		lh.addStaticHoliday ("08-OCT-2058", "PEN");

		lh.addStaticHoliday ("01-NOV-2058", "PEN");

		lh.addStaticHoliday ("25-DEC-2058", "PEN");

		lh.addStaticHoliday ("01-JAN-2059", "PEN");

		lh.addStaticHoliday ("27-MAR-2059", "PEN");

		lh.addStaticHoliday ("28-MAR-2059", "PEN");

		lh.addStaticHoliday ("01-MAY-2059", "PEN");

		lh.addStaticHoliday ("28-JUL-2059", "PEN");

		lh.addStaticHoliday ("29-JUL-2059", "PEN");

		lh.addStaticHoliday ("08-OCT-2059", "PEN");

		lh.addStaticHoliday ("08-DEC-2059", "PEN");

		lh.addStaticHoliday ("25-DEC-2059", "PEN");

		lh.addStaticHoliday ("01-JAN-2060", "PEN");

		lh.addStaticHoliday ("15-APR-2060", "PEN");

		lh.addStaticHoliday ("16-APR-2060", "PEN");

		lh.addStaticHoliday ("29-JUN-2060", "PEN");

		lh.addStaticHoliday ("28-JUL-2060", "PEN");

		lh.addStaticHoliday ("29-JUL-2060", "PEN");

		lh.addStaticHoliday ("30-AUG-2060", "PEN");

		lh.addStaticHoliday ("08-OCT-2060", "PEN");

		lh.addStaticHoliday ("01-NOV-2060", "PEN");

		lh.addStaticHoliday ("08-DEC-2060", "PEN");

		lh.addStandardWeekend();

		return lh;
	}
}
