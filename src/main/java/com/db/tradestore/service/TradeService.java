package com.db.tradestore.service;

import com.db.tradestore.model.Trade;
import com.db.tradestore.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired private TradeRepository tradeRepository;

	/*
	 * check if maturity date is less than today's date or not, if yes don't allow
	 * otherwise allow and check if the received version is less than existing version 
	 */
    public boolean isValid(Trade trade){
        if(validateMaturityDate(trade)) {
             Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
	         if (exsitingTrade.isPresent()) {
	             return validateVersion(trade, exsitingTrade.get());
	         }else{
	             return true;
	         }
         }
         return false;
    }

	/*
	 * During transmission if the lower version is being received by the store it
	 * will reject the trade and throw an exception. If the version is same it will
	 * override the existing record.
	 */
    
    private boolean validateVersion(Trade trade,Trade oldTrade) {
    	return trade.getVersion() < oldTrade.getVersion() ? false : true;
    }

	/*
	 * Store should not allow the trade which has less maturity date than today date
	 */
    private boolean validateMaturityDate(Trade trade){
        return trade.getMaturityDate().isBefore(LocalDate.now()) ? false : true;
    }

	/* If the versions are same it will override the existing record. */
    public void persist(Trade trade){
        trade.setCreatedDate(LocalDate.now());
        tradeRepository.save(trade);
    }

    public List<Trade> findAll(){
       return tradeRepository.findAll();
    }

    public void updateExpiryFlagOfTrade(){
        tradeRepository.findAll().stream().forEach(t -> {
	        if (!validateMaturityDate(t)) {
	            t.setExpiryFlag("Y");
	            log.info("Trade which needs to updated {}", t);
	            tradeRepository.save(t);
	        }
	    });
    }
}
