package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        LOG.debug("Finding employee with id [{}]", id);

        // find the employee in the database by id
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        // create a reporting structure instance for this employee
        ReportingStructure reportStruct = new ReportingStructure(employee);

        // compute the number of direct reports for an employee and all of their distinct reports
        reportStruct.getNumberOfReports();

        // return the information
        return reportStruct;
    }
}
