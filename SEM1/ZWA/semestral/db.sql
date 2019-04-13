-- Adminer 4.2.5 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `notebooks`;
CREATE TABLE `notebooks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `headline` varchar(60) COLLATE utf8_czech_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_czech_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci;

INSERT INTO `notebooks` (`id`, `owner_id`, `headline`, `description`) VALUES
(33,	4,	'Rychlé poznámky',	'Sešit pro rychlé poznámky'),
(30,	8,	'Life',	'Piece of ca...shit.');

DROP TABLE IF EXISTS `notes`;
CREATE TABLE `notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notebook_id` int(11) NOT NULL,
  `headline` varchar(60) COLLATE utf8_czech_ci NOT NULL,
  `content` text COLLATE utf8_czech_ci NOT NULL,
  `creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci;

INSERT INTO `notes` (`id`, `notebook_id`, `headline`, `content`, `creation`) VALUES
(39,	33,	'FASTE',	'FASTE POZNAMKA',	'2018-01-31 08:10:54'),
(40,	33,	'Skola',	'Udělat laborku na sítě',	'2018-03-28 08:41:05'),
(34,	30,	'Make somebody smile',	':)',	'2018-01-09 20:44:34'),
(35,	30,	'Make somebody cry',	':\'(',	'2018-01-09 20:44:59'),
(36,	30,	'And then just die',	'x.x',	'2018-01-09 20:45:36'),
(37,	30,	'And now for something completely different',	'nothing',	'2018-01-09 20:55:44');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(60) CHARACTER SET latin1 NOT NULL,
  `hash` varchar(255) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci;

INSERT INTO `users` (`id`, `username`, `hash`) VALUES
(1,	'Vojtik',	'$2y$10$7UeVDn6FjSagxg1eu3L/q.0tW1vaZNXOY0kSJQGw/2NZ8Gz3mNnsG'),
(3,	'marcel',	'$2y$10$c6ebIW9lmLVs6kBshlrYVOaqvPn6.A5M.C2vx0nWsm./d3XTkipWS'),
(4,	'uzi',	'$2y$10$RqQwneltWuZtxrLlBW2O7.oVqd0sZJPxRXPXZ6zAfDJ1X4exA3Vwm'),
(5,	'nejsemtu',	'$2y$10$L.MYylFEPQlUA8Kh3Po11e/oBL3qGdyuP95KnHoDxjZ8LhLEm4Z0.'),
(6,	'novyuzivatelik',	'$2y$10$ov9SnT8EvhyZ1SsQN575FeVRFiclkPN1HFfzi7X2EreAt5mwektFy'),
(7,	'empty',	'$2y$10$0oKkV3BAteA16AIQDf1kG.1AK2UV9tlTV2yZYMNIYekOhxoFeFYbK'),
(8,	'Tripecek',	'$2y$10$rYyCDOK4jv0jzQVJCdEnheL3uwVV352iTLUagOsA.SptwYUmLNaFi'),
(9,	'kajda',	'$2y$10$GgPLmuu9g4IgmAzS2ngbJOv8/b3cljllYwUEAJGD2PEfMZS2xegFu'),
(10,	'mrdat',	'$2y$10$VA5V4yc9dkTPonB7U95rhOmunWh.SJkJCQuKlydc8/Vp.Ysw5i5fS');

-- 2018-04-10 17:52:52