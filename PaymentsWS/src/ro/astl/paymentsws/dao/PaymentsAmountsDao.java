package ro.astl.paymentsws.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ro.astl.paymentsws.model.CategoryAmount;
import ro.astl.paymentsws.model.Payment;

public interface PaymentsAmountsDao {
	public List<CategoryAmount> getAmountByCategory(LocalDate date, String author);
	public List<Payment> getAmountPerDate(LocalDate date, String author);
	public Map<String, Integer> getMonthlySummary(LocalDate date, String author);
}
