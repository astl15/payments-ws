package ro.astl.paymentsws.ws;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import ro.astl.paymentsws.model.CategoryAmount;
import ro.astl.paymentsws.model.Payment;
import ro.astl.paymentsws.model.PaymentDTO;

public interface PaymentsService {
	public Response addPayment(PaymentDTO payment);
	public List<Payment> getLastPaymentsByAuthor(String author);
	public List<Payment> getPaymentsByMonthAndAuthor(int year, int month, String author);
}
