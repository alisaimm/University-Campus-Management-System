package com.campus.repository;

import com.campus.model.*;

import java.io.Serializable;
import java.util.ArrayList;

public class CampusRepository<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String repositoryName;
    private ArrayList<T> items;

    public CampusRepository() {
        items = new ArrayList<>();
    }

    public CampusRepository(String repositoryName) {
        this.items = new ArrayList<>();
        setRepositoryName(repositoryName);
    }

    public void setRepositoryName(String repositoryName) {
        if(repositoryName != null && !repositoryName.isEmpty()) {
            this.repositoryName = repositoryName;
        } else {
            System.out.println("Invalid repository name");
        }
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void addItem(T item) {
        if(item == null){
            System.out.println("Item cannot be null");
            return;
        }

        if(items.contains(item)) {
            System.out.println("Item already exists");
            return;
        }

        items.add(item);
        System.out.println("Item added successfully");
    }

    public void removeItem(T item) {
        if(item == null){
            System.out.println("Item cannot be null");
            return;
        }

        if(items.contains(item)) {
            items.remove(item);
            System.out.println("Item removed successfully");
        } else {
            System.out.println("Item does not exist");
        }
    }

    public T get(int index) {
        if(index >= 0 && index < items.size()) {
            return items.get(index);
        } else {
            System.out.println("Invalid index");
            return null;
        }
    }

    public ArrayList<T> getAll() {
        return new ArrayList<>(items);
    }

    public int getSize() {
        return items.size();
    }

    public boolean contains(T item) {
        if (item == null){
            System.out.println("Item cannot be null");
            return false;
        }

        return items.contains(item);
    }

    public T findById(String id) {
        if (id == null) return null;
        
        T item;

        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            if (item instanceof CampusEntity && id.equals(((CampusEntity) item).getEntityID())) {
                return item;
            } else if (item instanceof Course && id.equals(((Course) item).getCourseID())) {
                return item;
            } else if (item instanceof Department && id.equals(((Department) item).getDeptCode())) {
                return item;
            } else if (item instanceof User && id.equals(((User) item).getUsername())) {
                return item;
            } else if (item instanceof Student && id.equals(((Student) item).getStudentID())) {
                return item;
            }
        }
        
        return null;
    }

}
