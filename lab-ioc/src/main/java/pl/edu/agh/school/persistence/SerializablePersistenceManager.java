package pl.edu.agh.school.persistence;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.Teacher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class SerializablePersistenceManager implements PersistenceManager {

    private final Logger log;

    private String teachersStorageFileName;
    private String classStorageFileName;

    @Inject
    public SerializablePersistenceManager(Logger log) {
        teachersStorageFileName = "teachers.dat";
        classStorageFileName = "classes.dat";
        this.log = log;
    }


    @Override
    public void saveTeachers(List<Teacher> teachers) {
        if (teachers == null) {
            throw new IllegalArgumentException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(teachersStorageFileName))) {
            oos.writeObject(teachers);

            log.log("Teachers data saved");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            log.log("There was an error while saving the teachers data", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Teacher> loadTeachers() {
        ArrayList<Teacher> res = null;
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(teachersStorageFileName))) {

            res = (ArrayList<Teacher>) ios.readObject();
        } catch (FileNotFoundException e) {
            res = new ArrayList<>();
        } catch (IOException e) {
            log.log("There was an error while loading the teachers data", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        log.log("Teachers data loaded");
        return res;
    }

    @Override
    public void saveClasses(List<SchoolClass> classes) {
        if (classes == null) {
            throw new IllegalArgumentException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(classStorageFileName))) {

            oos.writeObject(classes);

            log.log("Classes data saved");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            log.log("There was an error while saving the classes data", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SchoolClass> loadClasses() {
        ArrayList<SchoolClass> res = null;
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(classStorageFileName))) {
            res = (ArrayList<SchoolClass>) ios.readObject();
        } catch (FileNotFoundException e) {
            res = new ArrayList<>();
        } catch (IOException e) {
            log.log("There was an error while loading the classes data", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        log.log("Classes data loaded");
        return res;
    }


    @Inject
    public void setClassStorageFileName(@Named("classesFile")
                                        String classStorageFileName) {
        this.classStorageFileName = classStorageFileName;
    }

    @Inject
    public void setTeachersStorageFileName(@Named("teachersFile")
                                           String teachersStorageFileName) {
        this.teachersStorageFileName = teachersStorageFileName;
    }
}
