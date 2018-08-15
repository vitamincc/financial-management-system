package com.xuzhu.fmsexpensemanagementservice.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Item {
    private String expenseItemName;
    private BigDecimal expenseItemAmount;
    private String expenseItemTimePoint;
    private String expenseItemSource;
    private String expenseItemMode;
    private String expenseItemPeriod;
    private Date updateTime;
    private String expenseItemInfo;

    public void setExpenseItemName(String expenseItemName) {
        this.expenseItemName = expenseItemName;
    }

    public String getExpenseItemName() {
        return expenseItemName;
    }

    public void setExpenseItemAmount(BigDecimal expenseItemAmount) {
        this.expenseItemAmount = expenseItemAmount;
    }

    public BigDecimal getExpenseItemAmount() {
        return expenseItemAmount;
    }

    public void setExpenseItemTimePoint(String expenseItemTimePoint) {
        this.expenseItemTimePoint = expenseItemTimePoint;
    }

    public String getExpenseItemTimePoint() {
        return expenseItemTimePoint;
    }

    public void setExpenseItemSource(String expenseItemSource) {
        this.expenseItemSource = expenseItemSource;
    }

    public String getExpenseItemSource() {
        return expenseItemSource;
    }

    public void setExpenseItemMode(String expenseItemMode) {
        this.expenseItemMode = expenseItemMode;
    }

    public String getExpenseItemMode() {
        return expenseItemMode;
    }

    public void setExpenseItemPeriod(String expenseItemPeriod) {
        this.expenseItemPeriod = expenseItemPeriod;
    }

    public String getExpenseItemPeriod() {
        return expenseItemPeriod;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setExpenseItemInfo(String expenseItemInfo) {
        this.expenseItemInfo = expenseItemInfo;
    }

    public String getExpenseItemInfo() {
        return expenseItemInfo;
    }
}
