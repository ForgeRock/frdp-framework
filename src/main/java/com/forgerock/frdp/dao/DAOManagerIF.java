/*
 * Copyright (c) 2018-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.dao;

import com.forgerock.frdp.common.CoreIF;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 *
 */
public interface DAOManagerIF extends CoreIF {
   public Map<String, DataAccessIF> getDAOs();

   public Set<String> getNames();

   public boolean containsDAO(String name);

   public DataAccessIF getDAO(String name);

   public void setDAO(String name, DataAccessIF dao);
}
