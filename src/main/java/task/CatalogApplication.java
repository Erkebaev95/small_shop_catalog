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

public class CatalogApplication {
    public static void main(String[] args) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();

        Category category = new Category();
        Product product = new Product();
        Option option = new Option();
        Value value = new Value();
        Scanner sc = new Scanner(System.in);

        // Добавление данных в Базу
        while (true) {
            System.out.println("Для заполнение данных об оборудованиях введите - 1 " +
                    "\nЧтобы изменить данных введите - 2 " +
                    "\nДля добавления характеристики категории нажмите - 3 " +
                    "\nЧтобы закончить введите - end");

            String enter = sc.nextLine();
            if (enter.equals("1")) {

                try {
                    manager.getTransaction().begin();
                    System.out.println("Список категории");
                    TypedQuery<Category> query1 = manager.createQuery(
                            "select c from Category c", Category.class);
                    List<Category> categoryList = query1.getResultList();
                    for (Category category1 : categoryList) {
                        System.out.println(category1.getId() + " - " + category1.getName());
                    }

                    // Добавляем данные в Базу
                    System.out.println("Добавьте новую категорию продукта: ");
                    String cat = sc.nextLine();
                    category.setName(cat);

                    System.out.println("Список продуктов");
                    TypedQuery<Product> query2 = manager.createQuery("select p from Product p", Product.class);
                    List<Product> productList = query2.getResultList();
                    for (Product product1 : productList) {
                        System.out.println(product1.getId() + " - " + product1.getName() + " - " + product1.getPrice());
                    }

                    System.out.println("Добавьте новую продукта: ");
                    String name = sc.nextLine();
                    product.setName(name);

                    System.out.println("Установите цену для продукта: ");
                    int price = sc.nextInt();
                    product.setPrice(price);
                    product.setCategoryId(category);

                    System.out.println("Добавьте деталь для продукта: ");
                    String details = sc.nextLine();
                    option.setName(details);
                    option.setCategoryId(category);

                    System.out.println("Введите значению деталь: ");
                    String values = sc.nextLine();
                    value.setValue(values);
                    value.setProductId(product);
                    value.setOptionId(option);

                    // Сохраняем
                    manager.persist(category);
                    manager.persist(product);
                    manager.persist(option);
                    manager.persist(value);
                    System.out.println("Успешно сохранено!");

                    manager.getTransaction().commit();
                } catch (Exception e) {
                    manager.getTransaction().rollback();
                    e.printStackTrace();
                }
            } else if (enter.equals("2")) {
                request(manager);
            } else if (enter.equals("3")) {
                addDetails(manager);
            } else if (enter.equals("end")) {
                break;
            }
        }
    }

    // Добавление детали по категории
    public static void addDetails(EntityManager manager) {
        Scanner scanner = new Scanner(System.in);
        Option option = new Option();
        Value value = new Value();

        // выводим на экран список категории
        TypedQuery<Category> query1 = manager.createQuery(
                "select c from Category c", Category.class);
        List<Category> categoryList = query1.getResultList();
        for (Category category : categoryList) {
            System.out.println(category.getId() + " - " + category.getName());
        }

        // выбираем категорию
        System.out.println("Введите id категории:");
        long enter = scanner.nextLong();
        try {
            // Запускаем транзакцию
            manager.getTransaction().begin();
            Category category = manager.find(Category.class, enter);

            // выводим на экран список продукта
            TypedQuery<Product> query2 = manager.createQuery(
                    "select p from Product p", Product.class);
            List<Product> productList = query2.getResultList();
            for (Product product : productList) {
                System.out.println(product.getId() + " - " + product.getName());
            }

            // выбираем продукта
            System.out.println("Введите id продукта:");
            long enter2 = scanner.nextLong();
            Product product = manager.find(Product.class, enter2);

            // показываем информацию о выбранном
            System.out.println("Вы выбрали категорию - " + category.getName() +
                    "\nВы выбрали продукт - " + product.getName() +
                    "\nДобавьте характеристику для категории: ");

            // Добавляем названию детали
            System.out.println("Введите названию:");
            String details1 = scanner.next();
            option.setName(details1);
            option.setCategoryId(category);

            // Добавляем значению детали
            System.out.println("Введите значению:");
            String details2 = scanner.next();
            value.setValue(details2);
            value.setProductId(product);
            value.setOptionId(option);

            // Сохраняем их
            manager.persist(option);
            manager.persist(value);
            System.out.println("Saved!");

            // Закрываем транзакцию
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    // Изменяем товар
    public static void request(EntityManager manager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Список продуктов");
        TypedQuery<Product> query2 = manager.createQuery("select p from Product p", Product.class);
        List<Product> productList = query2.getResultList();
        for (Product product : productList) {
            System.out.println(product.getId() + " - " + product.getName() + " - " + product.getPrice());
        }
        System.out.print("Введите id: ");
        long enter = scanner.nextLong();
        try {
            manager.getTransaction().begin();
            Product product = manager.find(Product.class, enter);
            scanner.nextLine();

            System.out.println("Измените названию товара " + product.getName());
            String proName = scanner.nextLine();
            product.setName(proName);

            System.out.println("Измените цену товара " + product.getPrice());
            int proPrice = scanner.nextInt();
            product.setPrice(proPrice);

            System.out.println(product.getCategoryId().getOption());
            System.out.println("Измените:");
            for (Value value : product.getValue()) {
                System.out.println(value.getOptionId().getName());
                value.setValue(scanner.nextLine());
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}