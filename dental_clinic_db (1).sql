-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 04, 2026 at 03:52 PM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dental_clinic_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
CREATE TABLE IF NOT EXISTS `appointments` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int NOT NULL,
  `dentist_id` int NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_time` time NOT NULL,
  `treatment` varchar(100) DEFAULT NULL,
  `status` enum('Scheduled','Completed','Cancelled') DEFAULT 'Scheduled',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `fk_dentist` (`dentist_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`appointment_id`, `patient_id`, `dentist_id`, `appointment_date`, `appointment_time`, `treatment`, `status`, `created_at`) VALUES
(1, 1, 1, '2026-07-30', '09:00:00', 'Teeth check', 'Completed', '2026-07-29 06:42:13'),
(2, 2, 1, '2026-07-31', '09:40:00', 'Filling teeth', 'Completed', '2026-07-29 07:04:35'),
(3, 3, 3, '2026-08-20', '09:00:00', 'Teeth check', 'Completed', '2026-08-18 10:37:12'),
(5, 1, 7, '2026-09-05', '10:00:00', 'Filling teeth', 'Completed', '2026-09-04 14:00:41');

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
CREATE TABLE IF NOT EXISTS `bills` (
  `bill_id` int NOT NULL AUTO_INCREMENT,
  `appointment_id` int NOT NULL,
  `patient_id` int NOT NULL,
  `dentist_id` int NOT NULL,
  `treatment_charge` decimal(10,2) NOT NULL DEFAULT '0.00',
  `discount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `amount_paid` decimal(10,2) NOT NULL DEFAULT '0.00',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `status` varchar(30) NOT NULL DEFAULT 'Unpaid',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`bill_id`),
  KEY `appointment_id` (`appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `dentist_id` (`dentist_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`bill_id`, `appointment_id`, `patient_id`, `dentist_id`, `treatment_charge`, `discount`, `total_amount`, `amount_paid`, `balance`, `status`, `created_at`) VALUES
(1, 1, 1, 1, 23000.00, 2100.00, 20900.00, 20900.00, 0.00, 'Paid', '2026-08-17 10:12:01'),
(2, 2, 2, 1, 8500.00, 750.00, 7750.00, 7750.00, 0.00, 'Paid', '2026-08-18 04:20:24'),
(3, 5, 1, 7, 7800.00, 890.00, 6910.00, 6000.00, 910.00, 'Partial', '2026-09-04 14:06:03');

-- --------------------------------------------------------

--
-- Table structure for table `bill_payments`
--

DROP TABLE IF EXISTS `bill_payments`;
CREATE TABLE IF NOT EXISTS `bill_payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `bill_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(30) DEFAULT 'Cash',
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `received_by` int DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `bill_id` (`bill_id`),
  KEY `received_by` (`received_by`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dentists`
--

DROP TABLE IF EXISTS `dentists`;
CREATE TABLE IF NOT EXISTS `dentists` (
  `dentist_id` int NOT NULL AUTO_INCREMENT,
  `dentist_name` varchar(100) NOT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`dentist_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `dentists`
--

INSERT INTO `dentists` (`dentist_id`, `dentist_name`, `specialization`, `phone`, `email`, `user_id`) VALUES
(1, 'Dr. Nimal Perera', 'General Dentistry', '0712531423', 'nimal@dental.com', NULL),
(2, 'Dr. Kasun Silva', 'Orthodontics', '0722222222', 'kasun@dental.com', NULL),
(3, 'Dr. Dinithi Fernando', 'Root Canal Specialist', '0712589631', 'dinithi@dental.com', NULL),
(5, 'Dr. Bavindu Fernado', 'Root Canal Specialist', '0712996931', 'bavindudeshapriya@gmail.com', 3),
(7, 'Dr. Upeksha Weerasinha', 'Root Canal Specialist', '0719081468', 'upeksha@gmail.com', 5);

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
CREATE TABLE IF NOT EXISTS `patients` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `date_of_birth` date NOT NULL,
  `phone` varchar(15) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`patient_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patient_id`, `first_name`, `last_name`, `gender`, `date_of_birth`, `phone`, `email`, `address`, `created_at`) VALUES
(1, 'Hasindu', 'Chamath', 'Male', '2001-05-28', '0702996931', 'hasindudeshapriya@gmail.com', 'Udumulla Watta\r\nMoralagala', '2026-07-28 16:18:33'),
(2, 'Hasini', 'Chamathka', 'Female', '2012-07-11', '0702996631', 'hasindudeshapriya2012@gmail.com', 'Udumulla Watta\r\nMoralagala', '2026-07-29 07:03:31'),
(3, 'Naduni', 'Harshika', 'Female', '2013-07-04', '0702996900', 'naduu@gmail.com', 'Kaluthara', '2026-08-18 10:36:32'),
(8, 'Dikki', 'Maeesh', 'Male', '2012-03-26', '1234567890', 'dikki@gmail.com', 'Rathnapura', '2026-08-26 08:05:16');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE',
  `dentist_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_users_dentist` (`dentist_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `full_name`, `email`, `role`, `status`, `dentist_id`, `created_at`) VALUES
(1, 'admin', 'admin123', 'System Administrator', NULL, 'ADMIN', 'ACTIVE', NULL, '2026-08-09 16:00:53'),
(2, 'Hasi@2001', '123456', 'Hasindu Chamath', NULL, 'CASHIER', 'ACTIVE', NULL, '2026-08-19 18:40:36'),
(3, 'Bavi@dentist', '789456', 'Bavindu', NULL, 'DENTIST', 'ACTIVE', 5, '2026-08-20 07:29:58'),
(4, 'Ravindu', '852369', 'Ravindu Deshapriya', NULL, 'CASHIER', 'ACTIVE', NULL, '2026-08-20 15:46:31'),
(5, 'UpekshaDR', 'Upeksha@2000', 'Upeksha Weerasinha', NULL, 'DENTIST', 'ACTIVE', 7, '2026-09-04 13:57:40');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
