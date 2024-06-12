package com.itworx.vaspp.datacollection.util;

import java.util.Properties;
import org.hibernate.Session;

public class PersistenceAttributes
{
    Class persistenceClass;
    Properties persistenceProperties;
    Session session;
    
    PersistenceAttributes(Class class1,Properties props,Session session)
    {
      this.persistenceClass= class1;
      this.persistenceProperties = props;
      this.session = session;
    }
    Class getPersistenceClass()
    {
      return persistenceClass;
    }
    public Properties getPersistenceProperties()
    {
      return persistenceProperties;
    }
    Session getSession()
    {
      return session;
    }
}