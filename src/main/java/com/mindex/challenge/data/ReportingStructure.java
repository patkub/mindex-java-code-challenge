package com.mindex.challenge.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ReportingStructure type
 * @author Patrick Kubiak
 */
public class ReportingStructure {

    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        // calculate number of reports for this employee
        this.numberOfReports = computeNumReports(this.employee);
        // return it
        return this.numberOfReports;
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
            // get first employee at top of stack
            Employee current = dfsStack.pop();

            // mark the current employee as visited
            // by adding their employee id to the visited list
            String currentEmployeeId = current.getEmployeeId();
            visitedEmployeeIds.add(currentEmployeeId);

            // get current employee's list of direct reports
            List<Employee> currentDirectReports = employee.getDirectReports();

            for (Employee child : currentDirectReports) {
                if (!visitedEmployeeIds.contains(child.getEmployeeId())) {
                    // add only unvisited direct reports to total
                    numReports++;
                    // push adjacent nodes that have yet to be visited onto dfs stack
                    dfsStack.push(child);
                }
            }
        }

        return numReports;
    }

}
