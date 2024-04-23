package ggs.srr.controller.home;

import ggs.srr.controller.home.dto.QuotesDto;
import ggs.srr.domain.Quotes;
import ggs.srr.service.quotes.QuotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class HomeController {

    private final QuotesService quotesService;

    @GetMapping("/quotes")
    public QuotesDto getQuotes(){
        Quotes quotes = quotesService.getQuotes();
        return new QuotesDto(quotes.getName(),  quotes.getContent());
    }

    @PostMapping("/quotes")
    public void addQuotes(@RequestBody QuotesDto quotesDto){

        Quotes quotes = new Quotes(quotesDto.getName(), quotesDto.getContent());
        quotesService.addQuotes(quotes);
    }
}
