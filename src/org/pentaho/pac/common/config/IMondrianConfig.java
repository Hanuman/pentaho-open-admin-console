package org.pentaho.pac.common.config;

public interface IMondrianConfig {
  public Integer getResultLimit(); 
  public void setResultLimit( Integer limit );
  public Integer getTraceLevel();
  public void setTraceLevel( Integer level );  
  public String getLogFileLocation(); 
  public void setLogFileLocation( String location ); 
  public Integer getQueryLimit(); 
  public void setQueryLimit( Integer limit ); 
  public Integer getQueryTimeout();
  public void setQueryTimeout( Integer timeout );
  public boolean getIgnoreInvalidMembers(); 
  public void setIgnoreInvalidMembers( boolean ignore );
  public boolean getCacheHitCounters();
}
