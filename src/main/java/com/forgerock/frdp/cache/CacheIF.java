/*
 * Copyright (c) 2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.cache;

import com.forgerock.frdp.common.DataIF;
import java.util.Set;

/**
 * Interface for cache implementations
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface CacheIF {

   public void put(String key, DataIF data) throws Exception;

   public DataIF get(String key) throws Exception;

   public void remove(String key) throws Exception;

   public Set<String> keySet();

   public int size();

   public boolean containsKey(String key);
}
