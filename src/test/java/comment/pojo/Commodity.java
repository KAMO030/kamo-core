package comment.pojo;

public class Commodity {
    private String commodityId;
    private Float price;

    public String getCommodityId() {
        return commodityId;
    }

    public Commodity setCommodityId(String commodityId) {
        this.commodityId = commodityId;
        return this;
    }

    public Float getPrice() {
        return price;
    }

    public Commodity setPrice(Float price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "commodityId='" + commodityId + '\'' +
                ", price=" + price +
                '}';
    }
}
