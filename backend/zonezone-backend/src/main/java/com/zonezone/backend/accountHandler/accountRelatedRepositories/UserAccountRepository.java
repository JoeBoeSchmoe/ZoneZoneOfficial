package com.zonezone.backend.accountHandler.accountRelatedRepositories;

import com.zonezone.backend.accountHandler.accountRelatedModels.UserAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends  JpaRepository<UserAccountModel, Long>{
    Optional<UserAccountModel> findByAccountUsername(String username);

    Optional<UserAccountModel> findByAccountEmail(String email);
}
