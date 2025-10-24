-- Configuration PostgreSQL pour l'application SportManager
ALTER USER postgres WITH PASSWORD 'postgres';
CREATE DATABASE IF NOT EXISTS salle_sport_db OWNER postgres;
GRANT ALL PRIVILEGES ON DATABASE salle_sport_db TO postgres;