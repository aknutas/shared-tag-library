package controller;

import org.junit.Test;

public class ProgramPropertiesTest {

    @Test
    public void testGetProperty() {
	ProgramProperties pp = ProgramProperties.getInstance();

	pp.setProperty("data::timeout", new Integer(5000));
	pp.setProperty("data::iter_block", new Integer(20));

	assert 20 == ((Integer) pp.setProperty("data::iter_block", new Integer(
		10)));
	assert 5000 == ((Integer) pp.getProperty("data::timeout")).intValue();
	assert 10 == ((Integer) pp.getProperty("data::iter")).intValue();
	assert null == pp.getProperty("foobar::none");
    }

}
