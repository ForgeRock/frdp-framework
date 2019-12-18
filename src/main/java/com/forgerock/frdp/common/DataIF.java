/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.common;

import org.json.simple.JSONObject;

/**
 * Interface for data implementations
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface DataIF extends CoreIF {
   public String getData();

   public void setData(String data);

   public Object getObject();

   public void setObject(Object object);

   public JSONObject getJSON();

   public void setJSON(JSONObject json);
}
