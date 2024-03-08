package com.parserlabs.phr.schedulers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.parserlabs.phr.model.PhrTransactionEntityLite;
import com.parserlabs.phr.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransactionScheduler {

	@Autowired
	private TransactionService transactionService;

	@Value("${enable.overdue.transaction.scheduler:false}")
	private boolean enableOverdueTransaction;

	@Scheduled(cron = "0 0 23 * * *")
	@Transactional
	public void deleteOverdueTransaction() {
		if (enableOverdueTransaction) {
			log.info("Scheduler started for deleting overdue transaction.");

			// total overdue transactions
			List<PhrTransactionEntityLite> overdueTransactions = transactionService.findOverdueTransaction();

			List<Long> idList = overdueTransactions.stream().map(overdueTransaction -> overdueTransaction.getId())
					.collect(Collectors.toList());

			log.info("Total overdue transactions are {}", idList.size());

			if (idList.size() > 0) {
				transactionService.deleteTransactionAllByIdInBatch(idList);
				// delete overdue transactions
				log.info("Deleted all overdue transaction.");
			}
		}
	}
}
