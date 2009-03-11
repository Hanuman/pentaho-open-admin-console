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
package org.pentaho.pac.client.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


// NOTE: i think apache commons collections has one of these, but a better implementation
// TODO get rid of IDoubleKeyMap interface


public class DoubleKeyMap<Key0Type, Key1Type, DataType> implements IDoubleKeyMap<Key0Type, Key1Type, DataType > {

  private Map<Key0Type, Map<Key1Type,DataType>> doubleKeyMap = new HashMap<Key0Type, Map<Key1Type,DataType>>(); // TODO, sbarkdull, make the implementaton generic
  public DoubleKeyMap() {
  }

  public void remove( Key0Type key0, Key1Type key1 ) {
    Map<Key1Type, DataType> innerMap = doubleKeyMap.get( key0 );
    if ( null != innerMap ) {
      innerMap.remove( key1 );
    }
  }
  
  public DataType get( Key0Type key0, Key1Type key1 ) {
    Map<Key1Type, DataType> innerMap = doubleKeyMap.get( key0 );
    if ( null != innerMap ) {
      DataType d = innerMap.get( key1 );
      return d;
    }
    return null;
  }
  
  public void put( Key0Type key0, Key1Type key1, DataType data ) {
    Map<Key1Type, DataType> innerMap = doubleKeyMap.get( key0 );
    if ( null == innerMap ) {
      innerMap = new HashMap<Key1Type, DataType>();
      doubleKeyMap.put( key0, innerMap );
    }
    innerMap.put( key1, data );
  }

  public Set<Key0Type> getKeySetOutter() {
    return doubleKeyMap.keySet();
  }

  public Set<Key1Type> getKeySetInner( Key1Type outterKey ) {
    return doubleKeyMap.get( outterKey ).keySet();
  }
  
  public List<DataType> getList() {
    List<DataType> l = new ArrayList<DataType>();
    for ( Map.Entry<Key0Type, Map<Key1Type,DataType>> doubleKeyEntry : doubleKeyMap.entrySet() ) {
      Map<Key1Type, DataType> innerMap = doubleKeyEntry.getValue();
      for ( Map.Entry<Key1Type, DataType> innerEntry : innerMap.entrySet() ) {
        l.add( innerEntry.getValue() );
      }
    }
    return l;
  }
}
