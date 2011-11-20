package uk.co.flamingpenguin.jewel.cli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException.ValidationError;
import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException.ValidationError.ErrorType;

public class TestCliImpl {
    public interface SingleOption {
        @Option String getName();
    }

    public interface ListOption {
        @Option List<String> getName();
    }

    public interface CharacterValue {
        @Option Character getName();
    }

    public interface SingleBooleanOption {
        @Option boolean getName0();

        @Option boolean isName1();
    }

    public interface SingleOptionalOption {
        @Option String getName();

        boolean isName();
    }

    public interface IntegerOption {
        @Option Integer getName();
    }

    public interface IntOption {
        @Option int getName();
    }

    public interface SingleOptionWithArgument {
        @Option String getName(String argument);
    }

    public interface SingleOptionMissingAnnotation {
        String getName();
    }

    public interface UnparsedOption {
        @Unparsed String getName();
    }

    public interface UnparsedListOption {
        @Unparsed List<String> getNames();
    }

    public interface OptionalUnparsedOption {
        @Unparsed String getName();

        boolean isName();
    }

    public interface OptionalUnparsedListOption {
        @Unparsed List<String> getNames();

        boolean isNames();
    }

    public enum TestEnum {
        Value0, Value1, Value2;
    }

    public interface EnumDefaultListOption {
        @Option(defaultValue = { "Value0", "Value1" }) List<TestEnum> getName();
    }

    @Test public void testSingleOption() throws ArgumentValidationException {
        final SingleOption option =
                new CliInterfaceImpl<SingleOption>(SingleOption.class).parseArguments(new String[] { "--name", "value" });
        assertEquals(option.getName(), "value");
    }

    @Test public void testSingleBooleanOption() throws ArgumentValidationException {
        final SingleBooleanOption option =
                new CliInterfaceImpl<SingleBooleanOption>(SingleBooleanOption.class).parseArguments(new String[] { "--name1" });
        assertEquals(option.getName0(), false);
        assertEquals(option.isName1(), true);
    }

    @Test public void testIntegerOption() throws ArgumentValidationException {
        final IntegerOption option =
                new CliInterfaceImpl<IntegerOption>(IntegerOption.class).parseArguments(new String[] { "--name", "10" });
        assertEquals(Integer.valueOf(10), option.getName());
    }

    @Test public void testInvalidIntegerOption() {
        try {
            new CliInterfaceImpl<IntegerOption>(IntegerOption.class).parseArguments(new String[] { "--name", "abc" });
            fail();
        } catch (final ArgumentValidationException e) {
            assertEquals(1, e.getValidationErrors().size());
            assertEquals(ErrorType.InvalidValueForType, e.getValidationErrors().get(0).getErrorType());
            assertEquals("Unsupported number format: For input string: \"abc\"", e
                    .getValidationErrors()
                    .get(0)
                    .getMessage());
        }
    }

    @Test public void testInvalidIntOption() {
        try {
            new CliInterfaceImpl<IntOption>(IntOption.class).parseArguments(new String[] { "--name", "abc" });
            fail();
        } catch (final ArgumentValidationException e) {
            assertEquals(1, e.getValidationErrors().size());
            assertEquals(ErrorType.InvalidValueForType, e.getValidationErrors().get(0).getErrorType());
            assertEquals("Unsupported number format: For input string: \"abc\"", e
                    .getValidationErrors()
                    .get(0)
                    .getMessage());
        }
    }

    @Test public void testInvalidOption() {
        try {
            new CliInterfaceImpl<SingleOption>(SingleOption.class).parseArguments(new String[] { "--invalid", "value" });
            fail();
        } catch (final ArgumentValidationException e) {
            final ArrayList<ValidationError> validationErrors = e.getValidationErrors();
            assertEquals(2, validationErrors.size());
            final ValidationError error0 = validationErrors.get(0);
            assertEquals(ErrorType.UnexpectedOption, error0.getErrorType());
            final ValidationError error1 = validationErrors.get(1);
            assertEquals(ErrorType.MissingOption, error1.getErrorType());
        }
    }

    @Test public void testSingleOptionalOption() throws ArgumentValidationException {
        SingleOptionalOption option =
                new CliInterfaceImpl<SingleOptionalOption>(SingleOptionalOption.class).parseArguments(new String[] {
                        "--name",
                        "value" });
        assertEquals(option.getName(), "value");
        assertTrue(option.isName());

        option = new CliInterfaceImpl<SingleOptionalOption>(SingleOptionalOption.class).parseArguments(new String[] {});
        assertFalse(option.isName());
    }

    @Test public void testSingleOptionalOptionRequestMissing() throws ArgumentValidationException {
        final SingleOptionalOption option =
                new CliInterfaceImpl<SingleOptionalOption>(SingleOptionalOption.class).parseArguments(new String[] {});

        assertThat(option.getName(), equalTo(null));
    }

    @Test public void testCharacterValue() throws ArgumentValidationException {
        final CharacterValue option =
                new CliInterfaceImpl<CharacterValue>(CharacterValue.class).parseArguments(new String[] { "--name", "a" });
        assertEquals((Character) 'a', option.getName());
    }

    @Test public void testInvalidCharacterValue() {
        try {
            new CliInterfaceImpl<CharacterValue>(CharacterValue.class).parseArguments(new String[] { "--name", "aa" });
            fail();
        } catch (final ArgumentValidationException e) {
            final ArrayList<ValidationError> validationErrors = e.getValidationErrors();
            assertEquals(1, validationErrors.size());
            assertEquals(ErrorType.InvalidValueForType, validationErrors.get(0).getErrorType());
        }
    }

    @Test public void testMethodWithArguments() throws ArgumentValidationException {
        try {
            new CliInterfaceImpl<SingleOptionWithArgument>(SingleOptionWithArgument.class).parseArguments(new String[] {
                    "--name",
                    "value" });
            fail();
        } catch (final ArgumentValidationException e) {
            assertEquals(
                    ErrorType.UnexpectedOption,
                    e.getValidationErrors().get(0).getErrorType());
        }
    }

    @Test public void testMethodWithMissingAnnotation() throws ArgumentValidationException {
        final SingleOptionMissingAnnotation result =
                new CliInterfaceImpl<SingleOptionMissingAnnotation>(SingleOptionMissingAnnotation.class)
                        .parseArguments(new String[] {});

        assertThat(result.getName(), equalTo(null));
    }

    @Test public void testUnparsedOption() throws ArgumentValidationException {
        final UnparsedOption result =
                new CliInterfaceImpl<UnparsedOption>(UnparsedOption.class).parseArguments(new String[] { "value" });
        assertEquals("value", result.getName());
    }

    @Test public void testUnparsedOptionMissingValue() {
        try {
            new CliInterfaceImpl<UnparsedOption>(UnparsedOption.class).parseArguments(new String[] {});
            fail();
        } catch (final ArgumentValidationException e) {
            final ArrayList<ValidationError> validationErrors = e.getValidationErrors();
            assertEquals(1, validationErrors.size());
            assertEquals(ErrorType.MissingValue, validationErrors.get(0).getErrorType());
        }
    }

    @Test public void testUnparsedListOption() throws ArgumentValidationException {
        final UnparsedListOption result =
                new CliInterfaceImpl<UnparsedListOption>(UnparsedListOption.class).parseArguments(new String[] {
                        "value0",
                        "value1" });
        assertEquals(2, result.getNames().size());
        assertEquals("value0", result.getNames().get(0));
        assertEquals("value1", result.getNames().get(1));
    }

    @Test public void testListOptionMissingValue() throws ArgumentValidationException {
        new CliInterfaceImpl<ListOption>(ListOption.class).parseArguments(new String[] { "--name" });
    }

    @Test public void testUnparsedListOptionMissingValue() throws ArgumentValidationException {
        new CliInterfaceImpl<UnparsedListOption>(UnparsedListOption.class).parseArguments(new String[] {});
    }

    @Test public void testOptionalUnparsedOption() throws ArgumentValidationException {
        final UnparsedOption result =
                new CliInterfaceImpl<UnparsedOption>(UnparsedOption.class).parseArguments(new String[] { "value" });
        assertEquals(result.getName(), "value");
    }

    @Test public void testOptionalUnparsedOptionMissingValue() throws ArgumentValidationException {
        final OptionalUnparsedOption result =
                new CliInterfaceImpl<OptionalUnparsedOption>(OptionalUnparsedOption.class).parseArguments(new String[] {});
        assertFalse(result.isName());
    }

    @Test public void testOptionalUnparsedListOption() throws ArgumentValidationException {
        final OptionalUnparsedListOption result =
                new CliInterfaceImpl<OptionalUnparsedListOption>(OptionalUnparsedListOption.class).parseArguments(new String[] {
                        "value0",
                        "value1" });
        assertEquals(2, result.getNames().size());
        assertEquals("value0", result.getNames().get(0));
        assertEquals("value1", result.getNames().get(1));
        assertTrue(result.isNames());
    }

    @Test public void testOptionalUnparsedListOptionMissingValue() throws ArgumentValidationException {
        final OptionalUnparsedListOption result =
                new CliInterfaceImpl<OptionalUnparsedListOption>(OptionalUnparsedListOption.class)
                        .parseArguments(new String[] {});
        assertFalse(result.isNames());
    }

    @Test public void testEnumDefaultList() throws ArgumentValidationException {
        final EnumDefaultListOption result =
                new CliInterfaceImpl<EnumDefaultListOption>(EnumDefaultListOption.class).parseArguments(new String[] {});

        final List<TestEnum> enumValues = result.getName();
        assertEquals(2, enumValues.size());
        assertEquals(TestEnum.Value0, enumValues.get(0));
        assertEquals(TestEnum.Value1, enumValues.get(1));
    }
}