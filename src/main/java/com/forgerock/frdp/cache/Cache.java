/*
 * Copyright (c) 2019-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.cache;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.common.DataIF;
import com.forgerock.frdp.utils.STR;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that implements the CacheIF interface.
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public abstract class Cache extends Core implements CacheIF {

   private final String CLASS = this.getClass().getName();
   private final Object _lock = new Object();
   private final ConcurrentMap<String, DataIF> _map = new ConcurrentHashMap<>();
   protected final Logger _logger = Logger.getLogger(CLASS);
   protected final Level DEBUG_LEVEL = Level.FINE;

   /**
    * Default constructor
    */
   public Cache() {
      super();

      String METHOD = "Cache()";

      _logger.entering(CLASS, METHOD);
      _logger.exiting(CLASS, METHOD);

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
    * Add data to the cache using the key. A cache entry with an existing key
    * will be replaced.
    *
    * @param key the unique identifier for the data
    * @param data the DataIF interface object that will be stored in the cache
    * @throws Exception could not save data to cache
    */
   @Override
   public void put(String key, DataIF data) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();

      _logger.entering(CLASS, METHOD);

      if (STR.isEmpty(key)) {
         _logger.log(Level.WARNING, "Key is empty");
         throw new Exception("Key is empty");
      }

      if (data == null) {
         _logger.log(Level.WARNING, "Data is null");
         throw new Exception("Data is null");
      }

      synchronized (_lock) {
         _map.put(key, data);
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * Get the data from the cache that is related to the key. A null value will
    * be returned if an entry does not exist.
    *
    * @param key the unique identifier for the data
    * @return DataIF a DataIF interface object, may be null
    * @throws Exception could not get data from the cache
    */
   @Override
   public DataIF get(String key) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      DataIF data = null;

      _logger.entering(CLASS, METHOD);

      if (!STR.isEmpty(key)) {
         if (_map.containsKey(key)) {
            synchronized (_lock) {
               data = _map.get(key);
            }

            if (data != null) // self cleaning when value is null
            {
               _logger.log(Level.INFO, "Found cache entry, key=''{0}''", key);
            } else {
               synchronized (_lock) {
                  _map.remove(key);
               }
               _logger.log(Level.WARNING, "Removed null cache entry, key=''{0}''", key);
            }
         } else {
            _logger.log(Level.INFO, "No cache entry, key=''{0}''", key);
         }
      } else {
         _logger.log(Level.WARNING, "Key is empty");
         throw new Exception("Key is empty");
      }

      _logger.exiting(CLASS, METHOD);

      return data;
   }

   /**
    * Remove the data from the cache that is related to the key.
    *
    * @param key the unique identifier for the data
    * @throws Exception could not remove data from the cache
    */
   @Override
   public void remove(String key) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();

      _logger.entering(CLASS, METHOD);

      if (!STR.isEmpty(key)) {
         if (_map.containsKey(key)) {
            synchronized (_lock) {
               _map.remove(key);
            }
         } else {
            _logger.log(Level.INFO, "No cache entry, key=''{0}''", key);
         }
      } else {
         _logger.log(Level.WARNING, "Key is empty");
         throw new Exception("Key is empty");
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * Get the Set of keys for all entries in the cache. May be null
    *
    * @return Set keys in the cache
    */
   @Override
   public Set<String> keySet() {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      Set<String> set = null;

      _logger.entering(CLASS, METHOD);

      synchronized (_lock) {
         set = _map.keySet();
      }

      _logger.exiting(CLASS, METHOD);

      return set;
   }

   /**
    * Get the number of items in the cache.
    *
    * @return int the number of entries in the cache
    */
   @Override
   public int size() {
      int size = 0;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();

      _logger.entering(CLASS, METHOD);

      synchronized (_lock) {
         size = _map.size();
      }

      _logger.exiting(CLASS, METHOD);

      return size;
   }

   /**
    * Check if an entry exists in the cache for the key
    *
    * @return boolean True is the entry exists
    */
   @Override
   public boolean containsKey(final String key) {
      boolean exists = false;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();

      _logger.entering(CLASS, METHOD);

      if (!STR.isEmpty(key)) {
         synchronized (_lock) {
            exists = _map.containsKey(key);
         }
      }

      _logger.exiting(CLASS, METHOD);

      return exists;
   }

}
