package com.db.tradestore.controller;

import com.db.tradestore.exception.InvalidTradeException;
import com.db.tradestore.model.Trade;
import com.db.tradestore.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TradeStoreController {
	
    private @Autowired TradeService tradeService;
 
    @PostMapping("/trade")
    public ResponseEntity<String> validateTrade(@RequestBody Trade trade){
       //check if received version is less than existing version then throw exception otherwise override existing trade
       if(tradeService.isValid(trade)) {
           tradeService.persist(trade);
       }else{
           throw new InvalidTradeException("Trade ID "+trade.getTradeId() +" is not found");
       }
       return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/trade")
    public List<Trade> findAllTrades(){
        return tradeService.findAll();
    }
}
