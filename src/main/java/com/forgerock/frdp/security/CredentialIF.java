/*
 * Copyright (c) 2016-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.security;

import com.forgerock.frdp.common.CoreIF;

/**
 * Credential interface
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface CredentialIF extends CoreIF {

   public enum Type {

      ANON, USERNAME, OPENAMTOKEN, PROXY
   }

   public Type getType();
}
