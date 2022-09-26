package hiber.dao;

import hiber.model.User;
import hiber.model.Car;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }

    public void deleteAllUsers() {
        List<User> users = listUsers();
        for (User user : users) {
            sessionFactory.getCurrentSession().delete(user);
        }
    }

    @Override
    public User getUserByCarModelAndSeries(String model, int series) {
        TypedQuery<Car> findCarQuery = sessionFactory.getCurrentSession().createQuery("from Car where car_model = :model and car_series = :series")
                .setParameter("model", model)
                .setParameter("series", series);
        List<Car> findCarList = findCarQuery.getResultList();
        if (!findCarList.isEmpty()) {
            Car findCar = findCarList.get(0);
            List<User> ListUser = listUsers();
            User FindUser = ListUser.stream()
                    .filter(user -> user.getCar().equals(findCar))
                    .findAny()
                    .orElse(null);
            return FindUser;
        }
        return null;
    }

}
