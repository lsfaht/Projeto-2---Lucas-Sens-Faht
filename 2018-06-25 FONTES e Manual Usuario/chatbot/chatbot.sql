-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 19-Mar-2018 às 07:16
-- Versão do servidor: 10.1.28-MariaDB
-- PHP Version: 7.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chatbot`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `frase`
--

CREATE TABLE `frase` (
  `id` int(11) NOT NULL,
  `frase` varchar(300) NOT NULL,
  `id_significado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `frase`
--

INSERT INTO `frase` (`id`, `frase`, `id_significado`) VALUES
(1, 'oi', 1),
(2, 'qual e o teu nome?', 2),
(3, 'ola', 1),
(4, 'ola, como estas?', 1),
(5, 'oi, como estas?', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `respostas`
--

CREATE TABLE `respostas` (
  `id` int(11) NOT NULL,
  `id_frase` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `respostas`
--

INSERT INTO `respostas` (`id`, `id_frase`) VALUES
(3, 1),
(2, 3),
(4, 4),
(5, 5);

-- --------------------------------------------------------

--
-- Estrutura da tabela `significado`
--

CREATE TABLE `significado` (
  `id` int(11) NOT NULL,
  `significado` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `significado`
--

INSERT INTO `significado` (`id`, `significado`) VALUES
(1, 'saudar'),
(2, 'perguntar nome');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `frase`
--
ALTER TABLE `frase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_significado` (`id_significado`);

--
-- Indexes for table `respostas`
--
ALTER TABLE `respostas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_frase` (`id_frase`);

--
-- Indexes for table `significado`
--
ALTER TABLE `significado`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `frase`
--
ALTER TABLE `frase`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `respostas`
--
ALTER TABLE `respostas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `significado`
--
ALTER TABLE `significado`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `frase`
--
ALTER TABLE `frase`
  ADD CONSTRAINT `frase_ibfk_1` FOREIGN KEY (`id_significado`) REFERENCES `significado` (`id`);

--
-- Limitadores para a tabela `respostas`
--
ALTER TABLE `respostas`
  ADD CONSTRAINT `respostas_ibfk_1` FOREIGN KEY (`id_frase`) REFERENCES `frase` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
