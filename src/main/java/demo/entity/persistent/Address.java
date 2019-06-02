package demo.entity.persistent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address extends HibernateEntity {

	@Column
	private String country;

	@Column
	private String county;

	@Column
	private String town;

	@Column
	private String postcode;

	@Column
	private String line1;

	@Column
	private String line2;
	
	@Column
	private String line3;
	
	@Column
	private String line4;

	@Column
	private String line5;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;

	@SuppressWarnings("unused")
	public Address() {
	}

	public Address(String country, String county, String town, String postcode, String line1) {
		this.country = country;
		this.county = county;
		this.town = town;
		this.postcode = postcode;
		this.line1 = line1;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getLine4() {
		return line4;
	}

	public void setLine4(String line4) {
		this.line4 = line4;
	}

	public String getLine5() {
		return line5;
	}

	public void setLine5(String line5) {
		this.line5 = line5;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		return Objects.hash(line1, line2, postcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Address))
			return false;
		Address other = (Address) obj;
		return Objects.equals(line1, other.line1) && Objects.equals(line2, other.line2)
				&& Objects.equals(postcode, other.postcode);
	}

	@Override
	public String toString() {
		List<String> nonNullList = new ArrayList<>();
		addIfNotNull(nonNullList, line1);
		addIfNotNull(nonNullList, line2);
		addIfNotNull(nonNullList, line3);
		addIfNotNull(nonNullList, line4);
		addIfNotNull(nonNullList, line5);
		addIfNotNull(nonNullList, town);
		addIfNotNull(nonNullList, county);
		addIfNotNull(nonNullList, country);
		addIfNotNull(nonNullList, postcode);
		return String.join(",", nonNullList);
	}

	private void addIfNotNull(List<String> list, String s) {
		if (s!=null) {
			list.add(s);
		}
	}
}
