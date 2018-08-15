package com.xuzhu.fmsincomemanagementservice.service;

import com.xuzhu.fmsincomemanagementservice.domain.Account;
import com.xuzhu.fmsincomemanagementservice.domain.Item;

import java.util.List;

public interface IncomesService {
    List<Item> loadIncomes(String username);
    List<Item> addIncomesItem(String username, Item item);
    List<Item> deleteIncomesItem(String username, int index);
    List<Item> editIncomesItem(String username, int index, Item item);
    boolean deleteIncomeItemFromFinanceManagement(String username, String itemName, String timePoint);
    boolean editIncomeItemFromFinanceManagement(String username, Item item);
    List<Item> addIncomesItemViaFile(String username, String fileName);
}
