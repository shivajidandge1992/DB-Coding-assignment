package com.db.tradestore;

import com.db.tradestore.controller.TradeStoreController;
import com.db.tradestore.exception.InvalidTradeException;
import com.db.tradestore.model.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeStoreApplicationTests {

	@Autowired private TradeStoreController tradeController;
	
	@Test
	void loadContexts() {
	
	}

	@Test
	void testTradeValidateAndStoreIt() {
		ResponseEntity<String> responseEntity = tradeController.validateTrade(createTrade("T1",1,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
	}

	@Test
	void testTradeValidateAndStoreWhenMaturityDatePast() {
		try {
			LocalDate localDate = getLocalDate(2015, 05, 21);
			tradeController.validateTrade(createTrade("T2", 1, localDate));
		}catch (InvalidTradeException ie) {
			Assertions.assertEquals("Invalid Trade: Trade ID T2 is not found", ie.getMessage());
		}
	}

	@Test
	void testTradeValidateAndStoreWhenSameVersionTrade(){
		ResponseEntity<String> responseEntity = tradeController.validateTrade(createTrade("T1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList.get(0).getVersion());
		Assertions.assertEquals("T1B1",tradeList.get(0).getBookId());

		//step-2 create trade with same version
		Trade trade2 = createTrade("T1",2,LocalDate.now());
		trade2.setBookId("T1B1V2");
		ResponseEntity<String> responseEntity2 = tradeController.validateTrade(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity2);
		List<Trade> tradeList2 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList2.size());
		Assertions.assertEquals("T1",tradeList2.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList2.get(0).getVersion());
		Assertions.assertEquals("T1B1V2",tradeList2.get(0).getBookId());

		//step-2 create trade with new version
		Trade trade3 = createTrade("T1",2,LocalDate.now());
		trade3.setBookId("T1B1V3");
		ResponseEntity<String> responseEntity3 = tradeController.validateTrade(trade3);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity3);
		List<Trade> tradeList3 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList3.size());
		Assertions.assertEquals("T1",tradeList3.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList3.get(0).getVersion());
		Assertions.assertEquals("T1B1V3",tradeList3.get(0).getBookId());
	}
	
	@Test
	void testTradeValidateAndStoreWhenOldVersion() {
		// step-1 create trade
		ResponseEntity<String> responseEntity = tradeController.validateTrade(createTrade("T1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList.get(0).getVersion());
		Assertions.assertEquals("T1B1",tradeList.get(0).getBookId());
		//step-2 create trade with old version
		try {
			tradeController.validateTrade(createTrade("T1", 1, LocalDate.now()));
		}catch (InvalidTradeException e){
			System.out.println(e.getId());
			System.out.println(e.getMessage());
		}
		List<Trade> tradeList1 = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList1.size());
		Assertions.assertEquals("T1",tradeList1.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList1.get(0).getVersion());
		Assertions.assertEquals("T1B1",tradeList.get(0).getBookId());
	}
	
	private Trade createTrade(String tradeId,int version,LocalDate  maturityDate){
		return new Trade(tradeId, version, tradeId+"Cpty", tradeId+"B1", maturityDate, "Y");
	}

	public static LocalDate getLocalDate(int year,int month, int day){
		LocalDate localDate = LocalDate.of(year,month,day);
		return localDate;
	}
}
