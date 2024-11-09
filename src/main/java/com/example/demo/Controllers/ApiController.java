package com.example.demo.Controllers;

import com.example.demo.Model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@RestController
@Slf4j
public class ApiController {

    @Autowired
    DynamoDbTable<Customer> customerDynamoDbTable;

    @Autowired
    DynamoDbEnhancedClient enhancedClient;

    @Autowired
    S3Client s3Client;

    @PostMapping("createRecord")
    public Customer createRecord(@RequestBody Customer customer){
//        customerDynamoDbTable.createTable();
        PutItemEnhancedRequest<Customer> request = PutItemEnhancedRequest.builder(Customer.class)
                .item(customer)
                .build();

        // Use putItemWithResponse to save and retrieve the updated item
        customer = customerDynamoDbTable.updateItem(customer);

        // Return the updated item
        return customer; // This contains the updated customer object
//        TransactWriteItemsEnhancedRequest transactionRequest = TransactWriteItemsEnhancedRequest.builder()
//                .addPutItem(customerDynamoDbTable, customer)
//                .build();
//
//        TransactWriteItemsEnhancedResponse response = enhancedClient.transactWriteItemsWithResponse(transactionRequest);
//        return customerDynamoDbTable.getItem(r -> r.key(k -> k.partitionValue(transactionRequest.transactWriteItems().get(0).put().item().get("CustomerId"))));
//        customerDynamoDbTable.putItem(customer);
//        PutObjectRequest request = PutObjectRequest.builder().key("Dilip/MOO/rkreddy.pdf").bucket("my-first-bucket").build();
//        File file = new File("C:/Users/Dilip/Downloads/rkreddy.pdf");
//        s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromFile(file));
    }

    @GetMapping("getRecord")
    public ResponseEntity<byte[]> getRecord(@RequestParam String customerName){
        // Access the DynamoDB table and GSI

        // Define the query condition based on the GSI
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(k -> k.partitionValue(customerName));

        // Query the index and collect results
        SdkIterable<Page<Customer>> results = customerDynamoDbTable.index("CustomerName").query(queryConditional);
        GetObjectRequest request = GetObjectRequest.builder()
                .key("Dilip/MOO/rkreddy.pdf")
                .bucket("my-first-bucket")
                .build();
        // Set the content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(request);
        log.info(responseBytes.response().responseMetadata().toString());
        return new ResponseEntity<>(responseBytes.asByteArray(), headers, HttpStatus.OK);
    }
}
