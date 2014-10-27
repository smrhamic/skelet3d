CREATE TABLE Model (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  filename VARCHAR(255),
  name VARCHAR(255),
  PRIMARY KEY(id)
);

CREATE TABLE Image (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  filename VARCHAR(255),
  name VARCHAR(255),
  PRIMARY KEY(id)
);

CREATE TABLE Language (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  short VARCHAR(10),
  name VARCHAR(255),
  flag_file VARCHAR(255),
  default_lang BOOLEAN,
  PRIMARY KEY(id)
);

CREATE TABLE Category (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  parent INTEGER,
  latin VARCHAR(255),
  PRIMARY KEY(id),
  FOREIGN KEY(parent)
    REFERENCES Category(id)
      ON DELETE RESTRICT
      ON UPDATE RESTRICT
);

CREATE TABLE UserRole (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  role VARCHAR(255),
  PRIMARY KEY(id)
);

CREATE TABLE Page (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  parent INTEGER,
  latin VARCHAR(255),
  PRIMARY KEY(id),
  FOREIGN KEY(parent)
    REFERENCES Category(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE Label (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  model INTEGER NOT NULL,
  mark_x DOUBLE,
  mark_y DOUBLE,
  mark_z DOUBLE,
  label_x DOUBLE,
  label_y DOUBLE,
  label_z DOUBLE,
  PRIMARY KEY(id),
  FOREIGN KEY(model)
    REFERENCES Model(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE AtlasUser (
  login VARCHAR(255) NOT NULL,
  role INTEGER NOT NULL,
  pass VARCHAR(255),
  salt VARCHAR(255),
  name VARCHAR(255),
  PRIMARY KEY(login),
  FOREIGN KEY(role)
    REFERENCES UserRole(id)
      ON DELETE RESTRICT
      ON UPDATE RESTRICT
);

CREATE TABLE PageContent (
  page INTEGER NOT NULL,
  language INTEGER NOT NULL,
  name VARCHAR(255),
  published BOOLEAN,
  PRIMARY KEY(page, language),
  FOREIGN KEY(page)
    REFERENCES Page(id)
      ON DELETE NO ACTION,
  FOREIGN KEY(Language)
    REFERENCES Language(id)
      ON DELETE NO ACTION
);

CREATE TABLE ImageComponent (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  language INTEGER NOT NULL,
  page INTEGER NOT NULL,
  image INTEGER NOT NULL,
  description CLOB,
  comp_order INTEGER,
  PRIMARY KEY(id),
  FOREIGN KEY(image)
    REFERENCES Image(id)
      ON DELETE RESTRICT,
  FOREIGN KEY(page, Language)
    REFERENCES PageContent(page, language)
      ON DELETE CASCADE
);

CREATE TABLE CategoryInfo (
  category INTEGER NOT NULL,
  language INTEGER NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY(category, language),
  FOREIGN KEY(category)
    REFERENCES Category(id)
      ON DELETE CASCADE,
  FOREIGN KEY(Language)
    REFERENCES Language(id)
);

CREATE TABLE ModelComponent (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  language INTEGER NOT NULL,
  page INTEGER NOT NULL,
  model INTEGER NOT NULL,
  description CLOB,
  comp_order INTEGER,
  PRIMARY KEY(id),
  FOREIGN KEY(model)
    REFERENCES Model(id)
      ON DELETE RESTRICT,
  FOREIGN KEY(page, language)
    REFERENCES PageContent(page, language)
      ON DELETE CASCADE
);

CREATE TABLE LabelContent (
  label INTEGER NOT NULL,
  language INTEGER NOT NULL,
  title VARCHAR(255),
  text CLOB,
  PRIMARY KEY(label, language),
  FOREIGN KEY(language)
    REFERENCES Language(id)
      ON DELETE NO ACTION,
  FOREIGN KEY(label)
    REFERENCES Label(id)
      ON DELETE NO ACTION
);

CREATE TABLE HeadlineComponent (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  language INTEGER NOT NULL,
  page INTEGER NOT NULL,
  text VARCHAR(255),
  comp_order INTEGER,
  PRIMARY KEY(id),
  FOREIGN KEY(page, Language)
    REFERENCES PageContent(page, language)
      ON DELETE CASCADE
);

CREATE TABLE TextComponent (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  language INTEGER NOT NULL,
  page INTEGER NOT NULL,
  text CLOB,
  comp_order INTEGER,
  PRIMARY KEY(id),
  FOREIGN KEY(page, Language)
    REFERENCES PageContent(page, language)
      ON DELETE CASCADE
);

CREATE INDEX CategoryFKIndex ON Category(parent);
CREATE INDEX PageFKIndex ON Page(parent);
CREATE INDEX PageContentFKIndex1 ON PageContent(page);
CREATE INDEX PageContentFKIndex2 ON PageContent(language);
CREATE INDEX TextComponentFKIndex ON TextComponent(page, language);
CREATE INDEX HeadlineComponentFKIndex ON HeadlineComponent(page, language);
CREATE INDEX LabelContentFKIndex1 ON LabelContent(language);
CREATE INDEX LabelContentFKIndex2 ON LabelContent(label);
CREATE INDEX ModelComponentFKIndex1 ON ModelComponent(model);
CREATE INDEX ModelComponentFKIndex2 ON ModelComponent(page, language);
CREATE INDEX CategoryInfoFKIndex1 ON CategoryInfo(category);
CREATE INDEX CategoryInfoFKIndex2 ON CategoryInfo(language);
CREATE INDEX AtlasUserFKIndex ON AtlasUser(role);
CREATE INDEX ImageComponentFKIndex1 ON ImageComponent(image);
CREATE INDEX ImageComponentFKIndex2 ON ImageComponent(page, language);
CREATE INDEX LabelFKIndex ON Label(model);