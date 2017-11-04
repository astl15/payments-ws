package ro.astl.paymentsws.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ro.astl.paymentsws.model.Category;

@Path("paymentsWS/v1/categories/")
public class PaymentsCategoriesServiceImpl implements PaymentsCategoriesService {
	
	@GET
	@Path("/getCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		Category tempCategory = new Category();
		tempCategory.setId(1);
		tempCategory.setLabel("Bauturi");
		categories.add(tempCategory);
		return categories;
	}
}
