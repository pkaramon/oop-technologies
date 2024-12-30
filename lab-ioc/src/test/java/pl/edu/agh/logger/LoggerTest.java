package pl.edu.agh.logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;
import pl.edu.agh.school.guice.SchoolModule;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void testSingletonBehaviour() {
        Injector injector = Guice.createInjector(new SchoolModule());
        Logger logger1 = injector.getInstance(Logger.class);
        Logger logger2 = injector.getInstance(Logger.class);

        assertSame(logger1, logger2);
    }

}