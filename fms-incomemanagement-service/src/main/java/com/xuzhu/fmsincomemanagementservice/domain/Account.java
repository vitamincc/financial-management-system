package com.xuzhu.fmsincomemanagementservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "IncomeAccount")
public class Account {
    @Id
    private String username;
    private Date createTime;
    private Date updateTime;
    private List<Item> incomes;

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return username; }

    public void setIncomes(List<Item> income) { this.incomes = income; }

    public List<Item> getIncomes() { return incomes; }

    public void addIncomItem(Item item) {
        if (incomes != null) {
            incomes.add(item);
        }
        else {
            incomes = new ArrayList<>();
            incomes.add(item);
        }
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
