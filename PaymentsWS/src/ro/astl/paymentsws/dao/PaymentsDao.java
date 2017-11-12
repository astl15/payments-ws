package ro.astl.paymentsws.dao;

import java.util.List;

import ro.astl.paymentsws.model.Payment;

public interface PaymentsDao {
	
	public boolean addPayment(Payment payment);
	public List<Payment> getLastPaymentsByAuthor(String author);

}
