/*
 * Copyright (c) 2018-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.dao;

import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.common.Data;

/**
 * Implements the OperationIF interface
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class Operation extends Data implements OperationIF {
   private TYPE _type = OperationIF.TYPE.NULL;

   /**
    * Create operation for the specified type
    * 
    * @param type enum TYPE
    */
   public Operation(TYPE type) {
      super();

      _type = type;

      return;
   }

   /**
    * Create operation using the type of an existing operation
    *
    * @param operation existing operation
    */
   public Operation(OperationIF operation) {
      super(operation);

      if (operation != null) {
         _type = operation.getType();
      } else {
         _type = OperationIF.TYPE.NULL;
      }

      return;
   }

   /**
    * This class does not implement the copy() method from the Core interface
    */
   @Override
   public CoreIF copy() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * Get the operation's type
    * 
    * @return TYPE enum TYPE
    */
   @Override
   public final synchronized TYPE getType() {
      return _type;
   }

   /**
    * Set the operation's type
    * 
    * @param operation enum TYPE
    */
   @Override
   public final synchronized void setType(TYPE operation) {
      _type = operation;

      return;
   }

}
