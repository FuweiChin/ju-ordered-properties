package net.bldgos.commons.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class OrderedPropertiesTest {
	@Test
	public void listProperties() {
		Properties props=new OrderedProperties();
		//Properties props=new Properties();
		try {
			try(InputStream in=this.getClass().getResourceAsStream("/application.properties");) {
				props.load(in);
			}
			String[] expectedNames=new String[] {"jdbc.password","jdbc.username","jdbc.url"};
			String[] actualNames=new String[expectedNames.length];
			int i=0;
			for(Enumeration<Object> e=props.keys(); e.hasMoreElements();) {
				actualNames[i++]=(String)e.nextElement();
			}
			Assert.assertArrayEquals(expectedNames, actualNames);
			props.list(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
