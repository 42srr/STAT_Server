package ggs.srr.repository.quotes;

import ggs.srr.domain.Quotes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class QuotesRepository  {

    @PersistenceContext
    private EntityManager em;


    public Quotes getQuotesById(Long id){
        log.info("get Quotes");
        em.getProperties();
        return em.find(Quotes.class, id);
    }

    public List<Quotes> getAllQuotes(){
        log.info("get all quotes");
        List<Quotes> quotes =new ArrayList<>(em.createQuery("select q from Quotes q", Quotes.class)
                .getResultList());
        return quotes;
    }

    public Long addQuotes(Quotes quotes) {
        em.persist(quotes);
        return quotes.getId();
    }

    public int getQuotesSize() {
        List<Quotes> list =new ArrayList<>(em.createQuery("select q from Quotes q", Quotes.class)
                .getResultList());
        return list.size();
    }

    public void deleteQuotes(Long quotesId){
        Quotes quotes = getQuotesById(quotesId);
        em.remove(quotes);
    }
}
