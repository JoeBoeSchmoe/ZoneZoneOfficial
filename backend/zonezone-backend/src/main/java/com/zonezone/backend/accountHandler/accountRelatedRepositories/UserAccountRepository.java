package com.zonezone.backend.accountHandler.accountRelatedRepositories;

import com.zonezone.backend.accountHandler.accountRelatedModels.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends  JpaRepository<UserAccount, Long>{
    // Custom query methods can be defined here

}
