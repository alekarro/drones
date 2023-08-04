CREATE TABLE IF NOT EXISTS drone (
   serial_number VARCHAR(100) NOT NULL,
   model VARCHAR(50) NOT NULL,
   weight_limit INTEGER NOT NULL,

   PRIMARY KEY(serial_number)

);

CREATE TABLE IF NOT EXISTS drone_state (
    serial_number VARCHAR(100) NOT NULL,
    battery_level INTEGER NOT NULL,
    state VARCHAR(50) NULL,
    loaded_weight INTEGER NULL,

    PRIMARY KEY(serial_number)
);

CREATE TABLE IF NOT EXISTS medication (
   code VARCHAR(100) NOT NULL,
   name VARCHAR(255) NOT NULL,
   weight INTEGER NOT NULL,
   image BYTEA NULL,

   PRIMARY KEY(code)

);

CREATE TABLE IF NOT EXISTS loading (
   drone_code VARCHAR(100) NOT NULL,
   medication_code VARCHAR(100) NOT NULL,
   quantity integer NOT NULL,

   PRIMARY KEY(drone_code, medication_code),
   CONSTRAINT fk_loading_drone_code
        FOREIGN KEY (drone_code)
        REFERENCES drone (serial_number)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
   CONSTRAINT fk_loading_medication_code
        FOREIGN KEY (medication_code)
        REFERENCES medication (code)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS audit_drone (
    serial_number VARCHAR(100) NOT NULL,
    battery_level INTEGER NOT NULL,
    date TIMESTAMP DEFAULT NOW()
);