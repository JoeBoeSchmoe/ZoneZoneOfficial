CREATE TABLE IF NOT EXISTS friended_users_table (
    user_account_id BIGINT REFERENCES user_accounts(user_accountid) ON DELETE CASCADE,
    friended_user BIGINT NOT NULL,
    PRIMARY KEY (user_account_id, friended_user)
);

CREATE TABLE IF NOT EXISTS blocked_users_table (
    user_account_id BIGINT REFERENCES user_accounts(user_accountid) ON DELETE CASCADE,
    blocked_user BIGINT NOT NULL,
    PRIMARY KEY (user_account_id, blocked_user)
);
