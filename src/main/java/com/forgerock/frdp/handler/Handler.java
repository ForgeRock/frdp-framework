/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.handler;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.utils.STR;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that implements the HandlerIF interface
 * 
 * @author Scott Fehrman, ForgeRock Inc.
 */
public abstract class Handler extends Core implements HandlerIF {

   private final String CLASS = this.getClass().getName();
   protected final Logger _logger = Logger.getLogger(this.getClass().getName());
   protected final Level DEBUG_LEVEL = Level.FINE;

   /**
    * Default constructor
    */
   public Handler() {
      super();
   }

   /**
    * Writes the current Status and State to a specific log level
    * 
    * @param level a valid Log level
    */
   @Override
   public void logStateStatus(Level level) {

      _logger.log(level, "{0}: {1}",
            new Object[] { this.getState().toString(), (this.getStatus() != null ? this.getStatus() : NULL) });

      return;
   }

   /* ================= PROTECTED METHODS ================= */

   /**
    * Load paramters from the provided Properties
    *
    * @param propFileName name of the property file
    * @throws Exception
    */
   protected final void loadParamsFromProperties(final String propFileName) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();

      _logger.entering(CLASS, METHOD);

      this.setParams(this.getParamsFromProperties(propFileName));

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * Creates a Map<String, String> from the provided Propety file name
    *
    * @param propFileName name of the property file
    * @return Map<String, String> the params map
    * @throws Exception
    */
   protected final Map<String, String> getParamsFromProperties(final String propFileName) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String key = null;
      String value = null;
      Properties props = null;
      InputStream is = null;
      Map<String, String> params = new HashMap<>();

      _logger.entering(CLASS, METHOD);

      if (!STR.isEmpty(propFileName)) {
         is = getClass().getClassLoader().getResourceAsStream(propFileName);

         if (is != null) {
            props = new Properties();
            props.load(is);

            if (!props.isEmpty()) {
               for (Object obj : props.keySet()) {
                  if (obj != null && obj instanceof String && !STR.isEmpty((String) obj)) {
                     key = (String) obj;
                     value = props.getProperty(key);
                     if (!STR.isEmpty(value)) {
                        params.put(key, value);
                     }
                  }
               }
            }
         } else {
            throw new Exception("Could not get Properties: '" + propFileName + "', InputStream is null");
         }
      }

      _logger.exiting(CLASS, METHOD);

      return params;
   }

}
