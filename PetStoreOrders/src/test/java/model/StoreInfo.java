package model;

import org.joda.time.format.ISODateTimeFormat;

public class StoreInfo {
	private Integer id;
	private Integer petId;
	private Integer quantity;
	private String shipDate;
	private String status;
	private Boolean complete;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public StoreInfo() {
	}

	/**
	 *
	 * @param petId
	 * @param quantity
	 * @param id
	 * @param shipDate
	 * @param complete
	 * @param status
	 */
	public StoreInfo(Integer id, Integer petId, Integer quantity, String shipDate, String status, Boolean complete) {
		super();
		this.id = id;
		this.petId = petId;
		this.quantity = quantity;
		this.shipDate = ISODateTimeFormat.dateTimeParser().parseDateTime(shipDate).toString();
		this.status = status;
		this.complete = complete;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}
}
