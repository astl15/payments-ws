package ro.astl.paymentsws.dao;

import java.util.List;

import ro.astl.paymentsws.model.Category;

public interface PaymentsCategoriesDao {
	public List<Category> getCategories();
	public boolean addCategory(String label);
}
