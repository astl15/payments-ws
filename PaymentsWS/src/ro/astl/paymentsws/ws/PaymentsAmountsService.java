package ro.astl.paymentsws.ws;

import java.util.List;

import ro.astl.paymentsws.model.CategoryAmount;

public interface PaymentsAmountsService {
	public List<CategoryAmount> getAmountByCategory(int year, int month, String author);
}
