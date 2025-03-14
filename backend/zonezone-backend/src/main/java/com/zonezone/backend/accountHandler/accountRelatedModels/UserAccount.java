package com.zonezone.backend.accountHandler.accountRelatedModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserAccount {

    @Id
    private Long userAccountID;

}
