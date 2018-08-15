package com.xuzhu.fmsexpensemanagementservice.service;

import com.xuzhu.fmsexpensemanagementservice.DAO.AccountDAO;
import com.xuzhu.fmsexpensemanagementservice.domain.Account;
import com.xuzhu.fmsexpensemanagementservice.domain.Item;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExpensesServiceImpl implements ExpensesService {
    @Autowired
    private AccountDAO accountDAO;

    @Override
    public List<Item> addExpensesItem(String username, Item item) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            account.addExpenseItem(item);
            accountDAO.save(account);
        }
        else return null;

        return account.getExpenses();
    }

    @Override
    public List<Item> deleteExpensesItem(String username, int index) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            List<Item> items = account.getExpenses();
            items.remove(index);
            account.setExpenses(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
        }
        else return null;

        return account.getExpenses();
    }

    @Override
    public List<Item> editExpensesItem(String username, int index, Item item) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            List<Item> items = account.getExpenses();
            items.set(index, item);
            account.setExpenses(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
        }
        else return null;

        return account.getExpenses();
    }

    @Override
    public List<Item> loadExpenses(String username) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            return account.getExpenses();
        }
        else {
            account = new Account();
            account.setUsername(username);
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            account.setExpenses(new ArrayList<>());
            accountDAO.save(account);
            return account.getExpenses();
        }
    }

    @Override
    public boolean deleteExpenseItemFromFinanceManagement(String username, String itemName, String timePoint) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            List<Item> items = account.getExpenses();
            int index = 0;
            for (index = 0; index != items.size(); ++index) {
                Item item = items.get(index);
                if (item.getExpenseItemName().equals(itemName) && item.getExpenseItemTimePoint().equals(timePoint))
                    break;
            }
            items.remove(index);
            account.setExpenses(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
            return true;
        }
        else return false;
    }

    @Override
    public boolean editExpenseItemFromFinanceManagement(String username, Item item) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            int index = 0;
            List<Item> items = account.getExpenses();
            for (index = 0; index != items.size(); ++index) {
                Item item1 = items.get(index);
                if (item.getExpenseItemName().equals(item1.getExpenseItemName()))
                    break;
            }

            items.set(index, item);
            account.setExpenses(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
            return true;
        }
        else return false;
    }

    @Override
    public List<Item> addExpensesItemViaFile(String username, String filename) {
        Account account = accountDAO.findOne(username);
        List<Item> expenses = new ArrayList<>();

        if (account != null) {
            expenses = account.getExpenses();
        }
        else {
            account = new Account();
            account.setUsername(username);
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            account.setExpenses(expenses);
        }

        String filePath = ".\\ExpenseBatchFile\\"+"AddExpenseItem-" + username + "-" + filename;
        try {
            File excel = new File(filePath);
            if (excel.isFile() && excel.exists()) {
                String[] split = excel.getName().split("\\.");
                Workbook wb;
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("文件类型错误!");
                    return null;
                }

                Sheet sheet = wb.getSheetAt(0);

                int firstRowIndex = sheet.getFirstRowNum()+1;
                int lastRowIndex = sheet.getLastRowNum();

                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        Item item = new Item();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                if (cIndex == 0) {
                                    if (cell.toString().equals("")) return null;
                                    item.setExpenseItemName(cell.toString());
                                } else if (cIndex == 1) {
                                    BigDecimal num = new BigDecimal(cell.toString());
                                    if (num.compareTo(new BigDecimal(0)) < 0) return null;
                                    item.setExpenseItemAmount(num);
                                } else if (cIndex == 2) {
                                    String mode = cell.toString();
                                    if (mode.equals("现金") || mode.equals("支付宝") || mode.equals("微信") || mode.equals("银行"))
                                        item.setExpenseItemMode(cell.toString());
                                    else return null;
                                } else if (cIndex == 3) {
                                    String source = cell.toString();
                                    if (source.equals("交通") || source.equals("租赁") || source.equals("教育") || source.equals("衣服") || source.equals("饮食") || source.equals("旅游") || source.equals("运动") || source.equals("医疗") || source.equals("水电费") || source.equals("其他"))
                                        item.setExpenseItemSource(source);
                                    else return null;
                                } else if (cIndex == 4) {
                                    String period = cell.toString();
                                    if (period.equals("一次性支出") || period.equals("本月每天支出") || period.equals("每月支出") || period.equals("每季度支出") || period.equals("每年支出"))
                                        item.setExpenseItemPeriod(cell.toString());
                                    else return null;
                                } else if (cIndex == 5) {
                                    String excelTimeFormat = cell.toString();
                                    String[] excelTimeFormatSplit = excelTimeFormat.split("-");
                                    if (excelTimeFormatSplit.length != 3) return null;
                                    switch (excelTimeFormatSplit[1]) {
                                        case "一月":
                                            excelTimeFormatSplit[1] = "01";
                                            break;
                                        case "二月":
                                            excelTimeFormatSplit[1] = "02";
                                            break;
                                        case "三月":
                                            excelTimeFormatSplit[1] = "03";
                                            break;
                                        case "四月":
                                            excelTimeFormatSplit[1] = "04";
                                            break;
                                        case "五月":
                                            excelTimeFormatSplit[1] = "05";
                                            break;
                                        case "六月":
                                            excelTimeFormatSplit[1] = "06";
                                            break;
                                        case "七月":
                                            excelTimeFormatSplit[1] = "07";
                                            break;
                                        case "八月":
                                            excelTimeFormatSplit[1] = "08";
                                            break;
                                        case "九月":
                                            excelTimeFormatSplit[1] = "09";
                                            break;
                                        case "十月":
                                            excelTimeFormatSplit[1] = "10";
                                            break;
                                        case "十一月":
                                            excelTimeFormatSplit[1] = "11";
                                            break;
                                        case "十二月":
                                            excelTimeFormatSplit[1] = "12";
                                            break;
                                        default:
                                            return null;
                                    }
                                    item.setExpenseItemTimePoint(excelTimeFormatSplit[2] + "-" + excelTimeFormatSplit[1] + "-" + excelTimeFormatSplit[0]);
                                } else if (cIndex == 6) {
                                    item.setExpenseItemInfo(cell.toString());
                                }
                            }
                        }
                        item.setUpdateTime(new Date());
                        expenses.add(item);
                    }
                }
                account.setExpenses(expenses);
                account.setUpdateTime(new Date());
                accountDAO.save(account);
                return expenses;
            }
            else {
                System.out.println("找不到指定文件");
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }
}
