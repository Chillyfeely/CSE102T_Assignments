package Assignment02;

import java.util.ArrayList;

/**
 * @author Veysel Reşit Çaçan
 * @version 19.0.1
 * @since 13.04.2023
 **/

public class Assignment02{
    public static void main(String[] args) {
        Store s = new Store ("Migros", "www.migros.com.tr");
        Customer c = new Customer ("CSE 102");
        ClubCustomer cc = new ClubCustomer ("Club CSE 102", "05551234567");
        //s.addCustomer (c); // compiler error because c is Customer not ClubCustomer
        s.addCustomer(cc);
        Product p = new Product (123456L, "Computer", 20, 1000.00);
        FoodProduct fp = new FoodProduct (456798L, "Snickers", 100, 2, 250, true,
                true, true, false);
        CleaningProduct cp = new CleaningProduct (31654L, "Mop", 28, 99,
                false, "Multi-room");
        s.addProduct(p);
        s.addProduct(fp);

        s.addProduct(cp);

        //System.out.println(s.getProduct("shoes")); // Exception due to product not being in store

        s.getProduct("Computer").addToInventory(3);
        //System.out.println(fp.purchase (200)); // results in Exception
        c.addToCart(p, 2);
        c.addToCart(s.getProduct("snickers"), -2); // NOTE: This does not stop the program because the Exception is caught
        c.addToCart(s.getProduct("snickers"), 1);
        System.out.println("Total due - " + c.getTotalDue());
        System.out.println("\n\nReceipt:\n" + c.receipt()); 
//System.out.println("After paying:" + c.pay (1000)); // results in Exception " );
        System.out.println("After paying: "+ c.pay(2020));
        System.out.println("Total due "+c.getTotalDue());
        System.out.println("\n\nReceipt 1:\n" + c.receipt());
        //Customer c2 = s.getCustomer("05551234568"); // Exception
        cc.addToCart(s.getProduct("snickers"), 2);
        cc.addToCart(s.getProduct("snickers"), 1);
        System.out.println("\n\nReceipt 2:\n" + cc.receipt());
        Customer c3 = s.getCustomer("05551234567");
        c3.addToCart(s.getProduct("snickers"), 10);
        System.out.println("\n\nReceipt 3:\n" + cc.receipt());
        System.out.println(((ClubCustomer) c3). pay (26, false));
        c3.addToCart(s.getProduct (31654L), 3);
        System.out.println(c3.getTotalDue());
        System.out.println(c3. receipt());
        System.out.println(cc.pay (399, false));
        c3.addToCart(s.getProduct (31654L), 3);
        System.out.println(c3.getTotalDue());
        System.out.println(c3. receipt());
        System.out.println(cc.pay(399, true));
} 

}
 
class Product {
    private Long Id;
    private String Name;
    private int Quantity;
    private double Price;

    public Product(Long Id, String Name, int Quantity, double Price) throws InvalidPriceException {
        this.Id = Id;
        this.Name = Name;
        this.Quantity = Quantity;
        if (Price < 0) {
            throw new InvalidPriceException(Price);
        } else {
            this.Price = Price;
        }
        if (Amount < 0) {
            throw new InvalidAmountException(Amount);
        } else {
            this.Amount = Amount;
        }
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) throws InvalidPriceException {
        if (Price < 0) {
            throw new InvalidPriceException(Price);
        } else {
            this.Price = Price;
        }
    }

    public int remaining() {
        return Quantity;
    }

    public int addToInventory(int amount) throws InvalidAmountException {

        if (amount < 0) {
            throw new InvalidAmountException(amount);
        } else {
            Quantity += amount;
        }
        return Quantity;
    }

    public double purchase(int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        } else if (amount > Quantity) {
            throw new InvalidAmountException(amount, Quantity);
        } else {
            Quantity -= amount;
            return Quantity * Price;
        }
    }

    public String toString() {
        return "Product " + Name + " has " + Quantity + " remaining";
    }

    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product p = (Product) o;
            if (Math.abs(p.Price - Price) <= 0.001)
                return true;
        }

        return false;
    }

}

class FoodProduct extends Product {
    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;

    public FoodProduct(Long Id, String Name, int Quantity, double Price, int Calories, boolean Dairy, boolean Eggs,
            boolean Peanuts, boolean Gluten) {
        super(Id, Name, Quantity, Price);
        if (Calories < 0) {
            throw new InvalidAmountException(Calories);
        } else {
            this.Calories = Calories;
        }
        this.Dairy = Dairy;
        this.Eggs = Eggs;
        this.Peanuts = Peanuts;
        this.Gluten = Gluten;
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int Calories) throws InvalidAmountException {
        if (Calories < 0) {
            throw new InvalidAmountException(Calories);
        } else {
            this.Calories = Calories;
        }
    }

    public boolean containsDairy() {
        return Dairy;
    }

    public boolean containsEggs() {
        return Eggs;
    }

    public boolean containsPeanuts() {
        return Peanuts;
    }

    public boolean containsGluten() {
        return Gluten;
    }
}

class CleaningProduct extends Product {
    private boolean Liquid;
    private String WhereToUse;

    public CleaningProduct(Long Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse) {
        super(Id, Name, Quantity, Price);
        this.Liquid = Liquid;
        this.WhereToUse = WhereToUse;
    }

    public String getWhereToUse() {
        return WhereToUse;
    }

    public void setWhereToUse(String size) {
        this.WhereToUse = size;
    }

    public boolean isLiquid() {
        return Liquid;
    }
}

class Customer {
    private String Name;
    private double TotalDue;
    protected ArrayList<Product> customerCart;
    private String receipt;

    public Customer(String Name) {
        this.Name = Name;
        this.customerCart = new ArrayList<Product>();
        this.TotalDue = 0.0;
        this.receipt = "";
    }

    public void addToCart(Product product, int count) {
        try {
            product.purchase(count);
            this.TotalDue += product.getPrice() * count;
            this.receipt += String.format("%s - %.2f X %d = %.2f\n", product.getName(), product.getPrice(), count,
                    product.getPrice() * count);
            System.out.println("Cart: " + this.customerCart);
            System.out.println("Total Due: " + this.TotalDue);
            System.out.println("Receipt:\n" + this.receipt);
        } catch (InvalidAmountException exc) {
            System.out.println("ERROR: " + exc.getMessage());
        }
    }

    public String receipt() {
        StringBuilder sb = new StringBuilder();
        for (Product product : this.customerCart) {
            sb.append(String.format("%s - %.2f X %d = %.2f\n", product.getName(), product.getPrice(),
                    this.getCount(product), product.getPrice() * this.getCount(product)));
        }
        sb.append(String.format("Total Due - %.2f", this.TotalDue));
        return sb.toString();
    }

    public int getCount(Product product) {
        int count = 0;
        for (Product p : this.customerCart) {
            if (p.equals(product)) {
                count++;
            }
        }
        return count;
    }

    public double pay(double amount) throws InsufficientFundsException {
        if (amount >= TotalDue) {
            System.out.println("Thank you");
            customerCart.clear();
            return amount - TotalDue;
        } else {
            throw new InsufficientFundsException(TotalDue, amount);
        }
    }

    public double getTotalDue() {
        return TotalDue;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String toString() {
        return Name;
    }
}

class ClubCustomer extends Customer {
    private String Phone;
    private int Points;

    public ClubCustomer(String Name, String Phone, int Points) {
        super(Name);
        this.Phone = Phone;
        this.Points = Points;
    }
    public ClubCustomer(String Name, String Phone){
        super(Name);
        this.Phone = Phone;
    }

    public double pay(double amount, boolean usePoints) throws InsufficientFundsException {
        double newTotal;
        int holdPoints = 0;
        if (usePoints) {
            newTotal = getTotalDue() - (0.01 * Points);
            if (newTotal <= 0) {
                Points = (int) (Points - (newTotal * 100));
                System.out.println("Thank you");
                customerCart.clear();
                return amount;
            } else if (amount + (0.01 * Points) >= newTotal) {
                Points = 0;
                System.out.println("Thank you");
                customerCart.clear();
                holdPoints = (int) (amount - (0.01 * Points));
                return amount + (0.01 * Points) - newTotal;
            } else if (amount + (0.01 * Points) < newTotal) {
                throw new InsufficientFundsException(newTotal, amount);
            }

        } else if (usePoints == false && amount >= getTotalDue()) {
            System.out.println("Thank you");
            customerCart.clear();
            holdPoints = (int) (getTotalDue() - amount);
            return getTotalDue() - amount;
        } else if (usePoints == false && amount < getTotalDue()) {
            throw new InsufficientFundsException(getTotalDue(), amount);
        } else {
            throw new InsufficientFundsException(getTotalDue(), amount);
        }
        this.Points += holdPoints;
        holdPoints = 0;
       
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public int getPoints() {
        return Points;
    }

    public void addPoints(int points) {
        if (points > 0)
            this.Points += points;
    }

    public String toString() {
        return getName() + " has " + Points + " points";
    }
}

class Store {
    private String Name;
    private String Website;
    private ArrayList<Product> products;
    private ArrayList<ClubCustomer> customers;

    Store(String name, String website) {
        this.Name = name;
        this.Website = website;
        ArrayList<Product> products = new ArrayList<Product>();
        this.products = products;
        ArrayList<ClubCustomer> customers = new ArrayList<ClubCustomer>();
        this.customers = customers;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public int getInventorySize() {
        return products.size();
    }

    public void addProduct(Product product, int index) {
        products.add(product);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product getProduct(long Id) throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getId() == Id) {
                return product;
            }
        }
        throw new ProductNotFoundException(Id);
    }

    public Product getProduct(String name) throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getName() == name) {
                return product;
            }
        }
        throw new ProductNotFoundException(name);
    }

    public void addCustomer(ClubCustomer customer) {
        customers.add(customer);
    }

    public ClubCustomer getCustomer(String phone) {
        for (ClubCustomer customer : customers) {
            if (customer.getPhone() == phone) {
                return customer;
            }
        }
        throw new CustomerNotFoundException(phone);
    }

    public void removeProduct(Long ID) throws ProductNotFoundException {
        boolean removed = false;
        for (Product product : products) {
            if (product.getId() == ID) {
                products.remove(product);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new ProductNotFoundException(ID);
        }
    }

    public void removeProduct(String name) throws ProductNotFoundException {
        boolean removed = false;
        for (Product product : products) {
            if (product.getName() == name) {
                products.remove(product);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new ProductNotFoundException(Name);
        }
    }

    public void removeCustomer(String phone) throws CustomerNotFoundException {
        boolean removed = false;
        for (ClubCustomer customer : customers) {
            if (customer.getPhone() == phone) {
                customers.remove(customer);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new CustomerNotFoundException(phone);
        }
    }
}

class CustomerNotFoundException extends IllegalArgumentException {
    private String phone;

    CustomerNotFoundException(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return "CustomerNotFoundException: " + phone;
    }
}

class InsufficientFundsException extends RuntimeException {
    private double total;
    private double payment;

    InsufficientFundsException(double total, double payment) {
        this.total = total;
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "InsufficientFundsException: " + total + " due, but only " + payment + " given";
    }
}

class InvalidAmountException extends RuntimeException {
    private int amount;
    private int quantity;

    InvalidAmountException(int amount) {
        this.amount = amount;
    }

    InvalidAmountException(int amount, int quantity) {
        this.amount = amount;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        if (quantity == 0) {
            return "InvalidAmountException: " + amount;
        } else {
            return "InvalidAmountException: " + amount + " was requested, but only " + quantity + " remaining";
        }
    }
}

class InvalidPriceException extends RuntimeException {
    private double price;

    InvalidPriceException(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvalidPriceException: " + price;
    }
}

class ProductNotFoundException extends IllegalArgumentException {
    private Long ID;
    private String name;

    ProductNotFoundException(Long ID) {
        this.ID = ID;
        this.name = null;
    }

    ProductNotFoundException(String name) {
        this.name = name;
        this.ID = 123L;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "ProductNotFoundException: ID - " + ID;
        } else {
            return "ProductNotFoundException: Name - " + name;
        }
    }
}
