package model;
// Generated Feb 16, 2025, 11:35:09 PM by Hibernate Tools 6.5.1.Final

/**
 * Phonenumber generated by hbm2java
 */
public class Phonenumber implements java.io.Serializable {

	private Integer phonenumberId;
	private Customer customer;
	private String number;

	public Phonenumber() {
	}

	public Phonenumber(Customer customer, String number) {
		this.customer = customer;
		this.number = number;
	}

	public Integer getPhonenumberId() {
		return this.phonenumberId;
	}

	public void setPhonenumberId(Integer phonenumberId) {
		this.phonenumberId = phonenumberId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
