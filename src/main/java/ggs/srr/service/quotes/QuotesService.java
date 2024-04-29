package ggs.srr.service.quotes;

import ggs.srr.domain.Quotes;
import org.springframework.transaction.annotation.Transactional;

public interface QuotesService {
    Quotes getQuotesById();

    @Transactional(readOnly = false)
    long addQuotes(Quotes quotes);
}
