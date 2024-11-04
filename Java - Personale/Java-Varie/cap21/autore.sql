CREATE TABLE `articoli`.`autore` (
  `id_autore` INTEGER  NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100)  NOT NULL,
  `cognome` VARCHAR(100)  NOT NULL,
  `indirizzo` varchar(250) ,
  `citta` varchar(100) ,
  `telefono` varchar(20) ,
  `email` varchar(50) ,
  `professione` varchar(100) ,
  `foto` varchar(200)  NOT NULL DEFAULT 'nofoto.gif',
  PRIMARY KEY (`id_autore`)
)
ENGINE = InnoDB;
