package com.itworx.vaspp.datacollection.util.sftp;

/*
 * Created on Oct 9, 2005
 *
 */

import java.security.Provider;
import java.security.Security;

/**
 * @author aly.saleh
 * @version 1.0
 * @serialData 9 Oct. 2005
 * 
 */

public class ProviderManager {

	public static void AddProvider(String provider) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Provider p = (Provider) Class.forName(provider).newInstance();
		Security.addProvider(p);
	}

	public static void AddProvider(String[] providers) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (int i = 0; i < providers.length; i++) {
			Provider p = (Provider) Class.forName(providers[i]).newInstance();
			Security.insertProviderAt(p,i+1);
		}
	}

	public static void AddProvider(String provider, int position) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Provider p = (Provider) Class.forName(provider).newInstance();
		Security.insertProviderAt(p, position);
	}
	
	public static Provider[] getProviders()
	{
		return java.security.Security.getProviders();
	}
}
