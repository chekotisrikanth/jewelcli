//
//Author       : t.wood
//Copyright    : (c) Teamphone.com Ltd. 2008 - All Rights Reserved
//
package uk.co.flamingpenguin.jewel.cli;

import java.util.List;

import com.lexicalscope.fluentreflection.ReflectedMethod;

/**
 * Specifies an Option
 * 
 * @author t.wood
 */
interface OptionSpecification
{
    /**
     * Each argument to this option must conform to the type returned by this
     * method
     * 
     * @return the type that each argument must conform to
     */
    Class<?> getType();

    /**
     * Canonical identifier for the option
     * 
     * @return the canonical identifier for the option
     */
    String getCanonicalIdentifier();

    /**
     * Does the option take any arguments?
     * 
     * @return True iff the the option takes at least one argument
     */
    boolean hasValue();

    /**
     * Are multiple arguments allowed?
     * 
     * @return True iff the the option takes multiple arguments
     */
    boolean isMultiValued();

    /**
     * Is the argument optional
     * 
     * @return is the argument optional
     */
    boolean isOptional();

    /**
     * Get the default values which will be used if the option is not specified
     * by the user.
     * 
     * @return The default values which will be used if the option is not
     *         present
     */
    List<String> getDefaultValue();

    /**
     * Is there a default value to use if this option is not present? Options
     * with a default value are assumed to be optional.
     * 
     * @return True iff this option has a default value
     */
    boolean hasDefaultValue();

    /**
     * Get a description of the option. The description can be specified in the
     * <code>Option</code> annotation
     * 
     * @see uk.co.flamingpenguin.jewel.cli.Option
     * 
     * @return a description of the option
     */
    String getDescription();

    /**
     * Is this option a request for help
     * 
     * @return True iff this option is a request for help
     */
    boolean isHelpOption();

    ReflectedMethod getMethod();

    ReflectedMethod getOptionalityMethod();
}
