/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.utils;

import java.util.List;

/**
 * String utilities, methods for processing String objects
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */

public class STR {
     /**
      * Test if the String is "empty" ... String is null or has zero length
      * 
      * @param str String
      * @return boolean is empty
      */
     public static synchronized boolean isEmpty(final String str) {
          boolean empty = true;

          if (str != null && str.trim().length() > 0) {
               empty = false;
          }

          return empty;
     }

     /**
      * Convert list of Strings, List<String>, to a comma delimited String
      * 
      * @param list List<String>
      * @return String delimited string
      */
     public static synchronized String toString(final List<String> list) {
          boolean empty = true;
          StringBuilder strBldr = null;

          if (list != null) {
               strBldr = new StringBuilder();
               strBldr.append("[");
               for (String s : list) {
                    if (!empty) {
                         strBldr.append(",");
                    } else {
                         empty = false;
                    }
                    if (!STR.isEmpty(s)) {
                         strBldr.append("'").append(s).append("'");
                    }
               }
               strBldr.append("]");
          }

          return (strBldr == null ? null : strBldr.toString());
     }
}
