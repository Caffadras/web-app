package com.endava.webapp.repositories.hibernate;

import com.endava.webapp.model.Department;
import com.endava.webapp.repositories.DepartmentRepository;
import com.endava.webapp.repositories.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {


    @Override
    @SuppressWarnings("unchecked")
    public List<Department> findAll() {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            return session.createQuery("FROM Department").list();
        }
    }

    @Override
    public Optional<Department> findById(Long id) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            return Optional.ofNullable(session.get(Department.class, id));
        }
    }

    @Override
    public Department save(Department department) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(department);
            transaction.commit();
        }
        return department;
    }

    @Override
    public Department update(Department department) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(department);
            transaction.commit();
        }
        return department;
    }

    @Override
    public void deleteById(Long id) {
        try(Session session = HibernateSessionFactoryUtil.openSession()){
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.getNamedQuery("deleteDepartmentsById");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        }
    }
}
