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
package org.krysalis.barcode4j.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the default <code>Configuration</code> implementation.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Id: DefaultConfiguration.java 506231 2007-02-12 02:36:54Z crossley $
 */
public class DefaultConfiguration
        extends AbstractConfiguration
        implements MutableConfiguration, Serializable
{
    /**
     * An empty (length zero) array of configuration objects.
     */
    protected static final Configuration[] EMPTY_ARRAY = new Configuration[ 0 ];

    private final String m_name;
    private final String m_location;
    private final String m_namespace;
    private final String m_prefix;
    private HashMap m_attributes;
    private ArrayList m_children;
    private String m_value;
    private boolean m_readOnly;

    /**
     * Copy constructor, to create a clone of another configuration.
     * To modify children, use <code>getChild()</code>,
     * <code>removeChild()</code> and <code>addChild()</code>.
     *
     * @param config the <code>Configuration</code> to copy
     * @param deepCopy true will cause clones of the children to be added,
     *                 false will add the original instances and is thus
     *                 faster.
     *
     * @throws ConfigurationException if an error occurs when copying
     */
    public DefaultConfiguration( Configuration config, boolean deepCopy )
            throws ConfigurationException
    {
        this( config.getName(), config.getLocation(), config.getNamespace(),
                ( (config instanceof AbstractConfiguration) ? ((AbstractConfiguration)config).getPrefix() : "") );

        addAll( config, deepCopy );
    }

    /**
     * Shallow copy constructor, suitable for craeting a writable clone of
     * a read-only configuration. To modify children, use <code>getChild()</code>,
     * <code>removeChild()</code> and <code>addChild()</code>.
     *
     * @param config the <code>Configuration</code> to copy
     * @throws ConfigurationException if an error occurs when copying
     */
    public DefaultConfiguration( Configuration config ) throws ConfigurationException
    {
        this( config, false );
    }

    /**
     * Create a new <code>DefaultConfiguration</code> instance.
     * @param name a <code>String</code> value
     */
    public DefaultConfiguration( final String name )
    {
        this( name, null, "", "" );
    }

    /**
     * Create a new <code>DefaultConfiguration</code> instance.
     * @param name a <code>String</code> value
     * @param location a <code>String</code> value
     */
    public DefaultConfiguration( final String name, final String location )
    {
        this( name, location, "", "" );
    }

    /**
     * Create a new <code>DefaultConfiguration</code> instance.
     * @param name config node name
     * @param location Builder-specific locator string
     * @param ns Namespace string (typically a URI). Should not be null; use ""
     * if no namespace.
     * @param prefix A short string prefixed to element names, associating
     * elements with a longer namespace string. Should not be null; use "" if no
     * namespace.
     * @since 4.1
     */
    public DefaultConfiguration( final String name,
                                 final String location,
                                 final String ns,
                                 final String prefix )
    {
        m_name = name;
        m_location = location;
        m_namespace = ns;
        m_prefix = prefix;  // only used as a serialization hint. Cannot be null
    }

    /**
     * Returns the name of this configuration element.
     * @return a <code>String</code> value
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Returns the namespace of this configuration element
     * @return a <code>String</code> value
     * @throws ConfigurationException if an error occurs
     * @since 4.1
     */
    public String getNamespace() throws ConfigurationException
    {
        if( null != m_namespace )
        {
            return m_namespace;
        }
        else
        {
            throw new ConfigurationException
                    ( "No namespace (not even default \"\") is associated with the "
                            + "configuration element \"" + getName()
                            + "\" at " + getLocation() );
        }
    }

    /**
     * Returns the prefix of the namespace
     * @return a <code>String</code> value
     * @throws ConfigurationException if prefix is not present (<code>null</code>).
     * @since 4.1
     */
    protected String getPrefix() throws ConfigurationException
    {
        if( null != m_prefix )
        {
            return m_prefix;
        }
        else
        {
            throw new ConfigurationException
                    ( "No prefix (not even default \"\") is associated with the "
                            + "configuration element \"" + getName()
                            + "\" at " + getLocation() );
        }

    }

    /**
     * Returns a description of location of element.
     * @return a <code>String</code> value
     */
    public String getLocation()
    {
        return m_location;
    }

    /**
     * Returns the value of the configuration element as a <code>String</code>.
     *
     * @param defaultValue the default value to return if value malformed or empty
     * @return a <code>String</code> value
     */
    public String getValue( final String defaultValue )
    {
        if( null != m_value )
        {
            return m_value;
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the configuration element as a <code>String</code>.
     *
     * @return a <code>String</code> value
     * @throws ConfigurationException If the value is not present.
     */
    public String getValue() throws ConfigurationException
    {
        if( null != m_value )
        {
            return m_value;
        }
        else
        {
            throw new ConfigurationException( "No value is associated with the "
                    + "configuration element \"" + getName()
                    + "\" at " + getLocation() );
        }
    }

    /**
     * Return an array of all attribute names.
     * @return a <code>String[]</code> value
     */
    public String[] getAttributeNames()
    {
        if( null == m_attributes )
        {
            return new String[ 0 ];
        }
        else
        {
            return (String[])m_attributes.keySet().toArray( new String[ 0 ] );
        }
    }

    /**
     * Return an array of <code>Configuration</code>
     * elements containing all node children.
     *
     * @return The child nodes with name
     */
    public Configuration[] getChildren()
    {
        if( null == m_children )
        {
            return new Configuration[ 0 ];
        }
        else
        {
            return (Configuration[])m_children.toArray( new Configuration[ 0 ] );
        }
    }

    /**
     * Returns the value of the attribute specified by its name as a
     * <code>String</code>.
     *
     * @param name a <code>String</code> value
     * @return a <code>String</code> value
     * @throws ConfigurationException If the attribute is not present.
     */
    public String getAttribute( final String name )
            throws ConfigurationException
    {
        final String value =
                ( null != m_attributes ) ? (String)m_attributes.get( name ) : null;

        if( null != value )
        {
            return value;
        }
        else
        {
            throw new ConfigurationException(
                    "No attribute named \"" + name + "\" is "
                            + "associated with the configuration element \""
                            + getName() + "\" at " + getLocation() );
        }
    }

    /**
     * Return the first <code>Configuration</code> object child of this
     * associated with the given name.
     * @param name a <code>String</code> value
     * @param createNew a <code>boolean</code> value
     * @return a <code>Configuration</code> value
     */
    public Configuration getChild( final String name, final boolean createNew )
    {
        if( null != m_children )
        {
            final int size = m_children.size();
            for( int i = 0; i < size; i++ )
            {
                final Configuration configuration = (Configuration)m_children.get( i );
                if( name.equals( configuration.getName() ) )
                {
                    return configuration;
                }
            }
        }

        if( createNew )
        {
            return new DefaultConfiguration( name, "<generated>" + getLocation(), m_namespace, m_prefix );
        }
        else
        {
            return null;
        }
    }

    /**
     * Return an array of <code>Configuration</code> objects
     * children of this associated with the given name.
     * <br>
     * The returned array may be empty but is never <code>null</code>.
     *
     * @param name The name of the required children <code>Configuration</code>.
     * @return a <code>Configuration[]</code> value
     */
    public Configuration[] getChildren( final String name )
    {
        if( null == m_children )
        {
            return new Configuration[ 0 ];
        }
        else
        {
            final ArrayList children = new ArrayList();
            final int size = m_children.size();

            for( int i = 0; i < size; i++ )
            {
                final Configuration configuration = (Configuration)m_children.get( i );
                if( name.equals( configuration.getName() ) )
                {
                    children.add( configuration );
                }
            }

            return (Configuration[])children.toArray( new Configuration[ 0 ] );
        }
    }

    /**
     * Append data to the value of this configuration element.
     *
     * @param value a <code>String</code> value
     * @deprecated Use setValue() instead
     */
    public void appendValueData( final String value )
    {
        checkWriteable();

        if( null == m_value )
        {
            m_value = value;
        }
        else
        {
            m_value += value;
        }
    }

    /**
     * Set the value of this <code>Configuration</code> object to the specified string.
     *
     * @param value a <code>String</code> value
     */
    public void setValue( final String value )
    {
        checkWriteable();

        m_value = value;
    }

    /**
     * Set the value of this <code>Configuration</code> object to the specified int.
     *
     * @param value a <code>int</code> value
     */
    public void setValue( final int value )
    {
        setValue( String.valueOf( value ) );
    }

    /**
     * Set the value of this <code>Configuration</code> object to the specified long.
     *
     * @param value a <code>long</code> value
     */
    public void setValue( final long value )
    {
        setValue( String.valueOf( value ) );
    }

    /**
     * Set the value of this <code>Configuration</code> object to the specified boolean.
     *
     * @param value a <code>boolean</code> value
     */
    public void setValue( final boolean value )
    {
        setValue( String.valueOf( value ) );
    }

    /**
     * Set the value of this <code>Configuration</code> object to the specified float.
     *
     * @param value a <code>float</code> value
     */
    public void setValue( final float value )
    {
        setValue( String.valueOf( value ) );
    }

    /**
     * Set the value of this <code>Configuration</code> object to the specified double.
     *
     * @param value a <code>double</code> value
     */
    public void setValue( final double value )
    {
        setValue( String.valueOf( value ) );
    }

    /**
     * Set the value of the specified attribute to the specified string.
     *
     * @param name name of the attribute to set
     * @param value a <code>String</code> value
     */
    public void setAttribute( final String name, final String value )
    {
        checkWriteable();

        if( null != value )
        {
            if( null == m_attributes )
            {
                m_attributes = new HashMap();
            }
            m_attributes.put( name, value );
        }
        else
        {
            if( null != m_attributes )
            {
                m_attributes.remove( name );
            }
        }
    }

    /**
     * Set the value of the specified attribute to the specified int.
     *
     * @param name name of the attribute to set
     * @param value an <code>int</code> value
     */
    public void setAttribute( final String name, final int value )
    {
        setAttribute( name, String.valueOf( value ) );
    }

    /**
     * Set the value of the specified attribute to the specified long.
     *
     * @param name name of the attribute to set
     * @param value an <code>long</code> value
     */
    public void setAttribute( final String name, final long value )
    {
        setAttribute( name, String.valueOf( value ) );
    }

    /**
     * Set the value of the specified attribute to the specified boolean.
     *
     * @param name name of the attribute to set
     * @param value an <code>boolean</code> value
     */
    public void setAttribute( final String name, final boolean value )
    {
        setAttribute( name, String.valueOf( value ) );
    }

    /**
     * Set the value of the specified attribute to the specified float.
     *
     * @param name name of the attribute to set
     * @param value an <code>float</code> value
     */
    public void setAttribute( final String name, final float value )
    {
        setAttribute( name, String.valueOf( value ) );
    }

    /**
     * Set the value of the specified attribute to the specified double.
     *
     * @param name name of the attribute to set
     * @param value an <code>double</code> value
     */
    public void setAttribute( final String name, final double value )
    {
        setAttribute( name, String.valueOf( value ) );
    }

    /**
     * Add an attribute to this configuration element, returning its old
     * value or <b>null</b>.
     *
     * @param name a <code>String</code> value
     * @param value a <code>String</code> value
     * @return a <code>String</code> value
     * @deprecated Use setAttribute() instead
     */
    public String addAttribute( final String name, String value )
    {
        checkWriteable();

        if( null == m_attributes )
        {
            m_attributes = new HashMap();
        }

        return (String)m_attributes.put( name, value );
    }

    /**
     * Add a child <code>Configuration</code> to this configuration element.
     * @param configuration a <code>Configuration</code> value
     */
    public void addChild( final Configuration configuration )
    {
        checkWriteable();

        if( null == m_children )
        {
            m_children = new ArrayList();
        }

        m_children.add( configuration );
    }

    /**
     * Add all the attributes, children and value
     * from specified configuration element to current
     * configuration element.
     *
     * @param other the {@link Configuration} element
     * @param deepCopy true will cause clones of the children to be added,
     *                 false will add the original instances and is thus
     *                 faster.
     *
     * throws ConfigurationException If there are any problems cloning the
     *                               children.
     */
    public void addAll( final Configuration other, final boolean deepCopy )
            throws ConfigurationException
    {
        checkWriteable();

        setValue( other.getValue( null ) );
        addAllAttributes( other );
        addAllChildren( other, deepCopy );
    }

    /**
     * Add all the attributes, children and value
     * from specified configuration element to current
     * configuration element.
     *
     * @param other the {@link Configuration} element
     */
    public void addAll( final Configuration other )
    {
        checkWriteable();

        setValue( other.getValue( null ) );
        addAllAttributes( other );
        addAllChildren( other );
    }

    /**
     * Add all attributes from specified configuration
     * element to current configuration element.
     *
     * @param other the {@link Configuration} element
     */
    public void addAllAttributes( final Configuration other )
    {
        checkWriteable();

        final String[] attributes = other.getAttributeNames();
        for( int i = 0; i < attributes.length; i++ )
        {
            final String name = attributes[ i ];
            final String value = other.getAttribute( name, null );
            setAttribute( name, value );
        }
    }

    /**
     * Add all child <code>Configuration</code> objects from specified
     * configuration element to current configuration element.
     *
     * @param deepCopy true will cause clones of the children to be added,
     *                 false will add the original instances and is thus
     *                 faster.
     *
     * @param other the other {@link Configuration} value
     *
     * throws ConfigurationException If there are any problems cloning the
     *                               children.
     */
    public void addAllChildren( final Configuration other, final boolean deepCopy )
            throws ConfigurationException
    {
        checkWriteable();

        final Configuration[] children = other.getChildren();
        for( int i = 0; i < children.length; i++ )
        {
            if ( deepCopy )
            {
                addChild( new DefaultConfiguration( children[ i ], true ) );
            }
            else
            {
                addChild( children[ i ] );
            }
        }
    }

    /**
     * Add all child <code>Configuration</code> objects from specified
     * configuration element to current configuration element.
     *
     * @param other the other {@link Configuration} value
     */
    public void addAllChildren( final Configuration other )
    {
        checkWriteable();

        final Configuration[] children = other.getChildren();
        for( int i = 0; i < children.length; i++ )
        {
            addChild( children[ i ] );
        }
    }

    /**
     * Remove a child <code>Configuration</code> to this configuration element.
     * @param configuration a <code>Configuration</code> value
     */
    public void removeChild( final Configuration configuration )
    {
        checkWriteable();

        if( null == m_children )
        {
            return;
        }
        m_children.remove( configuration );
    }

    /**
     * Return count of children.
     * @return an <code>int</code> value
     */
    public int getChildCount()
    {
        if( null == m_children )
        {
            return 0;
        }

        return m_children.size();
    }

    /**
     * Make this configuration read-only.
     *
     */
    public void makeReadOnly()
    {
        m_readOnly = true;
    }

    /**
     * heck if this configuration is writeable.
     *
     * @throws IllegalStateException if this configuration s read-only
     */
    protected final void checkWriteable()
            throws IllegalStateException
    {
        if( m_readOnly )
        {
            throw new IllegalStateException
                    ( "Configuration is read only and can not be modified" );
        }
    }

    /**
     * Returns true iff this DefaultConfiguration has been made read-only.
     */
    protected final boolean isReadOnly()
    {
        return m_readOnly;
    }

    /**
     * Convenience function to convert a child to a mutable configuration.
     * If the child is-a MutableConfiguration, and it isn't a read-only DefaultConfiguration
     * (which isn't really mutable), the child is cast to MutableConfiguration and returned.
     * If not, the child is replaced in the m_children array with a new writable DefaultConfiguration
     * that is a shallow copy of the child, and the new child is returned.
     */
    private MutableConfiguration toMutable( Configuration child ) throws ConfigurationException
    {
        if (child instanceof MutableConfiguration &&
                !( child instanceof DefaultConfiguration && ((DefaultConfiguration) child).isReadOnly() ))
        {
            // Child is already mutable - return it.
            return (MutableConfiguration) child;
        }

        // Child isn't mutable. (This is a mutating operation, so let's check
        // if we're writable.)
        checkWriteable();

        DefaultConfiguration config = new DefaultConfiguration( child );

        // Replace the old child.
        for( int i = 0; i < m_children.size(); i++)
        {
            if( m_children.get(i) == child )
            {
                m_children.set( i, config );
                break;
            }
        }

        return config;
    }

    public MutableConfiguration getMutableChild( final String name ) throws ConfigurationException
    {
        return getMutableChild( name, true );
    }

    public MutableConfiguration getMutableChild( final String name, boolean autoCreate ) throws ConfigurationException
    {
        Configuration child = getChild( name, false );
        if( child == null )
        {
            // No child. Create?

            if( autoCreate )
            {
                DefaultConfiguration config = new DefaultConfiguration( name, "-" );
                addChild( config );
                return config;
            }
            else
            {
                return null;
            }
        }

        // Child exists
        return toMutable( child );
    }

    public MutableConfiguration[] getMutableChildren() throws ConfigurationException
    {
        if( null == m_children )
        {
            return new MutableConfiguration[ 0 ];
        }
        else
        {
            final ArrayList children = new ArrayList();
            final int size = m_children.size();

            for( int i = 0; i < size; i++ )
            {
                final Configuration configuration = (Configuration)m_children.get( i );
                children.add( toMutable( configuration ) );
            }

            return (MutableConfiguration[])children.toArray( new MutableConfiguration[ 0 ] );
        }
    }

    public MutableConfiguration[] getMutableChildren( final String name ) throws ConfigurationException
    {
        if( null == m_children )
        {
            return new MutableConfiguration[ 0 ];
        }
        else
        {
            final ArrayList children = new ArrayList();
            final int size = m_children.size();

            for( int i = 0; i < size; i++ )
            {
                final Configuration configuration = (Configuration)m_children.get( i );
                if( name.equals( configuration.getName() ) )
                {
                    children.add( toMutable( configuration ) );
                }
            }

            return (MutableConfiguration[])children.toArray( new MutableConfiguration[ 0 ] );
        }
    }

    /**
     * Compare if this configuration is equal to another.
     *
     * @param other  The other configuration
     * @return <code>true</code> if they are the same.
     */
    public boolean equals( Object other )
    {
        if( other == null )
            return false;
        if( !( other instanceof DefaultConfiguration ) )
        {
            // Niclas: It is not possible to validate equality against any
            //         Configuration implementation, as it would be
            //         impossible to get the hashCode() method return the
            //         same value for two instances evaluating equal().
            //         I.e. If we were to do equality at API level, we would be
            //         breaking the equals()/hashCode() semantic contract.
            return false;
        }

        DefaultConfiguration c = (DefaultConfiguration) other;

        if( m_readOnly ^ c.m_readOnly )
            return false;

        if( check( m_name, c.m_name ) )
            return false;
        if( check( m_location, c.m_location ) )
            return false;
        if( check( m_namespace, c.m_namespace ) )
            return false;
        if( check( m_prefix, c.m_prefix ) )
            return false;
        if( check( m_value, c.m_value ) )
            return false;
        if( check( m_attributes, c.m_attributes ) )
            return false;
        if( check( m_children, c.m_children ) )
            return false;
        return true;
    }

    private boolean check( Object one, Object two )
    {
        if( one == null )
            return two != null;
        return ! one.equals( two );
    }

    /**
     * Obtaine the hashcode for this configuration.
     *
     * @return the hashcode.
     */
    public int hashCode()
    {
        int hash = m_prefix.hashCode();
        if( m_name != null )
            hash ^= m_name.hashCode();
        hash >>>= 7;
        if( m_location != null )
            hash ^= m_location.hashCode();
        hash >>>= 7;
        if( m_namespace != null )
            hash ^= m_namespace.hashCode();
        hash >>>= 7;
        if( m_attributes != null )
            hash ^= m_attributes.hashCode();
        hash >>>= 7;
        if( m_children != null )
            hash ^= m_children.hashCode();
        hash >>>= 7;
        if( m_value != null )
            hash ^= m_value.hashCode();
        hash >>>= ( m_readOnly ) ? 7 : 13;
        return hash;
    }
}
