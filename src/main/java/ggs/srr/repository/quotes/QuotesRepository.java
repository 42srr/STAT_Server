package ggs.srr.repository.quotes;

import ggs.srr.domain.Quotes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class QuotesRepository {

    @PersistenceContext
    private EntityManager em;

    public Quotes getQuotes(Long id){
        log.info("get Quotes");
        return em.find(Quotes.class, id);
    }

    public Long addQuotes(Quotes quotes) {
        em.persist(quotes);
        return quotes.getId();
    }
}
