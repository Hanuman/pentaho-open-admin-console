package org.pentaho.pac.common.config;

public interface IEmailConfig {
  public String getPassword();
  public void setPassword(String password);
  public boolean getAuthenticate();
  public void setAuthenticate(boolean authenticate);
  public boolean getDebug();
  public void setDebug(boolean debug);
  public String getDefaultFrom();
  public void setDefaultFrom(String defaultFrom);
  public String getSmtpHost();
  public void setSmtpHost(String smtpHost);
  public Integer getSmtpPort();
  public void setSmtpPort(Integer smtpPort);
  public String getSmtpProtocol();
  public void setSmtpProtocol(String smtpProtocol);
  public boolean getUseSsl();
  public void setUseSsl(boolean useSsl);
  public boolean getUseStartTls();
  public void setUseStartTls(boolean useStartTls);
  public String getUserId();
  public void setUserId(String userId);
  public String getPop3Server();
  public void setPop3Server(String pop3Server);

}
