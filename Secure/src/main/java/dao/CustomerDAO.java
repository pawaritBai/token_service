package dao;

import java.util.List;



import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import model.Customer;

public class CustomerDAO {
	public List<Customer> getAllCustomers() {
	    try (Session session = SessionUtil.getSession()) {
	        List<Customer> customers = session.createQuery("from Customer", Customer.class).getResultList();
	        
	        // Force load phonenumbers ก่อนปิด session
	        for (Customer customer : customers) {
	            Hibernate.initialize(customer.getPhonenumbers());
	        }
	        
	        return customers;
	    }
	}



	public boolean addCustomer(Customer c) {
		try {
			Session session = SessionUtil.getSession();
			Transaction tx = session.beginTransaction();
			//tx.begin();
			session.save(c);
			tx.commit();
			session.close();
		} catch (TransactionException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Customer findCustomerByName(String name) {
	    Session session = null;
	    Customer customer = null;
	    
	    try {
	        session = SessionUtil.getSession();
	        Criteria cr = session.createCriteria(Customer.class);
	        cr.add(Restrictions.eq("name", name));
	        customer = (Customer) cr.uniqueResult();
	        Hibernate.initialize(customer.getPhonenumbers());
	    } finally {
	        if (session != null) {
	            session.close(); // ปิด session เพื่อคืนทรัพยากร
	        }
	    }
	    
	    return customer;
	}
	
	public Customer findCustomer(String userName) {
	    try (Session session = SessionUtil.getSession()) {
	        Query<Customer> query = session.createQuery("from Customer where username = :username", Customer.class);
	        query.setParameter("username", userName); // ใช้ Parameter Binding ป้องกัน SQL Injection
	        
	        List<Customer> customers = query.getResultList();
	        return customers.isEmpty() ? null : customers.get(0); // ป้องกัน IndexOutOfBoundsException
	    }
	}






	public Customer findCustomerById(int id) {
	    try (Session session = SessionUtil.getSession()) { // ใช้ try-with-resources
	        return session.get(Customer.class, id); // ไม่ต้องแปลง type
	    }
	}

	
	public boolean updateCustomer(Customer updatedCustomer) {
	    Session session = SessionUtil.getSession();
	    Transaction transaction = null;
	    
	    try {
	        transaction = session.beginTransaction();
	        
	        // ดึงข้อมูลลูกค้าจากฐานข้อมูลโดยใช้ ID
	        Customer existingCustomer = (Customer) session.get(Customer.class, updatedCustomer.getCusId());
	        
	        if (existingCustomer != null) {
	            // อัปเดตข้อมูลที่ต้องการเปลี่ยนแปลง
	            existingCustomer.setName(updatedCustomer.getName());
	            existingCustomer.setPhonenumbers(updatedCustomer.getPhonenumbers());
	            
	            session.update(existingCustomer); // อัปเดตในฐานข้อมูล
	            transaction.commit();
	        } else {
	            System.out.println("Customer not found!");
	            return false;
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	    return true;
	}
	
	public boolean deleteCustomer(int id) {
	    Session session = SessionUtil.getSession();
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();

	        // ดึงข้อมูลลูกค้าจากฐานข้อมูล
	        Customer customer = (Customer) session.get(Customer.class, id);

	        if (customer != null) {
	            session.delete(customer); // ลบข้อมูลออกจากฐานข้อมูล
	            transaction.commit();
	            System.out.println("Customer deleted successfully.");
	        } else {
	            System.out.println("Customer not found.");
	            return false;
	        }

	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	    return true;
	}
	
	public Customer findCustomer(Customer c) {
		//System.out.print("adsadasd");
	    Session session = SessionUtil.getSession();
	    Criteria cr = session.createCriteria(Customer.class);
	    cr.add(Restrictions.eq("username", c.getUsername()));
	    cr.add(Restrictions.eq("pwd", c.getPwd()));
	    
	    // ใช้ uniqueResult() ถ้าคาดว่าควรได้เพียงรายการเดียว
	    Customer customer = (Customer) cr.uniqueResult();
	    return customer;
	}

}
