package ro.astl.paymentsws.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.astl.paymentsws.dao.PaymentsCategoriesDao;
import ro.astl.paymentsws.dao.PaymentsCategoriesDaoImpl;
import ro.astl.paymentsws.model.Category;

@Path("paymentsWS/v1/categories/")
public class PaymentsCategoriesServiceImpl implements PaymentsCategoriesService {
	
	private PaymentsCategoriesDao dao = PaymentsCategoriesDaoImpl.getInstance();
	
	@GET
	@Path("/getCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		categories = dao.getCategories();
		return categories;
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Category category) {
		String label = category.getLabel();
		boolean isExecuted = dao.addCategory(label);
		if(isExecuted) {
			return Response.status(Response.Status.CREATED).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
}
