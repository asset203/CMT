package com.itworx.vaspp.datacollection.util;

import java.io.File;

import com.itworx.vaspp.datacollection.dynacode.DynaCode;
import com.itworx.vaspp.datacollection.util.converters.AbstractTextConverter;
import com.itworx.vaspp.datacollection.util.converters.TextConverter;

public class DynaCodeUtils {
	
	/*
	public static void main(String[] args) throws Exception {
		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));

		//Postman postman = getPostman();
		TextConverter converter = getTextConverter("TestDynaConverter");

		while (true) {
			System.out.print("Enter a message: ");
			String msg = sysin.readLine();

			//postman.deliverMessage(msg);
			converter.convert(new File[]{new File("c:\\test.text")}, "TEST_SYSTEM");
		}
	}
	*/

	public synchronized static TextConverter getDynaTextConverter(DynaCode dynacode, String dynamicConverterName) {
        dynacode.addSourceDir(new File(PropertyReader.getApplicationPath()+PropertyReader.SYSTEM_SEPARATOR+"resources"+PropertyReader.SYSTEM_SEPARATOR+"dynacode"));
		try{
			return (AbstractTextConverter) Class.forName("com.itworx.vaspp.datacollection.util.converters."+ dynamicConverterName).newInstance();
		} catch (Exception e) {}

		return (TextConverter) dynacode.newProxyInstance(TextConverter.class,"com.itworx.vaspp.datacollection.util.converters."+dynamicConverterName);
	}
}
