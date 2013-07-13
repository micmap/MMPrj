package data;

/**
 * Holds all data about an apartment listed on ZVZ.
 * @author Inbal
 *
 */
public class ZvzListItem {

	private String origItemId;
	private String entDate;
	private String propType;
	private String numOfRooms;
	private String area;
	private String city;
	private String address;
	private String size;
	private String floor;
	private String payments;
	private String parking;
	private String furniture;
	private String ac;
	private String balcony;
	private String roomates;
	private String soragim;
	private String propState;
	private String elevator;
	private String forDisabled;
	private String neighborhood;
	private String price;
	private String sellerName;
	private String sellerPhone;
	private String furnitureNotes;
	private String notes;
	private byte[][] imagesBlob;
	
	public ZvzListItem() {
	}
	
	@Override
	public String toString() {
		
		StringBuilder bldr = new StringBuilder();
		bldr.append("origItemId:" + origItemId);
		bldr.append("\n");
		bldr.append("entDate:" + entDate);
		bldr.append("\n");
		bldr.append("propType:" + propType);
		bldr.append("\n");
		bldr.append("numOfRooms:" + numOfRooms);
		bldr.append("\n");
		bldr.append("area:" + area);
		bldr.append("\n");
		bldr.append("city:" + city);
		bldr.append("\n");
		bldr.append("address:" + address);
		bldr.append("\n");
		bldr.append("size:" + size);
		bldr.append("\n");
		bldr.append("floor:" + floor);
		bldr.append("\n");
		bldr.append("payments:" + payments);
		bldr.append("\n");
		bldr.append("parking:" + parking);
		bldr.append("\n");
		bldr.append("furniture:" + furniture);
		bldr.append("\n");
		bldr.append("ac:" + ac);
		bldr.append("\n");
		bldr.append("balcony:" + balcony);
		bldr.append("\n");
		bldr.append("roomates:" + roomates);
		bldr.append("\n");
		bldr.append("soragim:" + soragim);
		bldr.append("\n");
		bldr.append("propState:" + propState);
		bldr.append("\n");
		bldr.append("elevator:" + elevator);
		bldr.append("\n");
		bldr.append("forDisabled:" + forDisabled);
		bldr.append("\n");
		bldr.append("neighborhood:" + neighborhood);
		bldr.append("\n");
		bldr.append("price:" + price);
		bldr.append("\n");
		bldr.append("sellerName:" + sellerName);
		bldr.append("\n");
		bldr.append("sellerPhone:" + sellerPhone);
		bldr.append("\n");
		bldr.append("furnitureNotes:" + furnitureNotes);
		bldr.append("\n");
		bldr.append("notes:" + notes);
		bldr.append("\n");
		int numOfImages = imagesBlob != null ? imagesBlob.length : 0;
		bldr.append("images:" + numOfImages);
		return bldr.toString();
	} 
	
	////////// getters and setters //////////
	
	/** 
	 * Entrance date. can be either an actual date or just text
	 * (example: flexible)
	 * 
	 */
	public String getEntDate() {
		return entDate;
	}

	public void setEntDate(String entDate) {
		this.entDate = entDate;
	}

	/**
	 * Property type of the item - apartment, house, etc
	 * @return
	 */
	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	/**
	 * Number of rooms
	 * @return
	 */
	public String getNumOfRooms() {
		return numOfRooms;
	}

	public void setNumOfRooms(String numOfRooms) {
		this.numOfRooms = numOfRooms;
	}

	/**
	 * the area the item is located in
	 * @return
	 */
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * the city (or Moshav, Yeshuv, etc) of the item
	 * @return
	 */
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * the item's address
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * the size of the item (usually in sqaure meter)
	 * @return
	 */
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * item's floor number
	 * @return
	 */
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * number of payments
	 * @return
	 */
	public String getPayments() {
		return payments;
	}

	public void setPayments(String payments) {
		this.payments = payments;
	}

	/**
	 * is there an attached parking
	 * @return
	 */
	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	/**
	 * does the item come with furniture
	 * @return
	 */
	public String getFurniture() {
		return furniture;
	}

	public void setFurniture(String furniture) {
		this.furniture = furniture;
	}

	/**
	 * does the item come with air conditioning
	 * @return
	 */
	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	/**
	 * does the item have a balcony
	 * @return
	 */
	public String getBalcony() {
		return balcony;
	}

	public void setBalcony(String balcony) {
		this.balcony = balcony;
	}

	/**
	 * is the item suitable for roomates
	 * @return
	 */
	public String getRoomates() {
		return roomates;
	}

	public void setRoomates(String roomates) {
		this.roomates = roomates;
	}

	/**
	 * does the item have soragim
	 * @return
	 */
	public String getSoragim() {
		return soragim;
	}

	public void setSoragim(String soragim) {
		this.soragim = soragim;
	}

	/**
	 * what's the state of the item
	 * @return
	 */
	public String getPropState() {
		return propState;
	}

	public void setPropState(String propState) {
		this.propState = propState;
	}

	/**
	 * does the item have an elevator
	 * @return
	 */
	public String getElevator() {
		return elevator;
	}

	public void setElevator(String elevator) {
		this.elevator = elevator;
	}

	/**
	 * is the item accessible for the disabled
	 * @return
	 */
	public String getForDisabled() {
		return forDisabled;
	}

	public void setForDisabled(String forDisabled) {
		this.forDisabled = forDisabled;
	}

	/**
	 * the neighborhood the item is in
	 * @return
	 */
	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	/**
	 * the item's price
	 * @return
	 */
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * seller's name
	 * @return
	 */
	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	/**
	 * seller's phone
	 * @return
	 */
	public String getSellerPhone() {
		return sellerPhone;
	}

	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}

	/**
	 * note about the item's furniture
	 * @return
	 */
	public String getFurnitureNotes() {
		return furnitureNotes;
	}

	public void setFurnitureNotes(String furnitureNotes) {
		this.furnitureNotes = furnitureNotes;
	}

	/**
	 * general notes about the item 
	 * @return
	 */
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * the zvz id of the item
	 * @return
	 */
	public String getOrigItemId() {
		return origItemId;
	}

	public void setOrigItemId(String origItemId) {
		this.origItemId = origItemId;
	}

	/**
	 * blob of all item's images. readable as png.
	 * @return
	 */
	public byte[][] getImagesBlob() {
		return imagesBlob;
	}

	public void setImagesBlob(byte[][] imagesBlob) {
		this.imagesBlob = imagesBlob;
	}

	////////// getters and setters //////////

}
