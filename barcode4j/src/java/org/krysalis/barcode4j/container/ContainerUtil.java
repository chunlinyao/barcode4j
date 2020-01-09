package org.krysalis.barcode4j.container;

import org.krysalis.barcode4j.activity.Initializable;
import org.krysalis.barcode4j.configuration.Configurable;
import org.krysalis.barcode4j.configuration.Configuration;
import org.krysalis.barcode4j.configuration.ConfigurationException;

public class ContainerUtil {
    /**
     * Configure specified object if it implements the
     * {@link Configurable} interface.
     *
     * @param object the object to Start
     * @param configuration the configuration object to use during
     *        configuration. May be null in which case the specified object
     *        must not implement Configurable
     * @throws ConfigurationException if there is a problem Configuring object,
     *         or the object is Configurable but Configuration is null
     * @throws IllegalArgumentException if the object is Configurable but
     *         Configuration is null
     */
    public static void configure( final Object object,
                                  final Configuration configuration )
            throws ConfigurationException
    {
        if( object instanceof Configurable )
        {
            if( null == configuration )
            {
                final String message = "configuration is null";
                throw new IllegalArgumentException( message );
            }
            ( (Configurable)object ).configure( configuration );
        }
    }

    /**
     * Initialize specified object if it implements the
     * {@link Initializable} interface.
     *
     * @param object the object to Initialize
     * @throws Exception if there is a problem Initializing object
     */
    public static void initialize( final Object object )
            throws Exception
    {
        if( object instanceof Initializable )
        {
            ( (Initializable)object ).initialize();
        }
    }
}
