package ggs.srr.controller.home;

import ggs.srr.controller.home.dto.QuotesDto;
import ggs.srr.domain.Quotes;
import ggs.srr.service.quotes.QuotesRandomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class HomeController {

    private final QuotesRandomServiceImpl quotesService;

    @GetMapping("/quotes")
    public QuotesDto getQuotesByIdById(){
        Quotes quotes = quotesService.getQuotesById();
        return new QuotesDto(quotes.getName(),  quotes.getContent());
    }

    @PostMapping("/quotes")
    public void addQuotes(@RequestBody QuotesDto quotesDto){

        Quotes quotes = new Quotes(quotesDto.getName(), quotesDto.getContent());
        quotesService.addQuotes(quotes);
    }

    @GetMapping("/quotes/count")
    public String getQuotesByIdCount() {
        Long quotesCount = quotesService.getQuotesCount();
        return String.valueOf(quotesCount);
    }

    @DeleteMapping("quotes/{id}")
    public ResponseEntity<Quotes> delete(@PathVariable Long id) {
        Quotes deleted = quotesService.delete(id);
        return (id != null) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
