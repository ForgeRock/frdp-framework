/*
 * Copyright (c) 2018-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.handler;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.utils.STR;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages Handler Objects
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class HandlerManager extends Core implements HandlerManagerIF {

   private Map<String, HandlerIF> _map = null;

   /**
    * Default constructor
    */
   public HandlerManager() {
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
    * Get Map of the Handlers
    *
    * @return Map a map of handlers
    */
   @Override
   public Map<String, HandlerIF> getHandlers() {
      return _map;
   }

   /**
    * Get the Handler names as a Set
    *
    * @return Set handler names
    */
   @Override
   public Set<String> getNames() {
      return _map.keySet();
   }

   /**
    * Check is a given Handler exists
    *
    * @param name the Handler name
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
    * Get the Handler related to the name
    *
    * @param name the Handler name
    * @return HandlerIF the Handler
    */
   @Override
   public HandlerIF getHandler(String name) {
      HandlerIF handler = null;

      if (!STR.isEmpty(name) && _map.containsKey(name)) {
         handler = _map.get(name);
      }

      return handler;
   }

   /**
    * Set the Handler using the specified name
    *
    * @param name the Handler name
    * @param handler the HandlerIF object
    */
   @Override
   public void setHandler(String name, HandlerIF handler) {
      if (!STR.isEmpty(name) && handler != null) {
         _map.put(name, handler);
      }

      return;
   }

}
