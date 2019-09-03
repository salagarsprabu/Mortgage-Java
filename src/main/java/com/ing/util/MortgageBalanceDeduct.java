package com.ing.util;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ing.entity.Account;
import com.ing.entity.Mortagage;
import com.ing.entity.Transaction;
import com.ing.exception.MortagageManagementException;
import com.ing.repository.AccountRepository;
import com.ing.repository.MortagageRepository;
import com.ing.repository.TransactionRepository;

@Component
public class MortgageBalanceDeduct {

	@Autowired
	MortagageRepository mortagageRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	public void deductBalance()
	{
		List<Mortagage> mortagageList = mortagageRepository.findAll();
		
		for(int i =0;i<mortagageList.size();i++)
		{
			String customerIdMortagage = mortagageList.get(i).getCustomerId();
			
			Account accountData = accountRepository.findBycustomerId(customerIdMortagage);
			
			if(accountData.getAccountNumber() != null)
			{
				String accountNumber = accountData.getAccountNumber();
				String customerId = accountData.getCustomerId();
				Account account = new Account();
				account.setAccountNumber(accountData.getAccountNumber());
				Double accountDegrade = accountData.getBalance()-200;
				account.setBalance(accountDegrade);
				accountRepository.save(account);
				
				Transaction transactionData = new Transaction();
				transactionData.setAccountNumber(accountNumber);
				transactionData.setAccountType("transaction");
				transactionData.setDescription("payment towards to mortgage loan");
				transactionData.setTransactionAmount(200);
				transactionData.setTransactionType("credit");
				transactionData.setTransactionDate(LocalDateTime.now());
				transactionRepository.save(transactionData);
				
				Mortagage mort = mortagageRepository.findBycustomerId(customerId);
				Mortagage mortagageData = new Mortagage();
				mortagageData.setMortagageId(mort.getMortagageId());
				mortagageData.setCustomerId(mort.getCustomerId());
				Double mortgageMinus = mort.getMortagageBalance() + 200;
				mortagageData.setMortagageBalance(mortgageMinus);
				mortagageRepository.save(mortagageData);
			}
			else
			{
				throw new MortagageManagementException("exception in account number");
			}
		}
	}
	
}