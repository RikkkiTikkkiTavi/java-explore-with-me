drop table IF EXISTS users CASCADE;
drop table IF EXISTS locations CASCADE;
drop table IF EXISTS categories CASCADE;
drop table IF EXISTS events CASCADE;
drop table IF EXISTS compilations CASCADE;
drop table IF EXISTS compilation_events CASCADE;
drop table IF EXISTS participation_requests CASCADE;

create TABLE IF NOT EXISTS users (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(64) UNIQUE NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS locations (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  CONSTRAINT pk_location PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS categories (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) UNIQUE NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS events (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  category_id INT NOT NULL,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE,
  published TIMESTAMP WITHOUT TIME ZONE,
  created TIMESTAMP WITHOUT TIME ZONE,
  location_id INT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INT NOT NULL,
  request_moderation BOOLEAN NOT NULL,
  title VARCHAR(255) NOT NULL,
  initiator_id INT NOT NULL,
  state VARCHAR(64) NOT NULL,
  confirmed_requests INT,
  CONSTRAINT pk_event PRIMARY KEY (id),
  CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id),
  CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations(id),
  CONSTRAINT fk_user FOREIGN KEY (initiator_id) REFERENCES users(id)
);

create TABLE IF NOT EXISTS participation_requests (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  event_id INT NOT NULL,
  requester_id INT NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE,
  status VARCHAR(255) NOT NULL,
  CONSTRAINT pk_participation PRIMARY KEY (id),
  CONSTRAINT fk_event_req FOREIGN KEY (event_id) REFERENCES events(id),
  CONSTRAINT fk_user_req FOREIGN KEY (requester_id) REFERENCES users(id)
);

create TABLE IF NOT EXISTS compilations (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(255) NOT NULL,
  CONSTRAINT pk_compilation PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS compilation_events (
  compilation_id INT NOT NULL,
  event_id INT NOT NULL,
  CONSTRAINT fk_compilation FOREIGN KEY (compilation_id) REFERENCES compilations(id),
  CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id)
);