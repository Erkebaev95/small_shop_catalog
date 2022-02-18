package task;

import task.entity.Category;
import task.entity.Option;
import task.entity.Product;
import task.entity.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Test {
    private static final String CATEGORY_TITLE = "Выберите список категории по номеру:";
    private static final String PRODUCT_NAME = "Введите название товара: ";
    private static final String PRODUCT_PRICE = "Введите цену товара: ";
    private static final String MANUFACTURER = "Введите производитель: ";
    private static final String DIAGONAL = "Введите диагональ: ";
    private static final String MATRIX = "Введите матрицу: ";
    private static final String MATERIAL = "Введите материал: ";

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();

        System.out.println(CATEGORY_TITLE.toUpperCase());

        TypedQuery<Category> query = manager.createQuery(
                "select c from Category c", Category.class);
        List<Category> categoryList = query.getResultList();
        for (Category category : categoryList) {
            System.out.println(category.getId() + " - " + category.getName());
        }

        TypedQuery<Option> query1 = manager.createQuery(
                "select o from Option o", Option.class);
        List<Option> optionList = query1.getResultList();

        try {
            manager.getTransaction().begin();

            Scanner scanner = new Scanner(System.in);
            long categoryNumber = scanner.nextLong();
            scanner.nextLine();

            System.out.println(PRODUCT_NAME);
            Category category = manager.find(Category.class, categoryNumber);
            Product product = new Product();
            String productName = scanner.nextLine();
            product.setName(productName);

            System.out.println(PRODUCT_PRICE);
            Integer productPrice = scanner.nextInt();
            product.setPrice(productPrice);
            product.setCategoryId(category);
            manager.persist(product);

            for (Option option : optionList) {
                option = manager.find(Option.class, option.getId());
                System.out.println("Введите что то");
                String valueInput = scanner.next();
                Value value = new Value(valueInput, product, option);
                manager.persist(value);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
