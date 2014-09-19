package com.kodcu;

import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by usta on 18.09.2014.
 */
public class BackupReader implements CacheLoader<Long, Book> {

    private final EntityManager em;

    private static final Logger logger = Logger.getLogger(BackupReader.class.getName());

    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("employee_pu");
        em = emf.createEntityManager();

    }

    @Override
    public Book load(Long isbn) throws CacheLoaderException {

        logger.info(isbn + " Cache içinde bulunamadı.");
        logger.info(isbn + " db'den yükleniyor.");
        Book found = em.find(Book.class, isbn);

        if (Objects.nonNull(found))
            logger.info(isbn + " db'den yüklendi.");
        else
            logger.info(isbn + " db'de de bulunamadı.");
        return found;


    }

    @Override
    public Map<Long, Book> loadAll(Iterable<? extends Long> keys) throws CacheLoaderException {

        Map<Long, Book> allBooks = new HashMap<>();

        for (Long key : keys) {
            allBooks.put(key, load(key));
        }

        return allBooks;
    }
}
