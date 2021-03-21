package com.db.tradestore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @Column
    private String tradeId;
    @Column
    private int version;
    @Column
    private String counterParty;
    @Column
    private String bookId;
    @Column
    private LocalDate maturityDate;
    @Column
    private LocalDate createdDate;
    @Column
    private String expiryFlag;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCounterParty() {
        return counterParty;
    }

    public void setCounterParty(String counterParty) {
        this.counterParty = counterParty;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

	public String getExpiryFlag() {
		return expiryFlag;
	}

	public void setExpiryFlag(String expiryFlag) {
		this.expiryFlag = expiryFlag;
	}

	public Trade() {
		super();
	}
	
	public Trade(String tradeId, int version, String counterParty, String bookId, LocalDate maturityDate, String expiryFlag) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.counterParty = counterParty;
		this.bookId = bookId;
		this.maturityDate = maturityDate;
		this.expiryFlag = expiryFlag;
	}

	@Override
	public String toString() {
		return "Trade [tradeId=" + tradeId + ", version=" + version + ", counterParty=" + counterParty + ", bookId="
				+ bookId + ", maturityDate=" + maturityDate + ", createdDate=" + createdDate + ", expiryFlag="
				+ expiryFlag + "]";
	}
}
