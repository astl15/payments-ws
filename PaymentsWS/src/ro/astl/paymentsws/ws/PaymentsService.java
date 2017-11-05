package ro.astl.paymentsws.ws;

import java.time.LocalDate;

import javax.ws.rs.core.Response;

import ro.astl.paymentsws.model.Payment;

public interface PaymentsService {
	public Response addPayment(Payment payment);
}
