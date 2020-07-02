/*
 * Copyright (c) 2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.config;

import com.forgerock.frdp.common.CoreIF;
import java.util.Map;
import java.util.Set;

/**
 * Interface for the Configuration Manager
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface ConfigurationManagerIF extends CoreIF {

   public Map<String, ConfigurationIF> getConfigurations();

   public Set<String> getNames();

   public boolean contains(String name);

   public ConfigurationIF getConfiguration(String name);

   public void setConfiguration(String name, ConfigurationIF configuration);
}
