/*
 * Copyright (c) 2018-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * JSON utilities, methods for processing JSONObject objects
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */

public class JSON {

   private static enum Type {

      ARRAY, OBJECT, STRING, NUMBER, BOOLEAN
   }

   public static synchronized Boolean getBoolean(final JSONObject json, final String name) {
      Object obj = null;
      Boolean value = false;

      if (json != null && !STR.isEmpty(name)) {
         obj = JSON.getImpl(json, name, Type.BOOLEAN);
         if (obj != null) {
            if (obj instanceof Boolean) {
               value = (Boolean) obj;
            } else if (obj instanceof String) {
               value = Boolean.parseBoolean((String) obj);
            }
         }
      }

      return value;
   }

   public static synchronized Number getNumber(final JSONObject json, final String name) {
      Object obj = null;
      Number value = null;

      if (json != null && !STR.isEmpty(name)) {
         obj = JSON.getImpl(json, name, Type.NUMBER);
         if (obj != null && obj instanceof Number) {
            value = (Number) obj;
         }
      }

      return value;
   }

   public static synchronized String getString(final JSONObject json, final String name) {
      Object obj = null;
      String value = null;

      if (json != null && !STR.isEmpty(name)) {
         obj = JSON.getImpl(json, name, Type.STRING);
         if (obj != null && obj instanceof String) {
            value = (String) obj;
         }
      }

      return value;
   }

   public static synchronized JSONObject getObject(final JSONObject json, final String name) {
      Object obj = null;
      JSONObject value = null;

      if (json != null && !STR.isEmpty(name)) {
         obj = JSON.getImpl(json, name, Type.OBJECT);
         if (obj != null && obj instanceof JSONObject) {
            value = (JSONObject) obj;
         }
      }

      return value;
   }

   public static synchronized JSONArray getArray(final JSONObject json, final String name) {
      Object obj = null;
      JSONArray value = null;

      if (json != null && !STR.isEmpty(name)) {
         obj = JSON.getImpl(json, name, Type.ARRAY);
         if (obj != null && obj instanceof JSONArray) {
            value = (JSONArray) obj;
         }
      }

      return value;
   }

   /**
    * Convert the JSON object to a Map<String, String> of key / value pairs
    * 
    * <pre>
    * JSON:
    * {
    *   "attr1": "value1",
    *   "attr2": true,
    *   "obj1" : {
    *     "attr3" : "value3"
    *     "array1" : [
    *       {
    *         "attr4": "value4"
    *       },
    *       {
    *         "attr5": false
    *       }
    *     ]
    *   },
    *   "obj2": {
    *     "obj3": {
    *       "attr6" : "value6"
    *     }
    *   }
    * }
    *
    * MAP:
    * attr1 value1
    * attr2 true
    * obj1.attr3 value3
    * obj1.array1.0.attr4 attr4
    * obj1.array1.1.attr5 false
    * obj2.obj3.attr6 value 6
    *
    * </pre>
    * 
    * @param json
    * @return
    */
   public static synchronized Map<String, String> convertToParams(final JSONObject json) {
      Map<String, String> map = null;

      if (json != null && !json.isEmpty()) {
         map = new HashMap<>();
         JSON.loadMapFromObject(map, json, "");
      }

      return map;
   }

   /*
    * =============== PRIVATE METHODS ===============
    */

   /**
    * Load provided Map<String, String> from JSON data. The map entry name is a
    * period "." delimated string of the JSON attribute names. This is a recursive
    * method.
    * 
    * @param map    Map<String, String> that will be updated
    * @param json   JSONObject
    * @param prefix String prefix for map entry name
    */
   private static synchronized void loadMapFromObject(final Map<String, String> map, JSONObject json,
         final String prefix) {
      int index = 0;
      Object obj = null;
      Object item = null;
      String value = null;
      Iterator iter = null;

      if (json != null && !json.isEmpty()) {
         for (Object key : json.keySet()) {
            if (key != null && key instanceof String && !STR.isEmpty((String) key)) {
               obj = json.get(key);
               if (obj != null) {
                  if (obj instanceof String) {
                     value = (String) obj;
                     if (!STR.isEmpty(value)) {
                        map.put(prefix + key, value);
                     }
                  } else if (obj instanceof Boolean) {
                     value = ((Boolean) obj).toString();
                     if (!STR.isEmpty(value)) {
                        map.put(prefix + key, value);
                     }
                  } else if (obj instanceof JSONObject) {
                     JSON.loadMapFromObject(map, (JSONObject) obj, key + ".");
                  } else if (obj instanceof JSONArray && !((JSONArray) obj).isEmpty()) {
                     index = 0;
                     iter = ((JSONArray) obj).iterator();
                     while (iter.hasNext()) {
                        item = iter.next();
                        if (item instanceof String) {
                           value = (String) item;
                           if (!STR.isEmpty(value)) {
                              map.put(prefix + key + "." + index, value);
                           }
                        } else if (item instanceof Boolean) {
                           value = ((Boolean) item).toString();
                           if (!STR.isEmpty(value)) {
                              map.put(prefix + key + "." + index, value);
                           }
                        } else if (item instanceof JSONObject) {
                           JSON.loadMapFromObject(map, (JSONObject) item, key + "." + index + ".");
                        }
                        index++;
                     }
                  }
               }
            }
         }
      }

      return;
   }

   /**
    * Implementation for getting an Object from the JSON object
    *
    * Get JSON object using the object name. Process each level using the name
    * "data.foo.bar"
    * 
    * <pre>
    * { 
    *   "data": { 
    *     "foo": { 
    *       "bar": { ... } 
    *     } 
    *   } 
    * }
    * </pre>
    * 
    * Process each level using the name "data.results[0]"
    * 
    * <pre>
    * { 
    *   "data": { 
    *     "results": [ { ... } ] 
    *   } 
    * }
    * </pre>
    * 
    * String.split() ... need to handle "dot/period" as literal character
    *
    * @param json JSONObject
    * @param name String attribute name
    * @param type Type attribute Type
    * @return Object
    */
   private static synchronized Object getImpl(final JSONObject json, final String name, Type type) {
      boolean isLast = false;
      Object obj = null;
      Object ret = null;
      Integer index = 0;
      String currName = null;
      String arrayIndex = null;
      String[] names = null;
      JSONObject level = null;
      JSONArray array = null;

      if (json != null && !STR.isEmpty(name)) {
         names = name.split("[.]");
         level = json;

         for (int i = 0; i < names.length; i++) {
            if (i + 1 == names.length) {
               isLast = true;
            }

            currName = names[i];
            if (!STR.isEmpty(currName)) {

               if (currName.indexOf("[") > 0 && currName.indexOf("]") > 0) // JSONArray
               {
                  arrayIndex = currName.substring(currName.indexOf("[") + 1, currName.indexOf("]"));
                  if (arrayIndex != null && arrayIndex.length() > 0) {
                     index = Integer.parseInt(arrayIndex);
                  } else {
                     index = 0; // default: set index to first ... [0]
                  }
                  currName = currName.substring(0, currName.indexOf("[")); // remove "...[x]"
               } else {
                  index = 0; // JSONObject (or other: String, Number, Boolean)
               }

               if (level != null && level.containsKey(currName)) {
                  obj = level.get(currName);
                  if (obj != null) {
                     if (obj instanceof JSONArray) {
                        if (type != Type.ARRAY) {
                           array = (JSONArray) obj;
                           if (!array.isEmpty() && index < array.size()) {
                              obj = array.get(index);
                           } else {
                              obj = null;
                           }
                        }
                     }

                     if (isLast) {
                        ret = obj;
                     } else {
                        if (obj instanceof JSONObject) {
                           level = (JSONObject) obj;
                        } else {
                           level = null;
                        }
                     }
                  } else {
                     level = null;
                  }
               }
            }
         }
      }

      return ret;
   }
}
