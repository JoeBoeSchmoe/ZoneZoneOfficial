package com.zonezone.backend.accountHandler.accountRelatedModels;

/// @Author: Joseph Sheets
/// @Updated: 03/14/2025

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
