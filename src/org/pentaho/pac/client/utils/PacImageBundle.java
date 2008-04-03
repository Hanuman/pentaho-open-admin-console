package org.pentaho.pac.client.utils;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * @deprecated
 * @author Steven Barkdull
 *
 */
public interface PacImageBundle extends ImageBundle {
  
  /**
  * @gwt.resource org/pentaho/pac/public/style/images/minus_disabled.png
  */
 public AbstractImagePrototype minusDisabledIcon(); 
  /**
  * @gwt.resource org/pentaho/pac/public/style/images/minus.png
  */
 public AbstractImagePrototype minusIcon(); 
  /**
  * @gwt.resource org/pentaho/pac/public/style/images/plus.png
  */
 public AbstractImagePrototype plusIcon();
 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/users_over.png
  */
 public AbstractImagePrototype usersOverIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/users_selected.png
  */
 public AbstractImagePrototype usersSelectedIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/users_off.png
  */
 public AbstractImagePrototype usersOffIcon(); 
 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/roles_over.png
  */
 public AbstractImagePrototype rolesOverIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/roles_selected.png
  */
 public AbstractImagePrototype rolesSelectedIcon(); 
 /**
  * @gwt.resource org/pentaho/pac/public/style/images/roles_off.png
  */
 public AbstractImagePrototype rolesOffIcon(); 
 
 
}
