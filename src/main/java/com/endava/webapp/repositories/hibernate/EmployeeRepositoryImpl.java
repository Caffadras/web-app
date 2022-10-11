package com.endava.webapp.repositories.hibernate;

import com.endava.webapp.model.Employee;
import com.endava.webapp.repositories.EmployeeRepository;
import com.endava.webapp.repositories.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Employee> findAll() {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            return session.createQuery("FROM Employee").list();
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            return Optional.ofNullable(session.get(Employee.class, id));
        }
    }

    @Override
    public Employee save(Employee employee) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
        }
        return employee;
    }


    @Override
    public Employee update(Employee employee) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
        }
        return employee;
    }

    @Override
    public void deleteById(Long id) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.getNamedQuery("deleteEmployeesById");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        }
    }
}
