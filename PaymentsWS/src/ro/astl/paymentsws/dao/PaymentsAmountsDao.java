package ro.astl.paymentsws.dao;

import java.time.LocalDate;
import java.util.List;

import ro.astl.paymentsws.model.CategoryAmount;

public interface PaymentsAmountsDao {
	public List<CategoryAmount> getAmountByCategory(LocalDate date, String author); 
}
