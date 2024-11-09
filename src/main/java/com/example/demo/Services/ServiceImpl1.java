package com.example.demo.Services;

import com.example.demo.Model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Service
public class ServiceImpl1 implements ApiService {
    @Autowired
    DynamoDbTable<Customer> customerTable;

    public void getCustomersByName(String customerName) {
        // Access the DynamoDB table and GSI

        // Define the query condition based on the GSI
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(k -> k.partitionValue(customerName));

        // Query the index and collect results
        SdkIterable<Page<Customer>> results = customerTable.index("CustomerNameIndex").query(queryConditional);
    }
}
