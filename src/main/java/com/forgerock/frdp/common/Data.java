/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.common;

import org.json.simple.JSONObject;

/**
 * Abstract class that implements the DataIF interface.
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public abstract class Data extends Core implements DataIF {

   private String _data = null;
   private Object _object = null;
   private JSONObject _json = null;

   /**
    * Default constructor
    */
   public Data() {
      super();

      return;
   }

   /**
    * Creates a new object from an existing one
    *
    * @param data existing DataIF object
    */
   public Data(DataIF data) {
      super(data);

      _data = data.getData();

      return;
   }

   /**
    * Get the String value
    *
    * @return String value of the data
    */
   @Override
   public final synchronized String getData() {
      String data = null;

      if (_data != null) {
         data = new String(_data);
      }

      return data;
   }

   /**
    * Set the String value
    * 
    * @param data String value
    */
   @Override
   public final synchronized void setData(String data) {
      if (data != null) {
         _data = new String(data);
      } else {
         _data = null;
      }

      return;
   }

   /**
    * Get the Object
    * 
    * @return Object the Object
    */
   @Override
   public final synchronized Object getObject() {
      return _object;
   }

   /**
    * Set the Object
    * 
    * @param object the Object
    */
   @Override
   public final synchronized void setObject(Object object) {
      _object = object;

      return;
   }

   /**
    * Get the JSON
    * 
    * @return JSONObject the JSON
    */
   @Override
   public final synchronized JSONObject getJSON() {
      return _json;
   }

   /**
    * Set the JSON
    * 
    * @param json the JSONObject
    */
   @Override
   public final synchronized void setJSON(JSONObject json) {
      _json = json;

      return;
   }
}
