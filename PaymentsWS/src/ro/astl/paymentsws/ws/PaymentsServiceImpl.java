package ro.astl.paymentsws.ws;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.astl.paymentsws.dao.PaymentsCategoriesDao;
import ro.astl.paymentsws.dao.PaymentsCategoriesDaoImpl;
import ro.astl.paymentsws.dao.PaymentsDao;
import ro.astl.paymentsws.dao.PaymentsDaoImpl;
import ro.astl.paymentsws.model.Category;
import ro.astl.paymentsws.model.CategoryAmount;
import ro.astl.paymentsws.model.Payment;
import ro.astl.paymentsws.model.PaymentDTO;

@Path("paymentsWS/v1/payments/")
public class PaymentsServiceImpl implements PaymentsService {
	
	private PaymentsDao dao = PaymentsDaoImpl.getInstance();
	private PaymentsCategoriesDao daoCategories= PaymentsCategoriesDaoImpl.getInstance();
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response addPayment(PaymentDTO paymentDTO) {
		Payment payment = new Payment();
		Category category = new Category();
		String categoryID = paymentDTO.getCategory();
		category.setId((int)Float.parseFloat(categoryID));
		List<Category> categories = daoCategories.getCategories();
		for(Category dbCategory:categories) {
			if(dbCategory.getId() == category.getId()) {
				category.setLabel(dbCategory.getLabel());
			}
		}
		payment.setAuthor(paymentDTO.getAuthor());
		payment.setDescription(paymentDTO.getDescription());
		payment.setAmount(paymentDTO.getAmount());
		payment.setCategory(category);
		payment.setDate(paymentDTO.getDate());
		boolean isExecuted = dao.addPayment(payment);
		if(isExecuted) {
			return Response.status(Response.Status.CREATED).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	@GET
	@Path("/last/{author}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<Payment> getLastPaymentsByAuthor(@PathParam("author")String author) {
		List<Payment> payments = new ArrayList<Payment>();
		payments = dao.getLastPaymentsByAuthor(author);
		return payments;
	}

	@GET
	@Path("/{year}/{month}/{author}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<Payment> getPaymentsByMonthAndAuthor(@PathParam("year")int year, @PathParam("month")int month, @PathParam("author")String author) {
		List<Payment> payments = new ArrayList<Payment>();
		int dayOfMonth = 1;
		LocalDate searchDate = LocalDate.of(year, month, dayOfMonth);
		payments = dao.getPaymentsByDateAndAuthor(searchDate, author);
		return payments;
	}
}
