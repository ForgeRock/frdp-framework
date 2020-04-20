/*
 * Copyright (c) 2015-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.handler;

import com.forgerock.frdp.common.CoreIF;
import java.util.logging.Level;

/**
 * Interface for Handler classes
 * 
 * @author Scott Fehrman, ForgeRock Inc.
 */
public interface HandlerIF extends CoreIF {
   public void logStateStatus(Level level);
}
