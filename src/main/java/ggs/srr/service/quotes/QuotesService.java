package ggs.srr.service.quotes;

import ggs.srr.domain.Quotes;
import ggs.srr.repository.quotes.QuotesRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuotesService {


    private final QuotesRepository repository;

    /**
     * todo
     * 추후 랜덤값으로 수정
     */
    private static Long quotesId = 1l;
    public Quotes getQuotes(){
        return repository.getQuotes(quotesId);
    }

    @Transactional(readOnly = false)
    public long addQuotes(Quotes quotes) {
        return repository.addQuotes(quotes);
    }
}
