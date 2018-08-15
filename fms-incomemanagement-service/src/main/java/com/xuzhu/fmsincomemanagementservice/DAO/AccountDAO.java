package com.xuzhu.fmsincomemanagementservice.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.xuzhu.fmsincomemanagementservice.domain.Account;

@Repository
public interface AccountDAO extends CrudRepository<Account, String> {

}
