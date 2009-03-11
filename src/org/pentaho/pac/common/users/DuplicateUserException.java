/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.common.users;

import java.io.Serializable;

import org.pentaho.pac.common.CheckedException;

public class DuplicateUserException extends CheckedException implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  public DuplicateUserException(String msg) {
    super(msg);
  }
  
  public DuplicateUserException(Throwable cause) {
    super(cause);
  }

  public DuplicateUserException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public DuplicateUserException() {
    super();
  }
}
