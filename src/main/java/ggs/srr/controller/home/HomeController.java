package ggs.srr.controller.home;

import ggs.srr.controller.home.dto.QuotesDto;
import ggs.srr.domain.Quotes;
import ggs.srr.service.quotes.QuotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class HomeController {

    private final QuotesService quotesService;

    @GetMapping("/quotes")
    public QuotesDto getQuotes(){
        Quotes quotes = quotesService.getQuotes();
        return new QuotesDto(quotes.getId(), quotes.getName(),  quotes.getContent());
    }

    @GetMapping("/quotes/{id}")
    public QuotesDto getQuotesById(@PathVariable Long id) {
        Quotes quotes = quotesService.getQuotesById(id);
        return new QuotesDto(quotes.getId(), quotes.getName(), quotes.getContent());
    }

    @PostMapping("/quotes")
    public Long addQuotes(@RequestBody QuotesDto quotesDto){
        Quotes quotes = new Quotes(null, quotesDto.getName(), quotesDto.getContent());
        quotesService.addQuotes(quotes);
        return quotes.getId();
    }

    @GetMapping("/quotes/count")
    public String getQuoteSize(){
        int quotesNumber = quotesService.getQuotesSize();
        return String.valueOf(quotesNumber);
    }

    @DeleteMapping("/quotes/{quotesId}")
    public ResponseEntity<Quotes> deleteQuotes(@PathVariable("quotesId") Long quotesId){
        Quotes deleted = quotesService.deleteQuotes(quotesId);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
