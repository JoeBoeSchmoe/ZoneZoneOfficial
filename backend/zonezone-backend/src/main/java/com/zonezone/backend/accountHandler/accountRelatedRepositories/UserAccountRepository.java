package com.zonezone.backend.accountHandler.accountRelatedRepositories;

import com.zonezone.backend.accountHandler.accountRelatedModels.UserAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends  JpaRepository<UserAccountModel, Long>{
    // Custom query methods can be defined here

}
