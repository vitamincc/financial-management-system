package com.xuzhu.fmsincomemanagementservice.service;

import com.xuzhu.fmsincomemanagementservice.DAO.AccountDAO;
import com.xuzhu.fmsincomemanagementservice.domain.Account;
import com.xuzhu.fmsincomemanagementservice.domain.Item;
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
public class IncomesServiceImpl implements IncomesService{

    @Autowired
    private AccountDAO accountDAO;

    @Override
    public List<Item> addIncomesItem(String username, Item item) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            account.addIncomItem(item);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
        }
        else return null;

        return account.getIncomes();
    }

    @Override
    public List<Item> deleteIncomesItem(String username, int index) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            List<Item> items = account.getIncomes();
            items.remove(index);
            account.setIncomes(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
        }
        else return null;

        return account.getIncomes();
    }

    @Override
    public List<Item> editIncomesItem(String username, int index, Item item) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            List<Item> items = account.getIncomes();
            items.set(index, item);
            account.setIncomes(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
        }
        else return null;

        return account.getIncomes();
    }

    @Override
    public List<Item> loadIncomes(String username) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            return account.getIncomes();
        }
        else {
            account = new Account();
            account.setUsername(username);
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            account.setIncomes(new ArrayList<>());
            accountDAO.save(account);
            return account.getIncomes();
        }
    }

    @Override
    public boolean deleteIncomeItemFromFinanceManagement(String username, String itemName, String timePoint) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            int index = 0;
            List<Item> items = account.getIncomes();
            for (index = 0; index != items.size(); ++index) {
                Item item = items.get(index);
                if (item.getIncomeItemName().equals(itemName) && item.getIncomeItemTimePoint().equals(timePoint))
                    break;
            }

            items.remove(index);
            account.setIncomes(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
            return true;
        }
        else return false;
    }

    @Override
    public boolean editIncomeItemFromFinanceManagement(String username, Item item) {
        Account account = accountDAO.findOne(username);

        if (account != null) {
            int index = 0;
            List<Item> items = account.getIncomes();
            for (index = 0; index != items.size(); ++index) {
                Item item1 = items.get(index);
                if (item.getIncomeItemName().equals(item1.getIncomeItemName()))
                    break;
            }

            items.set(index, item);
            account.setIncomes(items);
            account.setUpdateTime(new Date());
            accountDAO.save(account);
            return true;
        }
        else return false;
    }

    @Override
    public List<Item> addIncomesItemViaFile(String username, String fileName) {
        Account account = accountDAO.findOne(username);
        List<Item> incomes = new ArrayList<>();

        if (account != null) {
            incomes = account.getIncomes();
        }
        else {
            account = new Account();
            account.setUsername(username);
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            account.setIncomes(incomes);
            accountDAO.save(account);
        }

        String filePath = ".\\IncomeBatchFile\\"+"AddIncomeItem-" + username + "-" + fileName;
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
                                    item.setIncomeItemName(cell.toString());
                                } else if (cIndex == 1) {
                                    BigDecimal num = new BigDecimal(cell.toString());
                                    if (num.compareTo(new BigDecimal(0)) < 0) return null;
                                    item.setIncomeItemAmount(num);
                                } else if (cIndex == 2) {
                                    String mode = cell.toString();
                                    if (mode.equals("现金") || mode.equals("支付宝") || mode.equals("微信") || mode.equals("银行"))
                                        item.setIncomeItemMode(mode);
                                    else return null;
                                } else if (cIndex == 3) {
                                    String source = cell.toString();
                                    if (source.equals("工资") || source.equals("租赁") || source.equals("财产转让") || source.equals("礼金") || source.equals("其他"))
                                        item.setIncomeItemSource(source);
                                    else return null;
                                } else if (cIndex == 4) {
                                    String period = cell.toString();
                                    if (period.equals("一次性收入") || period.equals("本月每天收入") || period.equals("每月收入") || period.equals("每季度收入") || period.equals("每年收入"))
                                        item.setIncomeItemPeriod(period);
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
                                    item.setIncomeItemTimePoint(excelTimeFormatSplit[2] + "-" + excelTimeFormatSplit[1] + "-" + excelTimeFormatSplit[0]);
                                } else if (cIndex == 6) {
                                    item.setIncomeItemInfo(cell.toString());
                                }
                            }
                        }
                        item.setUpdateTime(new Date());
                        incomes.add(item);
                    }
                }
                account.setIncomes(incomes);
                account.setUpdateTime(new Date());
                accountDAO.save(account);
                return incomes;
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
