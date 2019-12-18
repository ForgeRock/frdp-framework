/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.common;

import java.util.Map;
import java.util.Properties;

/**
 * Interface for core implementations
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface CoreIF {

   public static final String BEGIN = "BEGIN: ";
   public static final String END = "END: ";
   public static final String NULL = "(NULL)";

   public enum TSTAMP {

      CREATED, UPDATED, ACCESSED
   };

   public enum STATE {

      NEW, ERROR, READY, NOTREADY, SUCCESS, NOTAUTHORIZED, WARNING, NOTEXIST, FAILED
   };

   public CoreIF copy();

   public void setError(boolean error);

   public boolean isError();

   public void setDebug(boolean debug);

   public boolean isDebug();

   public void setStatus(String status);

   public String getStatus();

   public void setState(STATE state);

   public STATE getState();

   public void setParam(String name, String value);

   public void setParams(Map<String, String> params);

   public void setParams(Properties props);

   public String getParam(String name);

   public String getParamNotEmpty(final String name) throws Exception;

   public Map<String, String> getParams();

   public void mergeParams(Map<String, String> params);

   public void mergeParams(Properties props);

   public void setTStamp(TSTAMP ts);

   public long getTStamp(TSTAMP ts);
}
