package ro.astl.paymentsws.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Category")
public class Category {
	
	private int id;
	private String label;
	private String locale;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	

}
