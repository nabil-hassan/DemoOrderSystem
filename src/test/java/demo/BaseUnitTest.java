package demo;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import demo.categories.UnitTest;

@Category(UnitTest.class)
@RunWith(MockitoJUnitRunner.class)
@Ignore
public abstract class BaseUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    public <T extends Exception> void expectException(Class<T> exceptionClazz) {
        exceptionRule.expect(exceptionClazz);
    }

    public <T extends Exception> void expectException(Class<T> exceptionClazz, String message) {
        exceptionRule.expect(exceptionClazz);
        exceptionRule.expectMessage(message);
    }

}
