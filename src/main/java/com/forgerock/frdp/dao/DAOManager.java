/*
 * Copyright (c) 2018-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.dao;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.utils.STR;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages Data Access Objects
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class DAOManager extends Core implements DAOManagerIF {

   private Map<String, DataAccessIF> _map = null;

   /**
    * Default constructor
    */
   public DAOManager() {
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
    * Get the Map of Data Access Objects
    * 
    * @return Map<String, DataAccessIF> Data Access Objects
    */
   @Override
   public Map<String, DataAccessIF> getDAOs() {
      return _map;
   }

   /**
    * Get the Data Access Object's names
    * 
    * @return Set<String> DAO names
    */
   @Override
   public Set<String> getNames() {
      return _map.keySet();
   }

   /**
    * Check if a Data Access Object exists, for a given name.
    * 
    * @param name the DAO's name
    * @return boolean does the DAO exist
    */
   @Override
   public boolean containsDAO(String name) {
      boolean found = false;

      if (!STR.isEmpty(name)) {
         found = _map.containsKey(name);
      }

      return found;
   }

   /**
    * Get the Data Access Object for a given name.
    * 
    * @param name the DAO's name
    * @return DataAcessIF the Data Access Object
    */
   @Override
   public DataAccessIF getDAO(String name) {
      DataAccessIF dao = null;

      if (!STR.isEmpty(name) && _map.containsKey(name)) {
         dao = _map.get(name);
      }

      return dao;
   }

   /**
    * Set the Data Access Object for the given name.
    * 
    * @param name the DAO's name
    * @param dao  the Data Access Object
    */
   @Override
   public void setDAO(String name, DataAccessIF dao) {
      if (!STR.isEmpty(name) && dao != null) {
         _map.put(name, dao);
      }

      return;
   }

}
