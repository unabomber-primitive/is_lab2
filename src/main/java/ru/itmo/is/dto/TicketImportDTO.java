package ru.itmo.is.dto;

public class TicketImportDTO {
    private String name;
    private CoordinatesDTO coordinates;
    private PersonDTO person;
    private Integer eventId;
    private Long venueId;
    private Double price;
    private String type;
    private Long discount;
    private long number;

    public static class CoordinatesDTO {
        private float x;
        private Double y;

        public float getX() { return x; }
        public void setX(float x) { this.x = x; }
        public Double getY() { return y; }
        public void setY(Double y) { this.y = y; }
    }

    public static class PersonDTO {
        private String eyeColor;
        private String hairColor;
        private LocationDTO location;
        private double weight;
        private String passportID;
        private String nationality;

        public String getEyeColor() { return eyeColor; }
        public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }
        public String getHairColor() { return hairColor; }
        public void setHairColor(String hairColor) { this.hairColor = hairColor; }
        public LocationDTO getLocation() { return location; }
        public void setLocation(LocationDTO location) { this.location = location; }
        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }
        public String getPassportID() { return passportID; }
        public void setPassportID(String passportID) { this.passportID = passportID; }
        public String getNationality() { return nationality; }
        public void setNationality(String nationality) { this.nationality = nationality; }
    }

    public static class LocationDTO {
        private Float x;
        private Long y;
        private String name;

        public Float getX() { return x; }
        public void setX(Float x) { this.x = x; }
        public Long getY() { return y; }
        public void setY(Long y) { this.y = y; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public CoordinatesDTO getCoordinates() { return coordinates; }
    public void setCoordinates(CoordinatesDTO coordinates) { this.coordinates = coordinates; }
    public PersonDTO getPerson() { return person; }
    public void setPerson(PersonDTO person) { this.person = person; }
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getDiscount() { return discount; }
    public void setDiscount(Long discount) { this.discount = discount; }
    public long getNumber() { return number; }
    public void setNumber(long number) { this.number = number; }
}
