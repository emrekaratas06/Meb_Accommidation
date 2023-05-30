package meb.gov.tr.meb_accommidation.controller;

import meb.gov.tr.meb_accommidation.entity.Employee;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.service.EmployeeService;
import meb.gov.tr.meb_accommidation.service.TeacherHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TeacherHomeService teacherHomeService;

    public Model addModelAttributes(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("teacherHomes" , teacherHomeService.findAll());
        return model;
    }

    // Verilen öğretmenevine ait tüm çalışanları getirir
    @GetMapping("/teacherhome/{id}")
    public ResponseEntity<List<Employee>> getAllEmployeesByTeacherHome(@PathVariable(value = "id") Long teacherHomeId) {
        TeacherHome teacherHome = new TeacherHome();
        teacherHome.setId((long) Math.toIntExact(teacherHomeId));
        List<Employee> employees = employeeService.getAllEmployeesByTeacherHome(teacherHome);
        return ResponseEntity.ok().body(employees);
    }

    // TC kimlik numarasına göre çalışan getirir
    @GetMapping("/{tcNo}")
    public ResponseEntity<Employee> getEmployeeByTcNo(@PathVariable(value = "tcNo") String tcNo) {
        Employee employee = employeeService.getEmployeeByTcNo(tcNo);
        return ResponseEntity.ok().body(employee);
    }

    // Yeni bir çalışan kaydı oluşturur
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee newEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok().body(newEmployee);
    }

    // Tc nosu Verilen bir çalışanın kaydını günceller
    @PutMapping("/{tcNo}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") String tcNo, @Valid @RequestBody Employee employeeDetails) {
        Employee employee = employeeService.getEmployeeByTcNo(tcNo);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        employee.setEmployeeTcNo(employeeDetails.getEmployeeTcNo());
        employee.setTeacherHome(employeeDetails.getTeacherHome());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setPhone(employeeDetails.getPhone());
        employee.setEmail(employeeDetails.getEmail());
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return ResponseEntity.ok().body(updatedEmployee);
    }


    //Get All States
    @GetMapping("/employees")
    public String findAll(Model model){
        addModelAttributes(model);
        return "/employee/employees";
    }

    @GetMapping("/employee/employeeAdd")
    public String addEmployee(Model model){
        addModelAttributes(model);
        return "/employee/employeeAdd";
    }

    @GetMapping("/employee/employee/{op}/{id}")
    public String editEmployee(@PathVariable Integer id, @PathVariable String op, Model model){
        addModelAttributes(model);
        model.addAttribute("employee", employeeService.findById(id));
        return "/employee/employee" + op;
    }

    //Add State
    @PostMapping(value="/employee/employees")
    public String addNew(Employee employee) {
        employeeService.save(employee);
        return "redirect:/employees";
    }

    @Transactional
    @RequestMapping(value="/employee/employees/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable Integer id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }

    @PostMapping("/employee/update/{id}")
    public String updateEmployee(@PathVariable Integer id, @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "employeeEdit";
        }

        Optional<Employee> existingEmployee = Optional.ofNullable(employeeService.findById(id));
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = existingEmployee.get();
            updatedEmployee.setEmployeeTcNo(employee.getEmployeeTcNo());
            updatedEmployee.setFirstName(employee.getFirstName());
            updatedEmployee.setLastName(employee.getLastName());
            updatedEmployee.setPhone(employee.getPhone());
            updatedEmployee.setEmail(employee.getEmail());
            // Diğer özelliklerin güncellenmesi

            employeeService.updateEmployee(updatedEmployee);
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "redirect:/employees";
        } else {
            model.addAttribute("errorMessage", "Employee not found with ID: " + id);
            return "employeeEdit";
        }
    }

}
