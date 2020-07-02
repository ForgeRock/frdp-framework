/*
 * Copyright (c) 2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.config;

/**
 * Extends the abstract Configuration class. Provides "basic" functionality.
 *
 * @author Scott Fehrman, ForgeRock Inc.
 */
public class BasicConfiguration extends Configuration {

   /**
    * Default constructor
    */
   public BasicConfiguration() {
      super();
      return;
   }

   /**
    * This class does not implement the copy() method from the Core interface
    */
   @Override
   public ConfigurationIF copy() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
