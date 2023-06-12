
import java.util.HashMap;
import java.util.Map;

/**
 * @author Veysel Reşit Çaçan
 * @version 19.0.1
 * @since 28.05.2023
 **/



class Product {
    private Long Id;
    private String Name;
    private double Price;

    public Product(Long Id, String Name, double Price) throws InvalidPriceException {
        this.Id = Id;
        this.Name = Name;
        if (Price < 0) {
            throw new InvalidPriceException(Price);
        } else {
            this.Price = Price;
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

    public String toString() {
        return Id +" - "+ Name + " @ " + Price ;
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

    public FoodProduct(Long Id, String Name, double Price, int Calories, boolean Dairy, boolean Eggs,
            boolean Peanuts, boolean Gluten) throws InvalidAmountException {
        super(Id, Name, Price);
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

    public CleaningProduct(Long Id, String Name, double Price, boolean Liquid, String WhereToUse) {
        super(Id, Name,  Price);
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
    protected HashMap<Store ,HashMap<Product, Integer>> customerCart;

    public Customer(String Name) {
        this.Name = Name;
        this.customerCart = new HashMap<>();
        this.TotalDue = 0.0;
    }

    public void addToCart(Store store, Product product, int count) {
        if (!customerCart.containsKey(store)) {
            customerCart.put(store, new HashMap<>());
        }
        if (store.isProductExist(product)) {
            if (store.getProductCount(product) >= count) {
                customerCart.get(store).put(product, count);
            } else {
                System.out.println("ERROR: Product is not enough in the store");
            }
        } else {
            System.out.println("ERROR: Product does not exist");
        }
    }

    public String receipt(Store store) throws StoreNotFoundException {
        if (!customerCart.containsKey(store)) {
            throw new StoreNotFoundException("Store not found: " + store.getName());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Store: ").append(store.getName()).append("\n");

        HashMap<Product, Integer> cart = customerCart.get(store);

        for(Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int count = entry.getValue();

            double productTotal = product.getPrice() * count;
            TotalDue += productTotal;

            sb.append(product.getId()).append(" - ").append(product.getName());
            sb.append(" @ ").append(product.getPrice()).append(" X ").append(count);
            sb.append(" = ").append(productTotal).append("\n");
        }

        sb.append("Total Due - ").append(TotalDue);

        return sb.toString();
    }

    public int getPoints(Store store) throws StoreNotFoundException {
        if (!customerCart.containsKey(store)) {
            throw new StoreNotFoundException("Store not found: " + store.getName());
        } else {
            return store.getCustomerPoints(this);
        }
    }

    public double pay(Store store, double amount, boolean usePoints)throws StoreNotFoundException, InsufficientFundsException {
        double tempTotalDue = TotalDue;
        
        if (usePoints){
            HashMap<Customer, Integer> personToPoints = store.getPrivateMap();
            if (store.getCustomerPoints(this) > 0){
                int possibleUsedPoints = (int)tempTotalDue*100;
                tempTotalDue -= store.getCustomerPoints(this)*0.01;
                if(tempTotalDue <= 0){
                    tempTotalDue = 0;
                    store.addCustomerPoints(this, -possibleUsedPoints);
                    return amount;
                } else if (tempTotalDue > 0){
                    if (amount >= tempTotalDue){
                        store.setCustomerPoints(this, 0);
                        store.addCustomerPoints(this, (int)tempTotalDue);
                        return amount - tempTotalDue;
                    }else {
                        throw new InsufficientFundsException(tempTotalDue, amount);
                    }
                }     
            }else if(!personToPoints.containsKey(this)){
                store.addCustomer(this);
                if (store.getCustomerPoints(this) > 0){
                    int possibleUsedPoints = (int)tempTotalDue*100;
                    tempTotalDue -= store.getCustomerPoints(this)*0.01;
                    if(tempTotalDue <= 0){
                        tempTotalDue = 0;
                        store.addCustomerPoints(this, -possibleUsedPoints);
                        return amount;
                    } else if (tempTotalDue > 0){
                        if (amount >= tempTotalDue){
                            store.setCustomerPoints(this, 0);
                            store.addCustomerPoints(this, (int)tempTotalDue);
                            return amount - tempTotalDue;
                        }else {
                            throw new InsufficientFundsException(tempTotalDue, amount);
                        }
                    }
                }
            }
        }else if (!usePoints){
            HashMap<Customer, Integer> personToPoints = store.getPrivateMap();
            if(!personToPoints.containsKey(this)){
                store.addCustomer(this);
                if (amount > tempTotalDue){
                    store.addCustomerPoints(this, (int)tempTotalDue);
                    return amount - tempTotalDue;
                }else if (amount < tempTotalDue){
                    throw new InsufficientFundsException(tempTotalDue, amount);
                }
            } else if (personToPoints.containsKey(this)){
                if (amount > tempTotalDue){
                    store.addCustomerPoints(this, (int)tempTotalDue);
                    return amount - tempTotalDue;
                }else if (amount < tempTotalDue){
                    throw new InsufficientFundsException(tempTotalDue, amount);
                }
            }    
        }else if (customerCart.containsKey(store)){
            throw new StoreNotFoundException("Store not found: " + store.getName());
        }
        return amount;
    }

    public double getTotalDue(Store store) throws StoreNotFoundException {
        if (!customerCart.containsKey(store)) {
            throw new StoreNotFoundException("Store not found: " + store.getName());
        } else {
            return TotalDue;
        }
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

class Store {
    private String Name;
    private String Website;
    private HashMap<Product, Integer> productToQuantity;
    private HashMap<Customer, Integer> customerToPoints;

    Store(String name, String website) {
        this.Name = name;
        this.Website = website;
        this.productToQuantity = new HashMap<>();
        this.customerToPoints = new HashMap<>();
    }

    public HashMap<Customer, Integer> getPrivateMap() { //I know its a safety hazard xd
        return customerToPoints;
    }

    public boolean isProductExist(Product product) {
        return productToQuantity.containsKey(product);
    }

    public int getCount(){
        return productToQuantity.size();
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

    public void addCustomer(Customer customer) {
        customerToPoints.put(customer, 0);
    }

    public int getProductCount(Product product) throws CustomerNotFoundException{
        try {
            return productToQuantity.get(product);
        } catch (CustomerNotFoundException exc) {
            return 0;
        }
    }

    public int getCustomerPoints(Customer customer) {
        Integer points = customerToPoints.get(customer);
        if (points != null) {
            return points.intValue();
        } else {
            return 0;
        }
    }

    public void addCustomerPoints(Customer customer, int points) throws CustomerNotFoundException {
        try {
            customerToPoints.put(customer, customerToPoints.get(customer) + points);
        } catch (CustomerNotFoundException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public void setCustomerPoints(Customer customer, int points) throws CustomerNotFoundException {
        try {
            customerToPoints.put(customer, points);
        } catch (CustomerNotFoundException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public void removeProduct(Product product) throws ProductNotFoundException{
        try {
            productToQuantity.remove(product);
        } catch (ProductNotFoundException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public void addToInventory(Product product, int amount) throws InvalidAmountException{
        
        if (productToQuantity.containsKey(product)) {
            if(amount < 0){
                throw new InvalidAmountException(amount);
            }else{
                productToQuantity.put(product, productToQuantity.get(product) + amount);                
            }
        } else 
            if(amount < 0)     {
                throw new InvalidAmountException(amount);
            }
            else {
                productToQuantity.put(product, amount);
            }
        }
    
    public double purchase(Product product, int amount) throws ProductNotFoundException, InvalidAmountException {
        if (productToQuantity.containsKey(product)) {
            if (productToQuantity.get(product) >= amount) {
                productToQuantity.put(product, productToQuantity.get(product) - amount);
                return product.getPrice() * amount;
            } else {
                throw new InvalidAmountException(productToQuantity.get(product), amount);
            }
        } else {
            throw new ProductNotFoundException(product);
        }
    }   
}

class CustomerNotFoundException extends RuntimeException {
    private String phone;
    private String name;
    private Customer customer;

    CustomerNotFoundException(String phone) {//her kombinasyondan constructor yazdım hangisini severseniz onu kullanın
        this.phone = phone;
    }

    CustomerNotFoundException(String phone, Customer customer) {//her kombinasyondan constructor yazdım hangisini severseniz onu kullanın
        this.phone = phone;
        this.customer = customer;
    }

    CustomerNotFoundException(String name, String phone, Customer customer) {//her kombinasyondan constructor yazdım hangisini severseniz onu kullanın
        this.name = name;
        this.customer = customer;
        this.phone = phone;
    }

    public String toString() {
        return "CustomerNotFoundException: Name - " + name ;
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
    private Integer quantity;

    InvalidAmountException(int amount) {
        this.amount = amount;
    }

    InvalidAmountException(int amount, Integer quantity) {
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

class ProductNotFoundException extends RuntimeException {
    private Long ID;
    private String name;
    private Product product;

    ProductNotFoundException(Long ID) {//bunları silsem mi silmesem mi bilemedim🤠🤠🤠🤠🤠🤠
        this.ID = ID;
        this.name = null;
    }

    ProductNotFoundException(String name) {//bunları silsem mi silmesem mi bilemedim🤠🤠🤠🤠🤠🤠
        this.name = name;
        this.ID = 123L;
    }

    ProductNotFoundException(Product product){
        this.product = product;
    }

    ProductNotFoundException(Product product, String name, Long ID) {
        this.product = product;
        this.name = name;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "ProductNotFoundException: ID - " + ID + " Name - " + name;
    }
}

class StoreNotFoundException extends IllegalArgumentException{
    private String name;

    StoreNotFoundException(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "StoreNotFoundException: " + name;
    }
}
