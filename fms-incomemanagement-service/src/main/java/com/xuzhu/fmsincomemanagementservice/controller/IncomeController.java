package com.xuzhu.fmsincomemanagementservice.controller;


import com.xuzhu.fmsincomemanagementservice.service.IncomesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xuzhu.fmsincomemanagementservice.domain.Item;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.NewThreadAction;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@RestController
public class IncomeController {

    @Autowired
    IncomesService incomesService;

    @RequestMapping(value = "/addIncomeItem/{username}", method = RequestMethod.POST)
    public List<Item> addIncomeItem(@PathVariable String username, @Valid @RequestBody Item item) {
        return incomesService.addIncomesItem(username, item);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public List<Item> loadIncomes(@PathVariable String username) {
        return incomesService.loadIncomes(username);
    }

    @RequestMapping(value = "/deleteIncomeItem/{username}", method = RequestMethod.POST)
    public List<Item> deleteIncomeItem(@PathVariable String username, @Valid @RequestParam String index) {
        return incomesService.deleteIncomesItem(username, Integer.parseInt(index));
    }

    @RequestMapping(value = "/deleteIncomeItemFromFinanceManagement/{username}/{itemName}/{timePoint}", method = RequestMethod.POST)
    public boolean deleteIncomeItemFromFinanceManagement(@PathVariable String username, @PathVariable String itemName, @PathVariable String timePoint) {
        return incomesService.deleteIncomeItemFromFinanceManagement(username, itemName, timePoint);
    }

    @RequestMapping(value = "/editIncomeItem/{username}/{index}", method = RequestMethod.POST)
    public List<Item> editIncomeItem(@PathVariable String username, @Valid @PathVariable String index, @Valid @RequestBody Item item) {
        return incomesService.editIncomesItem(username, Integer.parseInt(index), item);
    }

    @RequestMapping(value = "/editIncomeItem/{username}", method = RequestMethod.POST)
    public boolean editIncomeItemFromFinanceManagement(@PathVariable String username, @Valid @RequestBody Item item) {
        return incomesService.editIncomeItemFromFinanceManagement(username, item);
    }

    @RequestMapping(value = "/uploadFile/{username}", method = RequestMethod.POST)
    public List<Item> uploadIncomeData(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(".\\IncomeBatchFile\\"+"AddIncomeItem-" + username + "-" + file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
                return incomesService.addIncomesItemViaFile(username, file.getOriginalFilename());
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else return null;
    }
}
