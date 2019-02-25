package com.ling.elasticsearch.repository;

import com.ling.elasticsearch.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface CustomerRepository extends ElasticsearchRepository<Customer,String> {
    public Page<Customer> findByAddress(String address, Pageable pageable);
    public List<Customer> findByAddress(String address);
    public Customer findByUserName(String userName);
    void deleteByUserName(String userName);
}
