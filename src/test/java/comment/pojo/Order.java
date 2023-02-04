package comment.pojo;

import java.util.Date;

public class Order {
    private String orderID;

    private String userName;
    private String commodityID;
    private Double totalPrices;
    private Integer count;
    private Date orderDate = new Date();

    public String getOrderID() {
        return orderID;
    }

    public Double getTotalPrices() {
        return totalPrices;
    }

    public Order setTotalPrices(Double totalPrices) {
        this.totalPrices = totalPrices;
        return this;
    }

    public Order setOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Order setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getCommodityID() {
        return commodityID;
    }

    public Order setCommodityID(String commodityID) {
        this.commodityID = commodityID;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public Order setCount(Integer count) {
        this.count = count;
        return this;
    }
}
