/*
 * Copyright (c) 2019, ForgeRock, Inc., All rights reserved,
 * Use subject to license terms.
 */

package com.forgerock.frdp.cache;

/**
 * Extends the abstract Cache class. Provides "basic" functionality.
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class BasicCache extends Cache {

   private final String CLASS = this.getClass().getName();

   /**
    * Default constructor
    */
   public BasicCache() {
      super();

      String METHOD = "BasicCache()";

      _logger.entering(CLASS, METHOD);
      _logger.exiting(CLASS, METHOD);

      return;
   }
}
