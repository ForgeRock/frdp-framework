/*
 * Copyright (c) 2015-2019, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */

package com.forgerock.frdp.rest;

import com.forgerock.frdp.common.ConstantsIF;
import com.forgerock.frdp.common.CoreIF;
import javax.ws.rs.core.Response;

/**
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public interface ResourceIF extends CoreIF {
   public static final String OPENAM_COOKIENAME = "openam.cookiename";
   public static final String PATH_APPS = ConstantsIF.APPS;
   public static final String PATH_FILESYSTEM = ConstantsIF.FILESYSTEM;
   public static final String PATH_ITEMS = ConstantsIF.ITEMS;
   public static final String PATH_LAUNCHPAD = "launchpad";
   public static final String PATH_MENUS = ConstantsIF.MENUS;
   public static final String PATH_PORTAL = ConstantsIF.PORTAL;
   public static final String PATH_TASK = ConstantsIF.TASK;
   public static final String PATH_TASKS = ConstantsIF.TASKS;
   public static final String PATH_TASKSVIEW = ConstantsIF.TASKSVIEW;
   public static final String PATH_THEMES = ConstantsIF.THEMES;
   public static final String PATH_WORKFLOW = ConstantsIF.WORKFLOW;
   public static final String QUERYPARAM_ACTION = ConstantsIF.ACTION;
   public static final String SERVLET_PATH = "servlet.path";
   public static final Response.Status[] _arrayStatus = // Response.Status
         { Response.Status.OK, // 200
               Response.Status.CREATED, // 201
               Response.Status.NO_CONTENT, // 204
               Response.Status.BAD_REQUEST, // 400
               Response.Status.UNAUTHORIZED, // 401
               Response.Status.FORBIDDEN, // 403
               Response.Status.NOT_FOUND, // 404
               Response.Status.CONFLICT, // 409
               Response.Status.INTERNAL_SERVER_ERROR // 500
         };
   public static final STATE[] _arrayState = // CoreIF.STATE
         { STATE.SUCCESS, // 200 : OK
               STATE.NEW, // 201 : CREATED
               STATE.READY, // 204 : NO_CONTENT
               STATE.ERROR, // 400 : BAD_REQUEST
               STATE.NOTAUTHORIZED, // 401 : UNAUTHORIZED
               STATE.WARNING, // 403 : FORBIDDEN
               STATE.NOTEXIST, // 404 : NOT_FOUND
               STATE.FAILED, // 409 : CONFLICT
               STATE.NOTREADY // 500 : INTERNAL_SERVER_ERROR
         };

   public static enum ACTION {
      CLAIM, ACCEPT, REJECT
   };
}
