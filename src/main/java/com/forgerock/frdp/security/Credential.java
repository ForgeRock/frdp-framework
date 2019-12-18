/*
 * Copyright (c) 2016-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.security;

import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;

/**
 * Implements CredentialIF interface
 * 
 * @author Scott Fehrman, ForgeRock, Inc.
 */

public class Credential extends Core implements CredentialIF {
   private Type _type = null;

   /**
    * Create credential using existing credential type
    * 
    * @param cred CredentialIF
    */
   public Credential(CredentialIF cred) {
      super(cred);

      _type = cred.getType();

      return;
   }

   /**
    * Create credential for specified type
    * 
    * @param type Type
    */
   public Credential(final Type type) {
      super();

      _type = type;

      return;
   }

   /**
    * Get the credential type
    * 
    * @return Type
    */
   @Override
   public final Type getType() {
      return _type;
   }

   /**
    * This class does not implement the copy() method from the Core interface
    */
   @Override
   public CoreIF copy() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
