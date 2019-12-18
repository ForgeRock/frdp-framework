/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.common;

/**
 * Extends the abstract Data class. Provides "basic" functionality.
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class BasicData extends Data {

   /**
    * Default constructor
    */
   public BasicData() {
      super();
      return;
   }

   /**
    * Creates a new object from an existing DataIF object
    * 
    * @param data existing DataIF object
    */
   public BasicData(DataIF data) {
      super(data);
      return;
   }

   /**
    * This class does not implement the copy() method from the Core interface
    */
   @Override
   public DataIF copy() {
      return new BasicData(this);
   }

}
