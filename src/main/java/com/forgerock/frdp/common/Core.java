/*
 * Copyright (c) 2015-2021, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.common;

import com.forgerock.frdp.utils.STR;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Abstract class that implements the CoreIF interfaces. Provides core services
 * to classes that extend this class.
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public abstract class Core implements CoreIF {

   private boolean _debug = false;
   private boolean _error = false;
   private String _status = null;
   private STATE _state = STATE.NEW;
   private Map<String, String> _params = null;
   private Map<TSTAMP, Long> _tstamps = null;

   /**
    * Default constructor
    */
   public Core() {
      this.init();

      return;
   }

   /**
    * Create a CoreIF object from an existing CoreIF object
    *
    * @param core existing CoreIF object
    */
   public Core(final CoreIF core) {
      this.init();

      if (core != null) {
         _error = core.isError();
         _status = core.getStatus();
         _state = core.getState();
         _params = core.getParams();

         for (TSTAMP ts : TSTAMP.values()) {
            _tstamps.put(ts, core.getTStamp(ts));
         }
      }

      return;
   }

   /**
    * Create a CoreIF object from a parameters map
    *
    * @param params map of key / value Strings
    */
   public Core(final Map<String, String> params) {
      this.init();

      if (params != null && !params.isEmpty()) {
         _params = new HashMap<>();
         for (String name : params.keySet()) {
            _params.put(name, params.get(name));
         }
      }

      return;
   }
   
   /**
    * Create a CoreIF object from Properties
    * 
    * @param props Properties
    */
   public Core(final Properties props) {
      String value = null;
      
      this.init();
      
      if (props != null && !props.isEmpty()) {
         _params = new HashMap<>();

         for (Object key : props.keySet()) {
            if (key != null && key instanceof String) {
               value = props.getProperty((String) key);
               if (value != null) {
                  value = new String(value);
               }
               _params.put((String) key, value);
            }
         }
      }
      
      return;
   }

   /**
    * Get string representation. Shows error, state, status, and params
    *
    * @return String representation of the object
    */
   @Override
   public String toString() {
      boolean haveParam = false;
      String value = null;
      StringBuilder buf = new StringBuilder();

      buf.append("error=").append(_error).append("; ");
      buf.append("state=").append(_state.toString()).append("; ");
      buf.append("status='").append(_status).append("'; ");

      buf.append("params=");
      if (_params != null && _params.size() > 0) {
         buf.append("{");
         for (String name : _params.keySet()) {
            if (name != null && name.length() > 0) {
               if (haveParam) {
                  buf.append(",");
               }
               haveParam = true;
               buf.append(name).append("='");
               value = _params.get(name);
               if (value != null && value.length() > 0) {
                  buf.append(value);
               }
               buf.append("'");
            }
         }
         buf.append("}");
      } else {
         buf.append("none");
      }

      return buf.toString();
   }

   /**
    * Set the Error flag. If "true" the STATE is set to ERROR
    *
    * @param error boolean
    */
   @Override
   public final synchronized void setError(final boolean error) {
      _error = error;
      if (_error == true) {
         _state = STATE.ERROR;
      }
      return;
   }

   /**
    * Get the value of the Error flag
    *
    * @return boolean is there an error
    */
   @Override
   public final boolean isError() {
      return _error;
   }

   /**
    * Set the value of the Debug flag
    *
    * @param debug true / false
    */
   @Override
   public final synchronized void setDebug(final boolean debug) {
      _debug = debug;
      return;
   }

   /**
    * Get the value of the Debug flag
    *
    * @return boolean
    */
   @Override
   public final boolean isDebug() {
      return _debug;
   }

   /**
    * Set the status attribute
    *
    * @param status String value
    */
   @Override
   public final synchronized void setStatus(String status) {
      if (status != null) {
         _status = new String(status);
      }

      return;
   }

   /**
    * Get the status attribute, may be null
    *
    * @return String value of the attribute
    */
   @Override
   public final synchronized String getStatus() {
      String status = null;

      if (_status != null) {
         status = new String(_status);
      }

      return status;
   }

   /**
    * Set the State attribute. If STATE.ERROR then the Error flag is true
    *
    * @param state enum STATE value
    */
   @Override
   public final synchronized void setState(STATE state) {
      _state = state;

      if (state == STATE.ERROR) {
         _error = true;
      }

      return;
   }

   /**
    * Get the State attribute.
    *
    * @return STATE enum value
    */
   @Override
   public final synchronized STATE getState() {
      return _state;
   }

   /**
    * Set a parameter (key / value pair).
    *
    * @param name the parameter name (not empty)
    * @param value the parameter value
    */
   @Override
   public synchronized void setParam(final String name, final String value) {
      if (!STR.isEmpty(name)) {
         if (_params == null) {
            _params = new HashMap<>();
         }

         if (value == null) {
            _params.put(name, null);
         } else {
            _params.put(name, new String(value));
         }
      }

      return;
   }

   /**
    * Get a parameter value.
    *
    * @param name the parameter's name
    * @return String the parameter's value
    */
   @Override
   public final synchronized String getParam(final String name) {
      String value = null;

      if (_params != null && !STR.isEmpty(name)) {
         value = _params.get(name);
      }

      return value;
   }

   /**
    * Get the parameter value, the value can NOT be empty. If the name is empty,
    * or the value is empty an Exception is thrown
    *
    * @param name the parameter's name
    * @return String the parameter's value
    * @throws Exception if name or value is empty
    */
   @Override
   public synchronized String getParamNotEmpty(final String name) throws Exception {
      String value = null;

      if (STR.isEmpty(name)) {
         throw new Exception("Param name is null");
      }

      value = this.getParam(name);

      if (STR.isEmpty(value)) {
         throw new Exception("Param '" + name + "' is empty");
      }

      return value;
   }

   /**
    * Sets all parameters. Existing parameters are replaced.
    *
    * @param params Map of parameters
    */
   @Override
   public synchronized void setParams(final Map<String, String> params) {
      String value = null;

      if (params != null && params.size() > 0) {
         _params = new HashMap<>();

         for (String name : params.keySet()) {
            value = params.get(name);
            if (value != null) {
               value = new String(value);
            }
            _params.put(name, value);
         }
      }

      return;
   }

   /**
    * Sets all parameters. Existing parameters are replaced.
    *
    * @param props instance of Properties object
    */
   @Override
   public synchronized void setParams(final Properties props) {
      String value = null;

      if (props != null && !props.isEmpty()) {
         _params = new HashMap<>();

         for (Object key : props.keySet()) {
            if (key != null && key instanceof String) {
               value = props.getProperty((String) key);
               if (value != null) {
                  value = new String(value);
               }
               _params.put((String) key, value);
            }
         }
      }

      return;
   }

   /**
    * Get all the parameters as a Map, may be null
    *
    * @return Map parameters
    */
   @Override
   public final synchronized Map<String, String> getParams() {
      String value = null;
      Map<String, String> params = null;

      if (_params != null) {
         params = new HashMap<>();

         for (String name : _params.keySet()) {
            value = _params.get(name);
            if (value != null) {
               value = new String(value);
            }
            params.put(name, value);
         }
      }

      return params;
   }

   /**
    * Merge parameters into existing parameters. If a parameter's name already
    * exists, it will be replaced.
    *
    * @param params Map of parameters
    */
   @Override
   public final synchronized void mergeParams(Map<String, String> params) {
      String value = null;

      if (params != null && !params.isEmpty()) {
         if (_params == null) {
            _params = new HashMap<>();
         }

         for (String name : params.keySet()) {
            value = params.get(name);
            if (value != null) {
               value = new String(value);
            }
            _params.put(name, value);
         }
      }

      return;
   }

   /**
    * Merge parameters into existing parameters. If a parameter's name already
    * exists, it will be replaced.
    *
    * @param props Properties object
    */
   @Override
   public final synchronized void mergeParams(Properties props) {
      String name = null;
      String value = null;

      for (Object key : props.keySet()) {
         if (key != null && key instanceof String) {
            name = (String) key;
            if (!STR.isEmpty(name)) {
               value = props.getProperty(name);
               if (value != null) {
                  value = new String(value);
               }
               _params.put(name, value);
            }
         }
      }

      return;
   }

   /**
    * Set a timestamp's value to the current time.
    *
    * @param ts enum TSTAMP
    */
   @Override
   public final synchronized void setTStamp(final TSTAMP ts) {
      if (ts != TSTAMP.CREATED) {
         _tstamps.put(ts, System.currentTimeMillis());
      }
      return;
   }

   /**
    * Get a timestamp's value.
    *
    * @param ts enum TSTAMP
    */
   @Override
   public final long getTStamp(final TSTAMP ts) {
      long millsec = 0L;

      if (_tstamps.containsKey(ts)) {
         millsec = _tstamps.get(ts);
      }

      return millsec;
   }

   /*
    * =============== PRIVATE METHODS ===============
    */
   private void init() {
      /*
       * Set all the timestamp values to the current time
       */
      Long millsec = System.currentTimeMillis();

      _tstamps = new HashMap<>();

      for (TSTAMP ts : TSTAMP.values()) {
         _tstamps.put(ts, millsec);
      }

      return;
   }
}
