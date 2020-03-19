/*
 * Copyright (c) 2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.config;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.utils.STR;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages Configuration Objects
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class ConfigurationManager extends Core implements ConfigurationManagerIF {

   private Map<String, ConfigurationIF> _map = null;

   /**
    * Default constructor
    */
   public ConfigurationManager() {
      super();

      _map = new HashMap<>();

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
    * Get Map of the Configurations
    *
    * @return _map a Map of configurations
    */
   @Override
   public Map<String, ConfigurationIF> getConfigurations() {
      return _map;
   }

   /**
    * Get the Configuration names as a Set
    *
    * @return Set configuration names
    */
   @Override
   public Set<String> getNames() {
      return _map.keySet();
   }

   /**
    * Check is a given Configuration exists
    *
    * @param name the Configuration name
    * @return boolean
    */
   @Override
   public boolean contains(String name) {
      boolean found = false;

      if (!STR.isEmpty(name)) {
         found = _map.containsKey(name);
      }

      return found;
   }

   /**
    * Get the Configuration related to the name
    *
    * @param name the Configuration name
    * @return ConfigurationIF the Configuration
    */
   @Override
   public ConfigurationIF getConfiguration(String name) {
      ConfigurationIF configuration = null;

      if (!STR.isEmpty(name) && _map.containsKey(name)) {
         configuration = _map.get(name);
      }

      return configuration;
   }

   /**
    * Set the Configuration using the specified name
    *
    * @param name the Configuration name
    * @param config the ConfigurationIF object
    */
   @Override
   public void setConfiguration(String name, ConfigurationIF config) {
      if (!STR.isEmpty(name) && config != null) {
         _map.put(name, config);
      }

      return;
   }

}
