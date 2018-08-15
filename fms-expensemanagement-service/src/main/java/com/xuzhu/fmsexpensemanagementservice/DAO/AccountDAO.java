package com.xuzhu.fmsexpensemanagementservice.DAO;

import com.xuzhu.fmsexpensemanagementservice.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends CrudRepository<Account, String> {

}
