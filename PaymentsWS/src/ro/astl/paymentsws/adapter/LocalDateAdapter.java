package ro.astl.paymentsws.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public String marshal(LocalDate localDate) throws Exception {
		return DateTimeFormatter.ISO_DATE.format(localDate);
	}

	@Override
	public LocalDate unmarshal(String dateString) throws Exception {
		return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
	}

}
