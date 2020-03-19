/*
 * Copyright (c) 2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.config;

import com.forgerock.frdp.common.Data;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that implements the ConfigurationIF interface
 *
 * @author Scott Fehrman, ForgeRock Inc.
 */
public abstract class Configuration extends Data implements ConfigurationIF {

   protected final Logger _logger = Logger.getLogger(this.getClass().getName());
   protected final Level DEBUG_LEVEL = Level.FINE;

   /**
    * Default constructor
    */
   public Configuration() {
      super();
   }

}
