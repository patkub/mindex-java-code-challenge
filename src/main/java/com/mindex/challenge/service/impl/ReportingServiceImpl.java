package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.DirectReport;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ReportingServiceImpl
 * @author Patrick Kubiak
 */
@Service
public class ReportingServiceImpl implements ReportingService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Report for employee id [{}]", id);

        // find the employee in the database by id
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            LOG.debug("Compensation invalid employeeId: [{}]", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid employeeId: " + id);
        }

        // create a reporting structure instance for this employee
        ReportingStructure reportStruct = new ReportingStructure(employee);

        // compute the number of direct reports for an employee and all of their distinct reports
        int numReports = computeNumReports(employee);
        // update reporting structure
        reportStruct.setNumberOfReports(numReports);

        // return the information
        return reportStruct;
    }

    /**
     * Compute the number of direct reports for an employee and all of their distinct reports
     * Does a Depth-First Search Traversal (DFS) through direct reports
     * @param employee starting employee
     * @return total number of direct reports
     */
    private int computeNumReports(Employee employee) {
        // calculate number of reports
        int numReports = 0;

        // DFS stack of employees
        Stack<Employee> dfsStack = new Stack<>();

        // list of already visited employee ids
        List<String> visitedEmployeeIds = new ArrayList<>();

        // add first employee to stack
        dfsStack.push(employee);

        while (!dfsStack.isEmpty()) {
            // get employee at top of stack
            Employee current = dfsStack.pop();

            // mark current employee as visited
            visitedEmployeeIds.add(current.getEmployeeId());

            // get current employee's list of direct reports
            List<DirectReport> currentDirectReports = current.getDirectReports();

            if (currentDirectReports != null) {
                for (DirectReport child : currentDirectReports) {
                    String childEmployeeId = child.getEmployeeId();
                    if (!visitedEmployeeIds.contains(childEmployeeId)) {
                        // add only unvisited direct reports to total
                        numReports += 1;

                        // Find child employee by id in repository
                        Employee childEmp = employeeRepository.findByEmployeeId(childEmployeeId);
                        if (child == null) {
                            throw new RuntimeException("Invalid employeeId: " + childEmployeeId);
                        }

                        // push adjacent nodes that have yet to be visited onto dfs stack
                        dfsStack.push(childEmp);
                    }
                }
            }
        }

        return numReports;
    }
}
