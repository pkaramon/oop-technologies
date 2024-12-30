package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import pl.edu.agh.logger.FileMessageSerializer;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

public class SchoolModule extends AbstractModule {
    @Provides
    public PersistenceManager providePersistenceManager(SerializablePersistenceManager mgr) {
        return mgr;
    }

    @Provides
    @Named("teachersFile")
    public String provideTeachersStorageFileName() {
        return "guice-teachers.dat";
    }

    @Provides
    @Named("classesFile")
    public String provideClassStorageFileName() {
        return "guice-classes.dat";
    }

    @Provides
    @Singleton
    public Logger provideLogger() {
        Logger logger = Logger.getInstance();
        logger.registerSerializer(new FileMessageSerializer("persistance.log"));
        return logger;
    }
}
