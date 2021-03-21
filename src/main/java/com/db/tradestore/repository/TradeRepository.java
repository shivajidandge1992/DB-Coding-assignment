package com.db.tradestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.db.tradestore.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade,String> {
	
}
