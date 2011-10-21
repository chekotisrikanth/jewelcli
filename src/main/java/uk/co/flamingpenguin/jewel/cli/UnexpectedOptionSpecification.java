package uk.co.flamingpenguin.jewel.cli;

import java.util.Collections;
import java.util.List;

import com.lexicalscope.fluentreflection.ReflectedMethod;

class UnexpectedOptionSpecification implements OptionSpecification
{
    private final String m_name;

    UnexpectedOptionSpecification(final String name)
    {
        m_name = name;
    }

    @Override public String getCanonicalIdentifier() {
        return m_name;
    }

    @Override public Class<?> getType()
    {
        return Void.class;
    }

    @Override public boolean hasValue()
    {
        return false;
    }

    @Override public boolean isMultiValued()
    {
        return false;
    }

    @Override public boolean isOptional()
    {
        return false;
    }

    @Override public String getDescription()
    {
        return String.format("Option not recognised");
    }

    @Override public List<String> getDefaultValue()
    {
        return Collections.emptyList();
    }

    @Override public boolean hasDefaultValue()
    {
        return false;
    }

    @Override public boolean isHelpOption()
    {
        return false;
    }

    @Override public ReflectedMethod getMethod() {
        return null;
    }

    @Override public ReflectedMethod getOptionalityMethod() {
        return null;
    }

    @Override public String toString()
    {
        final StringBuilder result = new StringBuilder();
        result.append(m_name).append(" : ").append(getDescription());
        return result.toString();
    }
}
