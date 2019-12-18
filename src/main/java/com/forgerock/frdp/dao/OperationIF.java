/*
 * Copyright (c) 2018-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.dao;

import com.forgerock.frdp.common.DataIF;

/**
 * Interface which defines operations.
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface OperationIF extends DataIF {

   public enum TYPE {

      NULL, CREATE, READ, REPLACE, DELETE, SEARCH
   };

   public TYPE getType();

   public void setType(TYPE operation);
}
