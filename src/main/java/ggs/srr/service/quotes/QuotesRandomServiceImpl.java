package ggs.srr.service.quotes;

import ggs.srr.domain.Quotes;
import ggs.srr.repository.quotes.QuotesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuotesRandomServiceImpl implements QuotesService{


    private final QuotesRepository repository;

    public Quotes getQuotesById(){
        String formatedNow = getTodayToString();
        log.info(formatedNow);
        Long quotesSize = getQuotesCount();
        Long quotesId = quotesSize % Integer.parseInt(formatedNow);
        return repository.getQuotes(quotesId);
    }

    private static String getTodayToString() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        String formatedNow = now.format(formatter);
        return formatedNow;
    }

    @Transactional(readOnly = false)
    public long addQuotes(Quotes quotes) {
        return repository.addQuotes(quotes);
    }

    public long getQuotesCount() {
        return repository.getQuotesCount();
    }

    @Transactional(readOnly = false)
    public Quotes delete(Long id) {
        Quotes quotes = repository.getQuotes(id);

        if (quotes == null) {
            return null;
        }
        repository.delete(quotes);
        return quotes;
    }
}
