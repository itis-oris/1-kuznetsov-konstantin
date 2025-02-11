-- Таблица для игроков
CREATE TABLE players (
    player_id SERIAL PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiration_date TIMESTAMP
);

-- Таблица для скинов
CREATE TABLE skins (
    skin_id SERIAL PRIMARY KEY,
    file_path VARCHAR(255) NOT NULL
);

-- Таблица для связи между игроками и скинами
CREATE TABLE player_skins (
    player_skin_id SERIAL PRIMARY KEY,
    player_id INT NOT NULL,
    skin_id INT NOT NULL,
    active_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
    FOREIGN KEY (skin_id) REFERENCES skins(skin_id) ON DELETE CASCADE
);

-- Таблица для пополнения баланса
CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    player_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    comment TEXT,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);