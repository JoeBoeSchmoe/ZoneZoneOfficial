package com.zonezone.backend.accountHandler.accountRelatedModels;

/// @Author: Joseph Sheets
/// @Updated: 03/14/2025

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_accounts") // Custom table name
public class UserAccountModel {

    @Id
    private Long userAccountID;

}
