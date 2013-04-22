package com.myeclipseide.ws;

import com.myeclipseide.ws.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.sun.jersey.spi.resource.Singleton;

@Produces("application/xml")
@Path("items")
@Singleton
public class ItemsResource {
	
	/** This is where the hibernate had to be implemented since I did not have the jars 
	 * and also since I had time constraint the below is in memory only.**/
	 private TreeMap<Integer, Item> itemMap = new TreeMap<Integer, Item>();
	private Configuration cfg;
	private	SessionFactory sf;
	private	Session s;
	
	  public ItemsResource() {
	 
	       
	  }
	  
	  private void repeatConfig()
	  {
		   cfg = (new Configuration()).configure("Myfile.cfg.xml");
			sf = cfg.buildSessionFactory();
			s = sf.openSession();
		
	  }

	@GET
	public List<Item> getItems() {
		List<Item> items = new ArrayList<Item>();
		int id1 =0;
		Configuration cfg = (new Configuration()).configure("Myfile.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		String SQL_QUERY ="from Item";
		Query query = s.createQuery(SQL_QUERY);
		Iterator it=query.iterate();
		while(it.hasNext())
		{
			System.out.println("Inside the while loop");
			Item item1 = new Item();
			item1=(Item)it.next();
			System.out.println("store"+item1.getStorename());
			itemMap.put(id1, item1);
			id1++;
		}
	    items.addAll(itemMap.values());
	    s.close();
	    return items;
	    
	}


	@GET
	@Path("{id}")
	public Item getItem(@PathParam("id") int cid) {
		
		Configuration cfg = (new Configuration()).configure("Myfile.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Item item1= (Item)s.get(Item.class, cid);
		
		return item1;	
		
	}


	@POST
	@Path("create")
	@Produces("text/html")
	@Consumes("application/xml")
	public String createItem(Item item) {
		int id1 =0;
		repeatConfig();
		Transaction tx = s.beginTransaction();
		String SQL_QUERY ="from Item";
		Query query = s.createQuery(SQL_QUERY);
		Iterator it=query.iterate();
		while(it.hasNext())
		{
		Item item1 = new Item();
		item1=(Item)it.next();
		itemMap.put(id1, item1);
		id1++;
		}
	    item.setId(id1);	    
		Item citem = new Item();
	    citem.setId(item.getId());
	    citem.setItemdescription(item.getItemdescription());
	    citem.setLocation(item.getLocation());
	    citem.setPrice(item.getPrice());
		//citem.setStorename(item.getStorename());
		citem.setStorename("Wallmart");
	    citem.setCurrency(item.getCurrency());
	    itemMap.put(id1, citem);
		s.save(citem);
		s.flush();
		tx.commit();
		s.close();
		return "Item " + item.getStorename() + " added with Id " + id1;

	}

	@Produces("text/plain")
	@Consumes("application/xml")
	@PUT
	@Path("update")
	public String updateItem(@PathParam("id") int id,Item item) {
		Configuration cfg = (new Configuration()).configure("Myfile.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Item item1= (Item)s.get(Item.class, item.getId());
		item1.setStorename("Wallmart");
		itemMap.put(id, item1);
		s.update(item1);
		s.flush();
		s.close();
		return "updated";
		
	}
}