/*
 * Copyright (c) 2018-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.handler;

import com.forgerock.frdp.common.CoreIF;
import java.util.Map;
import java.util.Set;

/**
 * Interface for the Handler Manager
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface HandlerManagerIF extends CoreIF {
   public Map<String, HandlerIF> getHandlers();

   public Set<String> getNames();

   public boolean contains(String name);

   public HandlerIF getHandler(String name);

   public void setHandler(String name, HandlerIF handler);
}
