package ggs.srr.service.quotes;

import ggs.srr.domain.Quotes;
import ggs.srr.repository.quotes.QuotesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuotesService {

    private final QuotesRepository repository;

    public int randomQuotesId() {
        int day;
        int size;

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        String formatedNow = now.format(formatter);
        day = Integer.parseInt(formatedNow);
        size = getQuotesSize();
        log.info("day " + day);
        log.info("size" + size);
        return day % size;
    }

    public Quotes getQuotes() {
        int quotesId;
        Quotes quotes;

        List<Quotes> allQuotes = repository.getAllQuotes();
        quotesId = randomQuotesId();
        quotes = allQuotes.get(quotesId);
        return quotes;
    }
    public Quotes getQuotesById(Long id){
        return repository.getQuotesById(id);
    }

    @Transactional(readOnly = false)
    public long addQuotes(Quotes quotes) {
        return repository.addQuotes(quotes);
    }

    public int getQuotesSize(){
        return repository.getQuotesSize();
    }

    @Transactional(readOnly = false)
    public Quotes deleteQuotes(long quotesId){
        Quotes target = repository.getQuotesById(quotesId);
        if (target == null){
            return null;
        }
        repository.deleteQuotes(quotesId);
        return target;
    }
}
