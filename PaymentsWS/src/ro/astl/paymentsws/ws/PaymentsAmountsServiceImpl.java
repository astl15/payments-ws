package ro.astl.paymentsws.ws;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ro.astl.paymentsws.dao.PaymentsAmountsDao;
import ro.astl.paymentsws.dao.PaymentsAmountsDaoImpl;
import ro.astl.paymentsws.model.CategoryAmount;

@Path("paymentsWS/v1/amounts/")
public class PaymentsAmountsServiceImpl implements PaymentsAmountsService {
	
	private PaymentsAmountsDao dao = PaymentsAmountsDaoImpl.getInstance();
	
	@GET
	@Path("/categories/{year}/{month}/{author}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<CategoryAmount> getAmountByCategory(@PathParam("year")int year, @PathParam("month")int month, @PathParam("author")String author) {
		List<CategoryAmount> amountsPerCategories = new ArrayList<CategoryAmount>();
		int dayOfMonth = 1;
		LocalDate searchDate = LocalDate.of(year, month, dayOfMonth);
		amountsPerCategories = dao.getAmountByCategory(searchDate, author);
		return amountsPerCategories;
	}
}
