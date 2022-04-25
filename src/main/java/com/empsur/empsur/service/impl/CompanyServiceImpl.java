package com.empsur.empsur.service.impl;

import com.empsur.empsur.domain.Company;
import com.empsur.empsur.repository.CompanyRepository;
import com.empsur.empsur.service.CompanyService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    @Override
    public Company update(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> partialUpdate(Company company) {
        log.debug("Request to partially update Company : {}", company);

        return companyRepository
            .findById(company.getId())
            .map(existingCompany -> {
                if (company.getName() != null) {
                    existingCompany.setName(company.getName());
                }
                if (company.getIdNumber() != null) {
                    existingCompany.setIdNumber(company.getIdNumber());
                }
                if (company.getStatus() != null) {
                    existingCompany.setStatus(company.getStatus());
                }
                if (company.getPhone() != null) {
                    existingCompany.setPhone(company.getPhone());
                }
                if (company.getAdressLine1() != null) {
                    existingCompany.setAdressLine1(company.getAdressLine1());
                }
                if (company.getAdressLine2() != null) {
                    existingCompany.setAdressLine2(company.getAdressLine2());
                }
                if (company.getCity() != null) {
                    existingCompany.setCity(company.getCity());
                }
                if (company.getCountry() != null) {
                    existingCompany.setCountry(company.getCountry());
                }
                if (company.getState() != null) {
                    existingCompany.setState(company.getState());
                }

                return existingCompany;
            })
            .map(companyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAll() {
        log.debug("Request to get all Companies");
        return companyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
