/*
 * Copyright (c) 2018-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.dao;

import com.forgerock.frdp.common.ConstantsIF;
import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.utils.JSON;
import com.forgerock.frdp.utils.STR;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 * Abstract class that implements the DataAccessIF interface. This must be
 * sub-classes
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public abstract class DataAccess extends Core implements DataAccessIF {

   private final String CLASS = this.getClass().getName();
   protected final Logger _logger = Logger.getLogger(this.getClass().getName());
   protected final Level DEBUG_LEVEL = Level.FINE;

   /**
    * Default constructor
    */
   public DataAccess() {
      super();
      return;
   }

   /**
    * Create object from existing object
    *
    * @param data existing DataAccessIF object
    */
   public DataAccess(final DataAccessIF data) {
      super(data);
      return;
   }

   /**
    * Create object from parameters
    *
    * @param params instance of existing Map
    */
   public DataAccess(final Map<String, String> params) {
      super(params);
      return;
   }

   /*
    * ================= PROTECTED METHODS =================
    */
   /**
    * Validate the OperationIF object: not null, contains JSON, check required
    * attributes based on the operation
    *
    * @param oper the OperationIF object
    * @throws Exception could not validate the operation
    */
   protected void validate(final OperationIF oper) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      JSONObject jsonInput = null;
      JSONObject jsonData = null;
      JSONObject jsonForm = null;
      JSONObject jsonQuery = null;

      _logger.entering(CLASS, METHOD);

      if (oper == null) {
         throw new Exception("Operation object is null");
      }

      jsonInput = oper.getJSON();

      if (jsonInput == null || jsonInput.isEmpty()) {
         throw new Exception("JSON Input is null or empty");
      }

      // read, update, delete may have a "uid" attribute
      if (oper.getType() == OperationIF.TYPE.READ || oper.getType() == OperationIF.TYPE.REPLACE
         || oper.getType() == OperationIF.TYPE.DELETE) {
         if (STR.isEmpty(JSON.getString(jsonInput, ConstantsIF.UID))) {
            throw new Exception("JSON attribute 'uid' is empty");
         }
      }

      // create and update require a "data" or "form" object
      if (oper.getType() == OperationIF.TYPE.CREATE || oper.getType() == OperationIF.TYPE.REPLACE) {
         if (jsonInput.containsKey(ConstantsIF.DATA)) {
            jsonData = JSON.getObject(jsonInput, ConstantsIF.DATA);

            if (jsonData == null || jsonData.isEmpty()) {
               throw new Exception("JSON 'data' is null or empty");
            }
         } else if (jsonInput.containsKey(ConstantsIF.FORM)) {
            jsonForm = JSON.getObject(jsonInput, ConstantsIF.FORM);

            if (jsonForm == null || jsonForm.isEmpty()) {
               throw new Exception("JSON 'form' is null or empty");
            }
         } else {
            throw new Exception("JSON Input must contain either 'data' or 'form' objects.");
         }
      }

      // search requires a "query" JSON object
      if (oper.getType() == OperationIF.TYPE.SEARCH) {
         jsonQuery = JSON.getObject(jsonInput, ConstantsIF.QUERY);

         if (jsonQuery == null || jsonQuery.isEmpty()) {
            throw new Exception("JSON Query is empty or missing");
         }
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

}
