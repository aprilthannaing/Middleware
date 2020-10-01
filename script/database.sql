CREATE TABLE `User` (  
  id bigint(20) primary key not null auto_increment,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phoneNo` varchar(50) DEFAULT NULL, 
  `paymentdescription` varchar(500) DEFAULT NULL 
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  
  CREATE TABLE `visa` (
 id bigint(20) primary key not null auto_increment,
  `visaTransactionId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `interactionOperation` varchar(30) DEFAULT NULL,
  `merchantId` varchar(255) DEFAULT NULL,
  `merchantCategoryCode` varchar(255) DEFAULT NULL,
  `orderId` bigint(20) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creationTime` varchar(255) DEFAULT NULL,
  `customerName` varchar(255) DEFAULT NULL,
  `customerOrderDate`varchar(255) DEFAULT NULL,
  `deviceType` varchar(255) DEFAULT NULL,
  `ipAddress` varchar(55) DEFAULT NULL,
  `result` varchar(50) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `expiryMonth` varchar(50) DEFAULT NULL,
  `expiryYear` varchar(50) DEFAULT NULL,
  `fundingMethod` varchar(50) DEFAULT NULL,
  `issuer` varchar(50) DEFAULT NULL,
  `nameOnCard` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `scheme` varchar(255) DEFAULT NULL,
  `storedOnFile` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
 `totalAuthorizedAmount` varchar(255) DEFAULT NULL,
 `totalCapturedAmount` varchar(255) DEFAULT NULL,
 `totalRefundedAmount` varchar(255) DEFAULT NULL,
  KEY `FK_s9y70mi0ken20v5xk44h573hd` (`visaTransactionId`),
  CONSTRAINT `FK_s9y70mi0ken20v5xk44h573hd` FOREIGN KEY (`visaTransactionId`) REFERENCES `VisaTransaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `VisaTransaction` (  
  id bigint(20) primary key not null auto_increment,
  `secureId` varchar(50) DEFAULT NULL,
  `authenticationToken` varchar(50) DEFAULT NULL,
  `xid` varchar(25) DEFAULT NULL,  
  `gatewayEntryPoint` varchar(50) DEFAULT NULL,
  `acquirerMessage` varchar(255) DEFAULT NULL,
  `gatewayCode` varchar(255) DEFAULT NULL,
  `batch` varchar(25) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `authorizationCode` varchar(55) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `transactionId` varchar(50) DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `receipt` varchar(50) DEFAULT NULL,
  `source` varchar(50) DEFAULT NULL,
  `taxAmount` varchar(50) DEFAULT NULL,
  `terminal` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `version` int DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;