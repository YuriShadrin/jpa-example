package dev.example.jpademo.persistent.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(nullable = true)
	private String siteUrl;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_id")
	private List<Project> projects;

	protected Customer() {
	}

	public Customer(String title, String address) {
		this(title, address, null);
	}

	public Customer(String name, String address, String siteUrl) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(address);

		this.name = name;
		this.address = address;
		this.siteUrl = siteUrl;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	@Override
	public String toString() {
		return String.format("Customer [id=%s, name=%s, address=%s, siteUrl=%s]", id, name, address, siteUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address, siteUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(address, other.address) && Objects.equals(siteUrl, other.siteUrl);
	}
}
