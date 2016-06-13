package org.metaborg.spoofax.shell.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.metaborg.core.context.IContext;
import org.metaborg.core.messages.IMessage;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Test the {@link AbstractTransformResult} class that wraps an {@link ISpoofaxTransformUnit}.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransformResultTest {
    private static final String ACTUAL = "actual";

    // Constructor mocks
    @Mock private IStrategoCommon common;
    @Mock private ISpoofaxTransformUnit<ISpoofaxParseUnit> unit;
    @Mock private IStrategoTerm ast;
    @Mock private IContext context;
    @Mock private IMessage message1, message2;

    private List<IMessage> messages;
    private AbstractTransformResult<?> transformResult;

    /**
     * Set up mocks used in the test case.
     */
    @Before
    public void setup() {
        messages = Arrays.asList(message1, message2);
        when(common.toString(any())).thenReturn(ACTUAL);
        when(unit.ast()).thenReturn(ast);
        when(unit.context()).thenReturn(context);
        when(unit.messages()).thenReturn(messages);

        transformResult = new ParseTransformResult(common, unit);
    }

    /**
     * Test returning the ast of a {@link AbstractTransformResult}.
     */
    @Test
    public void testAst() {
        assertEquals(Optional.of(ast), transformResult.ast());
    }

    /**
     * Test returning the {@link IContext} of a {@link AbstractTransformResult}.
     */
    @Test
    public void testContext() {
        assertEquals(Optional.of(context), transformResult.context());
    }

    /**
     * Test returning the list of {@link IMessage} of a {@link AbstractTransformResult}.
     */
    @Test
    public void testMessages() {
        assertEquals(messages, transformResult.messages());
    }

    /**
     * Test returning the {@link StyledText} of a valid {@link AbstractTransformResult}.
     */
    @Test
    public void testValidStyled() {
        when(unit.valid()).thenReturn(true);

        assertEquals(new StyledText(ACTUAL), transformResult.styled());
    }

    /**
     * Test returning the {@link StyledText} of an invalid {@link AbstractTransformResult}.
     */
    @Test
    public void testInvalidStyled() {
        when(unit.valid()).thenReturn(false);

        assertEquals(new StyledText(messages.toString()), transformResult.styled());
    }

    /**
     * Test validity of a {@link ParseTransformResult} wrapping a valid
     * {@link ISpoofaxTransformUnit}.
     */
    @Test
    public void testValid() {
        when(unit.valid()).thenReturn(true);

        assertTrue(transformResult.valid());
    }

    /**
     * Test validity of a {@link ParseTransformResult} wrapping an invalid
     * {@link ISpoofaxTransformUnit}.
     */
    @Test
    public void testInvalid() {
        when(unit.valid()).thenReturn(false);

        assertFalse(transformResult.valid());
    }
}


