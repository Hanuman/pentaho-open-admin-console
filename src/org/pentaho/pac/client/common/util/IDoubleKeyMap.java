package org.pentaho.pac.client.common.util;

// TODO sbarkdull, probably can go away
public interface IDoubleKeyMap<Key0Type, Key1Type, DataType> {

  public DataType get( Key0Type key0, Key1Type key1 );
  public void put( Key0Type key0, Key1Type key1, DataType data);
}
