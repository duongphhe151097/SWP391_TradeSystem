package dataAccess;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbFactory {
    private static final String PERSISTANCE_NAME = "tradesys";
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getFactory(){
        if(factory == null){
            factory = Persistence.createEntityManagerFactory(PERSISTANCE_NAME);
        }

        return factory;
    }

    public static void dispose(){
        if(factory != null){
            factory.close();
        }
    }
}
