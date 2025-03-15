CREATE TABLE IF NOT EXISTS recorded_time_model (
    id BIGSERIAL PRIMARY KEY,
    user_account_id BIGINT REFERENCES user_accounts(user_accountid) ON DELETE CASCADE,
    days_played BIGINT DEFAULT 0,
    hours_played BIGINT DEFAULT 0,
    minutes_played BIGINT DEFAULT 0,
    seconds_played BIGINT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
