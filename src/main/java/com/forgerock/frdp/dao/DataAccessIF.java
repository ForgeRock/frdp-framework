/*
 * Copyright (c) 2018-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.dao;

import com.forgerock.frdp.common.CoreIF;

/**
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface DataAccessIF extends CoreIF {

   public OperationIF execute(OperationIF data);

   public void close();
}
