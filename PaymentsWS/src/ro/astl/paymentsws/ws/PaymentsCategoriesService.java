package ro.astl.paymentsws.ws;

import java.util.List;

import ro.astl.paymentsws.model.Category;

public interface PaymentsCategoriesService {
	public List<Category> getCategories();
}
