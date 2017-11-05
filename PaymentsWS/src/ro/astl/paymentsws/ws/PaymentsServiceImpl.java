package ro.astl.paymentsws.ws;

import java.time.LocalDate;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.astl.paymentsws.dao.PaymentsDao;
import ro.astl.paymentsws.dao.PaymentsDaoImpl;
import ro.astl.paymentsws.model.Category;
import ro.astl.paymentsws.model.Payment;

@Path("paymentsWS/v1/payments/")
public class PaymentsServiceImpl implements PaymentsService {
	
	private PaymentsDao dao = PaymentsDaoImpl.getInstance();
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response addPayment(Payment payment) {
		boolean isExecuted = dao.addPayment(payment);
		if(isExecuted) {
			return Response.status(Response.Status.CREATED).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

}
