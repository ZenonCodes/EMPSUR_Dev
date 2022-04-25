package com.empsur.empsur.domain;

import com.empsur.empsur.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Pattern(regexp = "[\\d]+")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "adress_line_1", nullable = false)
    private String adressLine1;

    @Column(name = "adress_line_2")
    private String adressLine2;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "state")
    private String state;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "documentations", "record", "company", "department" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employees", "company" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Company idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Status getStatus() {
        return this.status;
    }

    public Company status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPhone() {
        return this.phone;
    }

    public Company phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdressLine1() {
        return this.adressLine1;
    }

    public Company adressLine1(String adressLine1) {
        this.setAdressLine1(adressLine1);
        return this;
    }

    public void setAdressLine1(String adressLine1) {
        this.adressLine1 = adressLine1;
    }

    public String getAdressLine2() {
        return this.adressLine2;
    }

    public Company adressLine2(String adressLine2) {
        this.setAdressLine2(adressLine2);
        return this;
    }

    public void setAdressLine2(String adressLine2) {
        this.adressLine2 = adressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public Company city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Company country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return this.state;
    }

    public Company state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setCompany(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setCompany(this));
        }
        this.employees = employees;
    }

    public Company employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Company addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setCompany(this);
        return this;
    }

    public Company removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setCompany(null);
        return this;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setCompany(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setCompany(this));
        }
        this.departments = departments;
    }

    public Company departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Company addDepartment(Department department) {
        this.departments.add(department);
        department.setCompany(this);
        return this;
    }

    public Company removeDepartment(Department department) {
        this.departments.remove(department);
        department.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", phone='" + getPhone() + "'" +
            ", adressLine1='" + getAdressLine1() + "'" +
            ", adressLine2='" + getAdressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
