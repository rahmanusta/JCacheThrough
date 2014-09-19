package com.kodcu;

import javax.cache.Cache;
import javax.cache.integration.CacheWriter;
import javax.cache.integration.CacheWriterException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by usta on 18.09.2014.
 */
public class BackupWriter implements CacheWriter<Long, Book> {

    private final EntityManager em;
    private static final Logger logger = Logger.getLogger(BackupWriter.class.getName());

    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("employee_pu");
        em = emf.createEntityManager();

    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Book> entry) throws CacheWriterException {

        Long isbn = entry.getKey();
        Book book = entry.getValue();

        logger.info(isbn + " db'ye yazılıyor");
        em.getTransaction().begin();
        em.merge(book);
        em.getTransaction().commit();
        logger.info(isbn + " db'ye yazıldı");
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Book>> entries) throws CacheWriterException {
        entries.forEach(this::write);
    }

    @Override
    public void delete(Object isbn) throws CacheWriterException {
        Book found = em.find(Book.class, isbn);

        if (Objects.nonNull(found)) {
            logger.info(isbn + " db'den siliniyor.");

            em.getTransaction().begin();
                em.remove(found);
            em.getTransaction().commit();

            logger.info(isbn + " db'den silindi.");
        }
    }

    @Override
    public void deleteAll(Collection<?> keys) throws CacheWriterException {
        keys.forEach(this::delete);
    }
}
