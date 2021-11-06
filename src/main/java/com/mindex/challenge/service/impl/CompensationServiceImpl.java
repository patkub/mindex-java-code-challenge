package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String empId = compensation.getEmployeeId();
        Employee employee = employeeRepository.findByEmployeeId(empId);

        if (employee == null) {
            LOG.debug("Compensation invalid employeeId: [{}]", empId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid employeeId: " + empId);
        }

        if (compensationRepository.findByEmployeeId(empId) != null) {
            // a compensation for this employee already exists
            LOG.debug("Compensation for employeeId: [{}] already exists!", empId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Compensation for employeeId: " + empId + " already exists!");
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation by employeeId [{}]", employeeId);

        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);

        if (compensation == null) {
            LOG.debug("Compensation invalid employeeId: [{}]", employeeId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid employeeId: " + employeeId);
        }

        return compensation;
    }
}
