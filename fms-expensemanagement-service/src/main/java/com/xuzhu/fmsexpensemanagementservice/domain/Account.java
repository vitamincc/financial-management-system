package com.xuzhu.fmsexpensemanagementservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "ExpenseAccount")
public class Account {
    @Id
    private String username;
    private Date createTime;
    private Date updateTime;
    private List<Item> expenses;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setExpenses(List<Item> expenses) {
        this.expenses = expenses;
    }

    public List<Item> getExpenses() {
        return expenses;
    }

    public void addExpenseItem(Item item) {
        if (expenses != null) {
            expenses.add(item);
        }
        else {
            expenses = new ArrayList<>();
            expenses.add(item);
        }
    }
}
