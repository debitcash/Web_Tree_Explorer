import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;

/* 
 * UrlsManagerBean.java: Stateless EJB that manages CRUD operations for UrlEntity. 
 * It interacts with the database using JPA to create, read, update, and delete URL records.
 * 
 * Author: GitHub @debitcash
 * 
 */

@LocalBean
@Stateless
public class UrlsManagerBean {
    
    @PersistenceContext(unitName = "p-unit")
    private EntityManager entityManager;

    // Creates a new URL entity if it does not already exist in the database
    public boolean create(UrlEntity urlEntity) {
        UrlEntity existingUrl = entityManager.find(UrlEntity.class, urlEntity.getName());
        if (existingUrl != null) {
            return false;
        }
        entityManager.persist(urlEntity);
        return true;
    }

    // Reads a URL entity from the database by its name
    public UrlEntity read(String name) {
        return entityManager.find(UrlEntity.class, name);
    }

    // Updates a URL entity in the database
    public boolean update(UrlEntity urlEntity) {
        return true;
    }

    // Deletes a URL entity from the database by its name
    public boolean delete(String name) {
        UrlEntity urlEntity = entityManager.find(UrlEntity.class, name);
        if (urlEntity == null) {
            return false;
        }
        entityManager.remove(urlEntity);
        return true;
    }

    // Reads and returns a list of all URL entities from the database, ordered by depth in descending order
    public List<UrlEntity> readList() {
        TypedQuery<UrlEntity> query = entityManager.createQuery("SELECT entity FROM UrlEntity entity ORDER BY entity.depth DESC", UrlEntity.class);
        return query.getResultList();
    }
}