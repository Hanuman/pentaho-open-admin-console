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
package org.pentaho.pac.server;

import org.pentaho.pac.server.common.ConsoleProperties;

/**
 * This class contains all parameters needed to create an SSL server or client socket.
 * 
 */
public class SslParameters {
    private int sslPort;
    private String keyAlias;
    private String keyPassword;
    private String keyStore;
    private String keyStorePassword;
    private String keyStoreType = "JKS"; // type of the key store //$NON-NLS-1$
    private String trustStore;
    private String trustStorePassword;
    private String trustStoreType = "JKS"; //$NON-NLS-1$
    private String protocol = "TLS"; //$NON-NLS-1$
    private String keyManagerFactoryAlgorithm = "SunX509"; // cert algorithm //$NON-NLS-1$
    private String trustManagerFactoryAlgorithm = "SunX509"; // cert algorithm //$NON-NLS-1$
    private boolean wantClientAuth;
    private boolean needClientAuth;

    public SslParameters() {
      
    }

    public SslParameters(ConsoleProperties properties) {
      String sslPort = properties.getProperty(ConsoleProperties.CONSOLE_SSL_PORT_NUMBER);
      setSslPort((sslPort != null && sslPort.length() > 0) ? Integer.parseInt(sslPort) : 0);
      setKeyAlias(properties.getProperty(ConsoleProperties.KEY_ALIAS));
      setKeyPassword(properties.getProperty(ConsoleProperties.KEY_PASSWORD));
      setKeyStore(properties.getProperty(ConsoleProperties.KEYSTORE));
      setKeyStorePassword(properties.getProperty(ConsoleProperties.KEYSTORE_PASSWORD));
      setTrustStore(properties.getProperty(ConsoleProperties.TRUSTSTORE));
      setTrustStorePassword(properties.getProperty(ConsoleProperties.TRUSTSTORE_PASSWORD));
      String needClientAuthValue = properties.getProperty(ConsoleProperties.NEED_CLIENT_AUTH);
      setNeedClientAuth((needClientAuthValue != null && needClientAuthValue.length() > 0) ? Boolean.parseBoolean(needClientAuthValue): false);
      String wantClientAuthValue = properties.getProperty(ConsoleProperties.WANT_CLIENT_AUTH);
      setWantClientAuth((wantClientAuthValue != null && wantClientAuthValue.length() > 0) ? Boolean.parseBoolean(wantClientAuthValue): false);
    }    
    /**
     * @return the sslPort
     */
    public int getSslPort() {
        return sslPort;
    }

    /**
     * @param sslPort
     *            the sslPort to set
     */
    public void setSslPort(int sslPort) {
        this.sslPort = sslPort;
    }

    /**
     * @return the keyAlias
     */
    public String getKeyAlias() {
        return keyAlias;
    }

    /**
     * @param keyAlias
     *            the keyAlias to set
     */
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    /**
     * @return Returns the algorithm.
     */
    public String getKeyManagerFactoryAlgorithm() {
        return keyManagerFactoryAlgorithm;
    }

    /**
     * @param algorithm
     *            The algorithm to set.
     */
    public void setKeyManagerFactoryAlgorithm(String algorithm) {
        this.keyManagerFactoryAlgorithm = algorithm;
    }

    /**
     * @return Returns the algorithm.
     */
    public String getTrustManagerFactoryAlgorithm() {
        return trustManagerFactoryAlgorithm;
    }

    /**
     * @param algorithm
     *            The algorithm to set.
     */
    public void setTrustManagerFactoryAlgorithm(String algorithm) {
        this.trustManagerFactoryAlgorithm = algorithm;
    }

    /**
     * @return Returns the keyPassword.
     */
    public String getKeyPassword() {
        return keyPassword;
    }

    /**
     * @param keyPassword
     *            The keyPassword to set.
     */
    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    /**
     * @return Returns the keyStore.
     */
    public String getKeyStore() {
        return keyStore;
    }

    /**
     * @param keyStore
     *            The keyStore to set.
     */
    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    /**
     * @return Returns the keyStorePassword.
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * @param keyStorePassword
     *            The keyStorePassword to set.
     */
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    /**
     * @return Returns the keyStoreType.
     */
    public String getKeyStoreType() {
        return keyStoreType;
    }

    /**
     * @param keyStoreType
     *            The keyStoreType to set.
     */
    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    /**
     * @return Returns the needClientAuth.
     */
    public boolean isNeedClientAuth() {
        return needClientAuth;
    }

    /**
     * @param needClientAuth
     *            The needClientAuth to set.
     */
    public void setNeedClientAuth(boolean needClientAuth) {
        this.needClientAuth = needClientAuth;
    }

    /**
     * @return Returns the protocol.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     *            The protocol to set.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return Returns the wantClientAuth.
     */
    public boolean isWantClientAuth() {
        return wantClientAuth;
    }

    /**
     * @param wantClientAuth
     *            The wantClientAuth to set.
     */
    public void setWantClientAuth(boolean wantClientAuth) {
        this.wantClientAuth = wantClientAuth;
    }

    /**
     * @return Returns the trustStore.
     */
    public String getTrustStore() {
        return trustStore;
    }

    /**
     * @param trustStore
     *            The trustStore to set.
     */
    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    /**
     * @return Returns the trustStorePassword.
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * @param trustStorePassword
     *            The trustStorePassword to set.
     */
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * @return Returns the trustStoreType.
     */
    public String getTrustStoreType() {
        return trustStoreType;
    }

    /**
     * @param trustStoreType
     *            The trustStoreType to set.
     */
    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SslParameters)) {
            return false;
        }
        SslParameters s = (SslParameters) o;
        return          eq(keyAlias, s.keyAlias)
                        && eq(keyManagerFactoryAlgorithm, s.keyManagerFactoryAlgorithm)
                        && eq(trustManagerFactoryAlgorithm, s.trustManagerFactoryAlgorithm)
                        && eq(keyPassword, s.keyPassword) && eq(keyStore, s.keyStore)
                        && eq(keyStorePassword, s.keyStorePassword) && eq(keyStoreType, s.keyStoreType)
                        && needClientAuth == s.needClientAuth && eq(protocol, s.protocol)
                        && eq(trustStore, s.trustStore) && eq(trustStorePassword, s.trustStorePassword)
                        && eq(trustStoreType, s.trustStoreType) && wantClientAuth == s.wantClientAuth;

    }

    public int hashCode() {
        return Boolean.valueOf(needClientAuth).hashCode() 
                    ^ Boolean.valueOf(wantClientAuth).hashCode()
                    ^ hash(new String[] {keyAlias, keyManagerFactoryAlgorithm, trustManagerFactoryAlgorithm, keyPassword, keyStore,
                        keyStorePassword, keyStoreType, protocol, trustStore, trustStorePassword, trustStoreType});
    }

    private static boolean eq(String s1, String s2) {
        return (s1 == null) ? s2 == null : s1.equals(s2);
    }

    private static int hash(String[] strings) {
        int result = 0;
        for (String s : strings) {
            result ^= hash(s);
        }
        return result;
    }
    
    private static int hash(String s) {
        return s != null ? s.hashCode() : 0;
    }

}
