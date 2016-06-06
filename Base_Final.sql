/*
SQLyog Ultimate v12.08 (32 bit)
MySQL - 5.6.30-0ubuntu0.14.04.1-log : Database - java-final
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`java-final` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `java-final`;

/*Table structure for table `cuentas` */

DROP TABLE IF EXISTS `cuentas`;

CREATE TABLE `cuentas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `persona_id` int(11) DEFAULT NULL,
  `tipo_cuenta_id` int(11) DEFAULT NULL,
  `sucursal_id` int(11) DEFAULT NULL,
  `numero_cuenta` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `saldo` int(11) NOT NULL DEFAULT '0',
  `fecha_apertura` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `tipo_cuenta_id` (`tipo_cuenta_id`),
  KEY `persona_id` (`persona_id`),
  KEY `sucursal_id` (`sucursal_id`),
  CONSTRAINT `cuentas_ibfk_1` FOREIGN KEY (`tipo_cuenta_id`) REFERENCES `tipo_cuentas` (`id`),
  CONSTRAINT `cuentas_ibfk_2` FOREIGN KEY (`persona_id`) REFERENCES `personas` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cuentas_ibfk_3` FOREIGN KEY (`sucursal_id`) REFERENCES `sucursales` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `cuentas` */

insert  into `cuentas`(`id`,`persona_id`,`tipo_cuenta_id`,`sucursal_id`,`numero_cuenta`,`saldo`,`fecha_apertura`) values (1,3,1,1,'1232132132-1',998800,'2016-06-05 07:48:13'),(2,3,3,3,'1232132132-2',1993800,'2016-06-05 03:04:03');

/*Table structure for table `movimientos` */

DROP TABLE IF EXISTS `movimientos`;

CREATE TABLE `movimientos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cuenta_id` int(11) DEFAULT NULL,
  `tipo_movimiento_id` int(11) DEFAULT NULL,
  `sucursal_id` int(11) DEFAULT NULL,
  `saldo_anterior` int(11) DEFAULT NULL,
  `saldo` int(11) DEFAULT NULL,
  `valor_movimiento` int(11) DEFAULT '0',
  `costo_movimiento` int(11) DEFAULT NULL,
  `fecha_movimiento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `tipo_movimiento_id` (`tipo_movimiento_id`),
  KEY `cuenta_id` (`cuenta_id`),
  KEY `sucursal_id` (`sucursal_id`),
  CONSTRAINT `movimientos_ibfk_1` FOREIGN KEY (`tipo_movimiento_id`) REFERENCES `tipo_movimientos` (`id`),
  CONSTRAINT `movimientos_ibfk_2` FOREIGN KEY (`cuenta_id`) REFERENCES `cuentas` (`id`) ON DELETE CASCADE,
  CONSTRAINT `movimientos_ibfk_3` FOREIGN KEY (`sucursal_id`) REFERENCES `sucursales` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `movimientos` */

insert  into `movimientos`(`id`,`cuenta_id`,`tipo_movimiento_id`,`sucursal_id`,`saldo_anterior`,`saldo`,`valor_movimiento`,`costo_movimiento`,`fecha_movimiento`) values (1,1,1,2,1700000,1595000,100000,5000,'2016-06-05 06:09:54'),(2,1,3,1,1595000,1693800,100000,1200,'2016-06-05 06:11:54'),(3,1,2,2,1693800,1692600,0,1200,'2016-06-05 06:15:21'),(5,2,3,3,0,998800,1000000,1200,'2016-06-05 06:20:33'),(6,2,3,1,998800,1993800,1000000,5000,'2016-06-05 06:22:45');

/*Table structure for table `personas` */

DROP TABLE IF EXISTS `personas`;

CREATE TABLE `personas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num_ident` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nombres` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `correo` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `celular` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imagen` longblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `personas` */

insert  into `personas`(`id`,`num_ident`,`nombres`,`correo`,`celular`,`imagen`) values (1,'1125183545','Wilmer Muñoz','WilmerMG@Outlook.com','3143492119',NULL),(2,'1292032312','Carlos Garcia','kalliche1996@gmail.com','hola@msn.com',NULL),(3,'1232132132','Andres Velasquez','rosvel@mail.com','3104314143',NULL);

/*Table structure for table `sucursales` */

DROP TABLE IF EXISTS `sucursales`;

CREATE TABLE `sucursales` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ciudad` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `direccion` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sucursales` */

insert  into `sucursales`(`id`,`nombre`,`ciudad`,`direccion`) values (1,'Bancolombia','Mocoa','Av. Colombia'),(2,'Sucursal Banco de Bogota','Pasto','Exito Panamericana '),(3,'Banco BBVA','Villagarzon','Parque principal');

/*Table structure for table `tipo_cuentas` */

DROP TABLE IF EXISTS `tipo_cuentas`;

CREATE TABLE `tipo_cuentas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `tipo_cuentas` */

insert  into `tipo_cuentas`(`id`,`nombre`) values (1,'Ahorro'),(2,'Corriente'),(3,'Nomina');

/*Table structure for table `tipo_movimientos` */

DROP TABLE IF EXISTS `tipo_movimientos`;

CREATE TABLE `tipo_movimientos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `costo_local` int(11) DEFAULT NULL,
  `costo_remoto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `tipo_movimientos` */

insert  into `tipo_movimientos`(`id`,`nombre`,`costo_local`,`costo_remoto`) values (1,'Retiro',1200,5000),(2,'Consulta de Saldo',1200,1200),(3,'Consignación',1200,5000);

/*Table structure for table `usuarios` */

DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `persona_id` int(11) NOT NULL,
  `usuario` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `llave` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `rol` enum('Administrador','Funcionario','Cliente','Invitado') COLLATE utf8_unicode_ci DEFAULT 'Cliente',
  PRIMARY KEY (`id`),
  KEY `persona_id` (`persona_id`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`persona_id`) REFERENCES `personas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `usuarios` */

insert  into `usuarios`(`id`,`persona_id`,`usuario`,`llave`,`rol`) values (1,1,'wito','*A529BA5688E8F86FC363FB2E215C73DEFBD0B9EC','Administrador'),(2,3,'rosvel','*FCD19738AB03CA5EE7D13BF81D3F7B0B5605DE76','Cliente');

/* Trigger structure for table `movimientos` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `movimientos_bf_insert` */$$

/*!50003 CREATE */ /*!50003 TRIGGER `movimientos_bf_insert` BEFORE INSERT ON `movimientos` FOR EACH ROW BEGIN
	declare COSTO INT;
	DECLARE SUCURSAL_CUENTA INT;
	
	SET SUCURSAL_CUENTA = (SELECT sucursal_id FROM cuentas WHERE id=new. cuenta_id);
	set COSTO = (SELECT IF(SUCURSAL_CUENTA=new.sucursal_id,costo_local,costo_remoto) from tipo_movimientos where id=new.tipo_movimiento_id limit 1);
	SET new.costo_movimiento = COSTO;
	SET new.saldo_anterior=(SELECT saldo FROM cuentas WHERE id=new.cuenta_id LIMIT 1);
	
	if new.tipo_movimiento_id in (1,2) then
		UPDATE cuentas SET cuentas.saldo=cuentas.saldo-(new.valor_movimiento+COSTO) where id=new.cuenta_id;
	else
		UPDATE cuentas SET cuentas.saldo=(cuentas.saldo+new.valor_movimiento)-COSTO  WHERE id=new.cuenta_id;
	end if;	
	set new.saldo=(SELECT saldo FROM cuentas WHERE id=new.cuenta_id limit 1);
    END */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
