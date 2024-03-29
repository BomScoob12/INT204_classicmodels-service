package sit.int204.classicmodelsservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import sit.int204.classicmodelsservice.entities.Employee;
import sit.int204.classicmodelsservice.entities.Office;
import sit.int204.classicmodelsservice.repositories.EmployeeRepository;
import sit.int204.classicmodelsservice.repositories.OfficeRepository;

import java.util.List;

@Service
public class EmployeeService {
    //auto create object
    @Autowired
    private EmployeeRepository repository;
    private OfficeRepository officeRepository;

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee getEmployee(Integer empNumber) {
        return repository.findById(empNumber).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee Id " + empNumber + " DOES NOT EXIST !!!"));
    }

    @Transactional
    public Employee createNewEmployee(Employee employee, String officeId) {
        Office office = officeRepository.findById(officeId).orElse(null);
        if (office == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Office Id DOES NOT EXIST !!!");
        } else {
            employee.setOffice(office);
            return repository.save(employee);
        }
    }

    @Transactional
    public void removeEmployee(Integer empNumber) {
        Employee employee = this.getEmployee(empNumber);
        repository.delete(employee);
    }

    public Employee updateEmployee(Integer empNumber, Employee employee) {
        if (employee.getId() != null && employee.getId() != 0) {
            if (!employee.getId().equals(empNumber)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Conflict Employee number ( " + empNumber + " vs " + employee.getId());
            }
        }
        this.getEmployee(empNumber);
        return repository.save(employee);
    }
}
