/*
 * Copyright (c) 2015-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.json;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.utils.STR;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Manages the access to JSON files. Reads file system JSON files and exposes
 * them as JSONObjects.
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public class JsonDataFactory extends Core {

   private final String CLASS = this.getClass().getName();
   private final Logger _logger = Logger.getLogger(this.getClass().getName());
   private String _propName = null;

   /**
    * Constructor
    *
    * @param propFile name of the Property file in the relative file system
    * @param propName name of a Property who's value is the JSON file name
    * @throws Exception could not create instance of Factory
    */
   public JsonDataFactory(String propFile, String propName) throws Exception {
      super();

      String METHOD = "JsonDataFactory(propFile, propName)";
      Properties props = null;
      InputStream is = null;

      _logger.entering(CLASS, METHOD);

      if (_logger.isLoggable(Level.FINE)) {
         _logger.log(Level.FINE, "propFile=''{0}'', propName=''{1}''",
            new Object[]{propFile != null ? propFile : NULL, propName != null ? propName : NULL});
      }

      if (STR.isEmpty(propFile)) {
         throw new Exception("Argument 'propFile' is empty");
      }

      if (STR.isEmpty(propName)) {
         throw new Exception("Argument 'propName' is empty");
      } else {
         _propName = propName;
      }

      // is = new FileInputStream(propFile);
      try {
         is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile);
      } catch (Exception ex) {
         this.setState(STATE.ERROR);
         this.setStatus("Exception: getResourceAsStream(propFile): " + ex.getMessage());
         throw new Exception(this.getStatus());
      }

      props = new Properties();

      try {
         props.load(is);
      } catch (Exception ex) {
         this.setState(STATE.ERROR);
         this.setStatus("Exception: props.load(is): " + ex.getMessage());
         throw new Exception(this.getStatus());
      }

      try {
         is.close();
      } catch (Exception ex) {
         this.setState(STATE.ERROR);
         this.setStatus("Exception: is.close(): " + ex.getMessage());
         throw new Exception(this.getStatus());
      }

      if (!props.isEmpty()) {
         this.mergeParams(props);
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * This class does not implement the copy() method from the Core interface
    */
   @Override
   public CoreIF copy() {
      throw new UnsupportedOperationException("Not supported.");
   }

   /**
    * Get the JSONObject. Uses the value from the Property which contains the
    * name of the JSON file in the file system.
    *
    * @return JSONObject the JSON object representing the JSON file
    * @throws Exception could not get the JSONObject instance
    */
   public JSONObject getData() throws Exception {
      Object obj = null;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String configFile = null;
      FileReader reader = null;
      JSONParser parser = null;
      JSONObject jObject = null;

      _logger.entering(CLASS, METHOD);

      if (_logger.isLoggable(Level.FINE)) {
         _logger.log(Level.FINE, "propName=''{0}''", new Object[]{_propName != null ? _propName : NULL});
      }

      configFile = this.getParamNotEmpty(_propName);
      reader = new FileReader(configFile);
      parser = new JSONParser();
      obj = parser.parse(reader);
      jObject = (JSONObject) obj;

      _logger.exiting(CLASS, METHOD);

      return jObject;
   }

}
