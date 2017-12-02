package ro.astl.paymentsws.ws;

import java.util.List;

import ro.astl.paymentsws.model.CategoryAmount;
import ro.astl.paymentsws.model.Payment;

public interface PaymentsAmountsService {
	public List<CategoryAmount> getAmountByCategory(int year, int month, String author);
	public List<Payment> getAmountPerDate(int year, int month, String author);
}
