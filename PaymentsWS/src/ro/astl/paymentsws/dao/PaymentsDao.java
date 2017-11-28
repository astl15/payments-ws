package ro.astl.paymentsws.dao;

import java.time.LocalDate;
import java.util.List;

import ro.astl.paymentsws.model.Payment;

public interface PaymentsDao {
	public boolean addPayment(Payment payment);
	public List<Payment> getLastPaymentsByAuthor(String author);
	public List<Payment> getPaymentsByDateAndAuthor(LocalDate date, String author);
}
