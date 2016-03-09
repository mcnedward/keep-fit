package com.mcnedward.keepfit;

import com.mcnedward.keepfit.utils.enums.Unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ConversionUnitTest {

    @Test
    public void meterConversion_isCorrect() throws Exception {
        double meter = 100;

        double kilometer = Unit.convert(Unit.METER, Unit.KILOMETER, meter);
        double mile = Unit.convert(Unit.METER, Unit.MILE, meter);
        double yard = Unit.convert(Unit.METER, Unit.YARD, meter);
        double step = Unit.convert(Unit.METER, Unit.STEP, meter);

        Assert.assertThat(Unit.format(kilometer), is(equalTo("0.1")));
        Assert.assertThat(Unit.format(mile), is(equalTo("0.06")));
        Assert.assertThat(Unit.format(yard), is(equalTo("109.36")));
        Assert.assertThat(Unit.format(step), is(equalTo("76.2")));
    }

    @Test
    public void kilometerConversion_isCorrect() throws Exception {
        double kilometer = 16;

        double meter = Unit.convert(Unit.KILOMETER, Unit.METER, kilometer);
        double mile = Unit.convert(Unit.KILOMETER, Unit.MILE, kilometer);
        double yard = Unit.convert(Unit.KILOMETER, Unit.YARD, kilometer);
        double step = Unit.convert(Unit.KILOMETER, Unit.STEP, kilometer);

        Assert.assertThat(Unit.format(meter), is(equalTo("16000")));
        Assert.assertThat(Unit.format(mile), is(equalTo("9.94")));
        Assert.assertThat(Unit.format(yard), is(equalTo("17497.76")));
        Assert.assertThat(Unit.format(step), is(equalTo("12192")));
    }

    @Test
    public void mileConversion_isCorrect() throws Exception {
        double mile = 89;

        double meter = Unit.convert(Unit.MILE, Unit.METER, mile);
        double kilometer = Unit.convert(Unit.MILE, Unit.KILOMETER, mile);
        double yard = Unit.convert(Unit.MILE, Unit.YARD, mile);
        double step = Unit.convert(Unit.MILE, Unit.STEP, mile);

        Assert.assertThat(Unit.format(meter), is(equalTo("143231.26")));
        Assert.assertThat(Unit.format(kilometer), is(equalTo("143.23")));
        Assert.assertThat(Unit.format(yard), is(equalTo("156640")));
        Assert.assertThat(Unit.format(step), is(equalTo("187968")));
    }

    @Test
    public void yardConversion_isCorrect() throws Exception {
        double yard = 64;

        double meter = Unit.convert(Unit.YARD, Unit.METER, yard);
        double kilometer = Unit.convert(Unit.YARD, Unit.KILOMETER, yard);
        double mile = Unit.convert(Unit.YARD, Unit.MILE, yard);
        double step = Unit.convert(Unit.YARD, Unit.STEP, yard);

        Assert.assertThat(Unit.format(meter), is(equalTo("58.52")));
        Assert.assertThat(Unit.format(kilometer), is(equalTo("0.06")));
        Assert.assertThat(Unit.format(mile), is(equalTo("0.04")));
        Assert.assertThat(Unit.format(step), is(equalTo("76.8")));
    }

}