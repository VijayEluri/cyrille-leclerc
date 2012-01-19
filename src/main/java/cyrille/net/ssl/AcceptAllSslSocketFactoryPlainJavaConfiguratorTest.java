/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cyrille.net.ssl;

import static junit.framework.Assert.*;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

import org.junit.Test;

/**
 * @author <a href="mailto:cyrille@cyrilleleclerc.com">Cyrille Le Clerc</a>
 */
public class AcceptAllSslSocketFactoryPlainJavaConfiguratorTest {
    
    @Test
    public void test() throws Exception {
        
        SSLSocketFactory initialDefaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        try {
            String sslUrlWithUntrustedCertificate = "https://localhost";
            {
                // UNTRUSTED CERTIFICATE + DEFAULT SSL_SOCKET_FACTORY : MUST RAISE AN SSL EXCEPTION
                URL url = new URL(sslUrlWithUntrustedCertificate);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                try {
                    connection.getResponseCode();
                    fail("an exception should have been raised");
                } catch (SSLHandshakeException e) {
                    // ok
                }
            }
            
            // register the AcceptAllSslSocketFactory
            new AcceptAllSslSocketFactoryPlainJavaConfigurator();
            
            {
                // UNTRUSTED CERTIFICATE + ACCEPT_ALL SSL_SOCKET_FACTORY : OK
                URL url = new URL(sslUrlWithUntrustedCertificate);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                
                connection.getResponseCode();
            }
        } finally {
            // restore JVM defaults
            HttpsURLConnection.setDefaultSSLSocketFactory(initialDefaultSSLSocketFactory);
        }
        
    }
}
