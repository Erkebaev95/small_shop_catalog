package task;

import task.entity.Product;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class Filter {
    private static final EntityManagerFactory MANAGER =
            Persistence.createEntityManagerFactory("default");

    public static void main(String[] args) {
        // Поля для создания условии
        Long categoryId = 3L;
        String name = null;
        String from = null;
        String to = null;

        CriteriaBuilder builder = MANAGER.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        // Собрал в список
        if (categoryId != null) {
            predicates.add(builder.equal(root.get("categoryId"), categoryId));
        }
        if (name != null) {
            predicates.add(builder.equal(root.get("name"), name));
        }
        if (from != null) {
            predicates.add(builder.greaterThan(root.get("price"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThan(root.get("price"), to));
        }

        // Закинул в where этот список преобразованный к массиву
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Product> typedQuery = MANAGER.createEntityManager().createQuery(criteriaQuery);
        List<Product> products = typedQuery.getResultList();

        // через цикл вывожу данные
        for (Product p : products) {
            System.out.println(p.getName() + " - " + p.getPrice());
        }
    }
}

