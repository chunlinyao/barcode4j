/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.krysalis.barcode4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * This class provides basic facilities for manipulating exceptions.
 *
 * Some exception handling stuff thieved from Turbine...
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 */
public final class ExceptionUtil
{
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );
    private static final String GET_CAUSE_NAME = "getCause";
    private static final Class[] GET_CAUSE_PARAMTYPES = new Class[ 0 ];

    /**
     * Private constructor to prevent instantiation.
     */
    private ExceptionUtil()
    {
    }

    /**
     * Generate string for specified exception and the cause of
     * this exception (if any).
     * @param throwable a <code>Throwable</code>
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable )
    {
        return printStackTrace( throwable, 0, true );
    }

    /**
     * Generate string for specified exception and if printCascading
     * is true will print all cascading exceptions.
     * @param throwable a <code>Throwable</code>
     * @param printCascading if <code>true</code> will print all cascading exceptions
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable,
                                          final boolean printCascading )
    {
        return printStackTrace( throwable, 0, printCascading );
    }

    /**
     * Serialize the specified <code>Throwable</code> to a string.
     * Restrict the number of frames printed out to the specified depth.
     * If the depth specified is <code>0</code> then all the frames are
     * converted into a string.
     * @param throwable a <code>Throwable</code>
     * @param depth number of stack trace frames to show
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable, final int depth )
    {
        int dp = depth;
        final String[] lines = captureStackTrace( throwable );

        if( 0 == dp || dp > lines.length )
        {
            dp = lines.length;
        }

        final StringBuffer sb = new StringBuffer();

        for( int i = 0; i < dp; i++ )
        {
            sb.append( lines[ i ] );
            sb.append( LINE_SEPARATOR );
        }

        return sb.toString();
    }

    /**
     * Generate exception string for specified exception to specified depth
     * and all Cascading exceptions if printCascading is true.
     * @param throwable a <code>Throwable</code>
     * @param depth number of stack trace frames to show
     * @param printCascading if <code>true</code> will print all cascading exceptions
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable,
                                          final int depth,
                                          final boolean printCascading )
    {
        return printStackTrace( throwable, depth, printCascading, true );
    }

    /**
     * Generate exception string for specified exception to specified depth
     * and all Cascading exceptions if printCascading is true. If useReflection
     * is true then the method will also attempt to use reflection to find a
     * method with signature <code>Throwable getCause()</code>. This makes
     * it compatible with JDK1.4 mechanisms for nesting exceptions.
     * @param throwable a <code>Throwable</code>
     * @param depth number of stack trace frames to show
     * @param printCascading if <code>true</code> will print all cascading exceptions
     * @param useReflection if <code>true</code> will use reflection to handle JDK1.4
     *                      nested exceptions
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable,
                                          final int depth,
                                          final boolean printCascading,
                                          final boolean useReflection )
    {
        final String result = printStackTrace( throwable, depth );

        if( !printCascading )
        {
            return result;
        }
        else
        {
            final StringBuffer sb = new StringBuffer();
            sb.append( result );

            Throwable cause = getCause( throwable, useReflection );

            while( null != cause )
            {
                sb.append( "rethrown from" );
                sb.append( LINE_SEPARATOR );
                sb.append( printStackTrace( cause, depth ) );

                cause = getCause( cause, useReflection );
            }

            return sb.toString();
        }
    }

    /**
     * Utility method to get cause of exception.
     * @param throwable a <code>Throwable</code>
     * @param useReflection if <code>true</code> will use reflection to handle JDK1.4
     *                      nested exceptions
     * @return cause of specified exception
     */
    public static Throwable getCause( final Throwable throwable,
                                      final boolean useReflection )
    {
        if( useReflection )
        {
            try
            {
                final Class clazz = throwable.getClass();
                final Method method =
                        clazz.getMethod( GET_CAUSE_NAME, GET_CAUSE_PARAMTYPES );
                return (Throwable)method.invoke( throwable, null );
            }
            catch( final Throwable t )
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Captures the stack trace associated with this exception.
     *
     * @param throwable a <code>Throwable</code>
     * @return an array of Strings describing stack frames.
     */
    public static String[] captureStackTrace( final Throwable throwable )
    {
        final StringWriter sw = new StringWriter();
        throwable.printStackTrace( new PrintWriter( sw, true ) );
        return splitStringInternal( sw.toString(), LINE_SEPARATOR );
    }

    /**
     * Splits the string on every token into an array of stack frames.
     *
     * @param string the string to split
     * @param onToken the token to split on
     * @return the resultant array
     * @deprecated This is an internal utility method that should not be used
     */
    public static String[] splitString( final String string, final String onToken )
    {
        return splitStringInternal( string, onToken );
    }

    /**
     * Splits the string on every token into an array of stack frames.
     *
     * @param string the string to split
     * @param onToken the token to split on
     * @return the resultant array
     */
    private static String[] splitStringInternal( final String string, final String onToken )
    {
        final StringTokenizer tokenizer = new StringTokenizer( string, onToken );
        final String[] result = new String[ tokenizer.countTokens() ];

        for( int i = 0; i < result.length; i++ )
        {
            result[ i ] = tokenizer.nextToken();
        }

        return result;
    }
}
