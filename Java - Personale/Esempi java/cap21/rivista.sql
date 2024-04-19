CREATE TABLE `articoli`.`rivista` (
  `id_rivista` INTEGER  NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(200)  NOT NULL,
  `editore` varchar(200) ,
  `nr` INTEGER  NOT NULL,
  `copertina` varchar(200) NOT NULL DEFAULT 'nofoto.gif',
  PRIMARY KEY (`id_rivista`)
)
ENGINE = InnoDB;
