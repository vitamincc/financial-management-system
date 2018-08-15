package com.xuzhu.fmsexpensemanagementservice.controller;

import com.xuzhu.fmsexpensemanagementservice.domain.Item;
import com.xuzhu.fmsexpensemanagementservice.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    ExpensesService expensesService;

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public List<Item> loadExpenses(@PathVariable String username) {
        return expensesService.loadExpenses(username);
    }

    @RequestMapping(value = "/addExpenseItem/{username}", method = RequestMethod.POST)
    public List<Item> addExpenseItem(@PathVariable String username, @Valid @RequestBody Item item) {
        return expensesService.addExpensesItem(username, item);
    }

    @RequestMapping(value = "/deleteExpenseItem/{username}", method = RequestMethod.POST)
    public List<Item> deleteExpenseItem(@PathVariable String username, @Valid @RequestParam String index) {
        return expensesService.deleteExpensesItem(username, Integer.parseInt(index));
    }

    @RequestMapping(value = "/deleteExpenseItemFromFinanceManagement/{username}/{itemName}/{timePoint}", method = RequestMethod.POST)
    public boolean deleteExpenseItemFromFinanceManagement(@PathVariable String username, @PathVariable String itemName, @PathVariable String timePoint) {
        return expensesService.deleteExpenseItemFromFinanceManagement(username, itemName, timePoint);
    }

    @RequestMapping(value = "/editExpenseItem/{username}/{index}", method = RequestMethod.POST)
    public List<Item> editExpenseItem(@PathVariable String username, @Valid @PathVariable String index, @Valid @RequestBody Item item) {
        return expensesService.editExpensesItem(username, Integer.parseInt(index), item);
    }

    @RequestMapping(value = "/editExpenseItem/{username}", method = RequestMethod.POST)
    public boolean editExpenseItem(@PathVariable String username, @Valid @RequestBody Item item) {
        return expensesService.editExpenseItemFromFinanceManagement(username, item);
    }

    @RequestMapping(value = "/uploadFile/{username}", method = RequestMethod.POST)
    public List<Item> uploadExpenseData(@PathVariable String username, @RequestParam("file")MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(".\\ExpenseBatchFile\\"+"AddExpenseItem-" + username + "-" + file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
                return expensesService.addExpensesItemViaFile(username, file.getOriginalFilename());
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else return null;
    }
}
