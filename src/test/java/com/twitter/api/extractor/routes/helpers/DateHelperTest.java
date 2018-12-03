package com.twitter.api.extractor.routes.helpers;

import org.junit.Assert;
import org.junit.Test;

public class DateHelperTest {

    private DateHelper dateHelper = new DateHelper();

    @Test
    public void datePlusDaysAssert() {
        final String datePlusDays = dateHelper.getDatePlusDays(
                "2018-10-31", 1);
        Assert.assertEquals("2018-11-01", datePlusDays);
    }
}