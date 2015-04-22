/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium.support.url;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * Provides URL manipulations and functionality.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class URLUtils {

    private static final int MD5_BUFFER_SIZE = 8192;
    private static final int HEX_RADIX = 16;

    private URLUtils() {
    }

    /**
     * Use URL context and one or more relocations to build end URL.
     *
     * @param context first URL used like a context root for all relocation changes
     * @param relocations array of relocation URLs
     * @return end url after all changes made on context with relocations
     * @throws AssertionError when context or some of relocations are malformed URLs
     */
    public static URL buildUrl(String context, String... relocations) {
        try {
            return buildUrl(new URL(context), relocations);
        } catch (MalformedURLException e) {
            throw new AssertionError("URL('" + context + "') isn't valid URL");
        }
    }

    /**
     * Use URL context and one or more relocations to build end URL.
     *
     * @param context first URL used like a context root for all relocation changes
     * @param relocations array of relocation URLs
     * @return end url after all changes made on context with relocations
     * @throws AssertionError when context or some of relocations are malformed URLs
     */
    public static URL buildUrl(URL context, String... relocations) {
        URL url = context;

        for (String move : relocations) {
            try {
                url = new URL(url, move);
            } catch (MalformedURLException e) {
                throw new AssertionError("URL('" + url + "', '" + move + "') isn't valid URL");
            }
        }

        return url;
    }

    /**
     * Gets a MD5 digest of some resource obtains as input stream from connection to URL given by URL string.
     *
     * @param url of the resource
     * @return MD5 message digest of resource
     * @throws IOException when connection to URL fails
     */
    public static String resourceMd5Digest(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();

        InputStream in = connection.getInputStream();
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("MD5 hashing is unsupported", ex);
        }
        byte[] buffer = new byte[MD5_BUFFER_SIZE];
        int read = 0;

        while ((read = in.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }

        byte[] md5sum = digest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        return bigInt.toString(HEX_RADIX);
    }

    /**
     * Encodes credentials using Base64 encoding, which can be used to build a header for HTTP Basic authorization
     *
     * @param username User name to be encoded
     * @param password Password to be encoded
     * @return A string {@code username:password} encoded with Base64
     */
    public static String encodeBase64Credentials(String username, String password) {
        String credentials = username + ":" + password;
        try {
            String defaultCharsetName = Charset.defaultCharset().displayName();
            byte[] credentialsAsBytes = credentials.getBytes(defaultCharsetName);
            byte[] encodedCredentials = Base64.encodeBase64(credentialsAsBytes);
            return new String(encodedCredentials, defaultCharsetName);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
