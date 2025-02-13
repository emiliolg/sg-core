
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.AbstractAmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.common.collections.Colls.filter;

/**
 * AmazonDynamoDB implementation in memory.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "WeakerAccess", "OverlyComplexClass" })
public class DynamoMemoryDB extends AbstractAmazonDynamoDBAsync {

    //~ Instance Fields ..............................................................................................................................

    private final HashMap<String, List<String>>                                 mapKeys = new HashMap<>();
    private final HashMap<String, HashMap<String, Map<String, AttributeValue>>> maps    = new HashMap<>();

    //~ Methods ......................................................................................................................................

    @Override public BatchGetItemResult batchGetItem(BatchGetItemRequest batchGetItemRequest)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems, String returnConsumedCapacity)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<BatchGetItemResult> batchGetItemAsync(BatchGetItemRequest batchGetItemRequest)
        throws AmazonClientException
    {
        return asyncOp(this::batchGetItem, batchGetItemRequest);
    }

    @Override public Future<BatchGetItemResult> batchGetItemAsync(Map<String, KeysAndAttributes> requestItems) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<BatchGetItemResult> batchGetItemAsync(BatchGetItemRequest                                   batchGetItemRequest,
                                                                  AsyncHandler<BatchGetItemRequest, BatchGetItemResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::batchGetItem, batchGetItemRequest, asyncHandler);
    }

    @Override public Future<BatchGetItemResult> batchGetItemAsync(Map<String, KeysAndAttributes> requestItems, String returnConsumedCapacity) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<BatchGetItemResult> batchGetItemAsync(Map<String, KeysAndAttributes>                        requestItems,
                                                                  AsyncHandler<BatchGetItemRequest, BatchGetItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<BatchGetItemResult> batchGetItemAsync(Map<String, KeysAndAttributes> requestItems, String returnConsumedCapacity,
                                                                  AsyncHandler<BatchGetItemRequest, BatchGetItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public BatchWriteItemResult batchWriteItem(BatchWriteItemRequest batchWriteItemRequest)
        throws AmazonClientException
    {
        batchWriteItemRequest.getRequestItems().entrySet().forEach(this::batchWriteItem);
        return new BatchWriteItemResult();
    }

    @Override public BatchWriteItemResult batchWriteItem(Map<String, List<WriteRequest>> requestItems)
        throws AmazonClientException
    {
        final BatchWriteItemRequest batchWriteItemRequest = new BatchWriteItemRequest();
        batchWriteItemRequest.setRequestItems(requestItems);
        return batchWriteItem(batchWriteItemRequest);
    }

    @Override public Future<BatchWriteItemResult> batchWriteItemAsync(BatchWriteItemRequest batchWriteItemRequest)
        throws AmazonClientException
    {
        return asyncOp(this::batchWriteItem, batchWriteItemRequest);
    }

    @Override public Future<BatchWriteItemResult> batchWriteItemAsync(Map<String, List<WriteRequest>> requestItems) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<BatchWriteItemResult> batchWriteItemAsync(BatchWriteItemRequest                                     batchWriteItemRequest,
                                                                      AsyncHandler<BatchWriteItemRequest, BatchWriteItemResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::batchWriteItem, batchWriteItemRequest, asyncHandler);
    }

    @Override public Future<BatchWriteItemResult> batchWriteItemAsync(Map<String, List<WriteRequest>>                           requestItems,
                                                                      AsyncHandler<BatchWriteItemRequest, BatchWriteItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public CreateTableResult createTable(CreateTableRequest createTableRequest)
        throws AmazonClientException
    {
        return createTable(createTableRequest.getAttributeDefinitions(),
            createTableRequest.getTableName(),
            createTableRequest.getKeySchema(),
            createTableRequest.getProvisionedThroughput());
    }

    @Override public CreateTableResult createTable(List<AttributeDefinition> attributeDefinitions, String tableName, List<KeySchemaElement> keySchema,
                                                   ProvisionedThroughput provisionedThroughput)
        throws AmazonClientException
    {
        maps.put(tableName, new HashMap<>());
        final ArrayList<String> keys = new ArrayList<>();
        for (final KeySchemaElement keySchemaElement : keySchema)
            keys.add(keySchemaElement.getAttributeName());
        mapKeys.put(tableName, keys);
        return new CreateTableResult();
    }

    @Override public Future<CreateTableResult> createTableAsync(CreateTableRequest createTableRequest)
        throws AmazonClientException
    {
        return asyncOp(this::createTable, createTableRequest);
    }

    @Override public Future<CreateTableResult> createTableAsync(CreateTableRequest                                  createTableRequest,
                                                                AsyncHandler<CreateTableRequest, CreateTableResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::createTable, createTableRequest, asyncHandler);
    }

    @Override public Future<CreateTableResult> createTableAsync(List<AttributeDefinition> attributeDefinitions, String tableName,
                                                                List<KeySchemaElement> keySchema, ProvisionedThroughput provisionedThroughput) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<CreateTableResult> createTableAsync(List<AttributeDefinition> attributeDefinitions, String tableName,
                                                                List<KeySchemaElement> keySchema, ProvisionedThroughput provisionedThroughput,
                                                                AsyncHandler<CreateTableRequest, CreateTableResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public DeleteItemResult deleteItem(DeleteItemRequest deleteItemRequest)
        throws AmazonClientException
    {
        return deleteItem(deleteItemRequest.getTableName(), deleteItemRequest.getKey());
    }

    @Override public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key)
        throws AmazonClientException
    {
        final Map<String, AttributeValue> remove = maps.get(tableName).remove(solveKey(tableName, key));
        return new DeleteItemResult().withAttributes(remove);
    }

    @Override public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key, String returnValues)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DeleteItemResult> deleteItemAsync(DeleteItemRequest deleteItemRequest)
        throws AmazonClientException
    {
        return asyncOp(this::deleteItem, deleteItemRequest);
    }

    @Override public Future<DeleteItemResult> deleteItemAsync(DeleteItemRequest                                 deleteItemRequest,
                                                              AsyncHandler<DeleteItemRequest, DeleteItemResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::deleteItem, deleteItemRequest, asyncHandler);
    }

    @Override public Future<DeleteItemResult> deleteItemAsync(String tableName, Map<String, AttributeValue> key) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DeleteItemResult> deleteItemAsync(String tableName, Map<String, AttributeValue> key,
                                                              AsyncHandler<DeleteItemRequest, DeleteItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DeleteItemResult> deleteItemAsync(String tableName, Map<String, AttributeValue> key, String returnValues) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DeleteItemResult> deleteItemAsync(String tableName, Map<String, AttributeValue> key, String returnValues,
                                                              AsyncHandler<DeleteItemRequest, DeleteItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public DeleteTableResult deleteTable(DeleteTableRequest deleteTableRequest)
        throws AmazonClientException
    {
        maps.remove(deleteTableRequest.getTableName());
        return new DeleteTableResult();
    }

    @Override public DeleteTableResult deleteTable(String tableName)
        throws AmazonClientException
    {
        maps.remove(tableName);
        return new DeleteTableResult();
    }

    @Override public Future<DeleteTableResult> deleteTableAsync(DeleteTableRequest deleteTableRequest)
        throws AmazonClientException
    {
        return asyncOp(this::deleteTable, deleteTableRequest);
    }

    @Override public Future<DeleteTableResult> deleteTableAsync(String tableName) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DeleteTableResult> deleteTableAsync(DeleteTableRequest                                  deleteTableRequest,
                                                                AsyncHandler<DeleteTableRequest, DeleteTableResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::deleteTable, deleteTableRequest, asyncHandler);
    }

    @Override public Future<DeleteTableResult> deleteTableAsync(String tableName, AsyncHandler<DeleteTableRequest, DeleteTableResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public DescribeLimitsResult describeLimits(DescribeLimitsRequest describeLimitsRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DescribeLimitsResult> describeLimitsAsync(DescribeLimitsRequest describeLimitsRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DescribeLimitsResult> describeLimitsAsync(DescribeLimitsRequest                                     describeLimitsRequest,
                                                                      AsyncHandler<DescribeLimitsRequest, DescribeLimitsResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public DescribeTableResult describeTable(DescribeTableRequest describeTableRequest)
        throws AmazonClientException
    {
        final String tableName = describeTableRequest.getTableName();
        if (!maps.containsKey(tableName)) {
            final AmazonServiceException amazonServiceException = new AmazonServiceException(TABLE + tableName + DOES_NOT_EXIST);
            amazonServiceException.setErrorCode(DynamoUtils.RESOURCE_NOT_FOUND_EXCEPTION);
            throw amazonServiceException;
        }

        return new DescribeTableResult().withTable(new TableDescription().withTableName(tableName).withTableStatus(TableStatus.ACTIVE));
    }

    @Override public DescribeTableResult describeTable(String tableName)
        throws AmazonClientException
    {
        if (!maps.containsKey(tableName)) {
            final AmazonServiceException amazonServiceException = new AmazonServiceException(TABLE + tableName + DOES_NOT_EXIST);
            amazonServiceException.setErrorCode(DynamoUtils.RESOURCE_NOT_FOUND_EXCEPTION);
            throw amazonServiceException;
        }
        return new DescribeTableResult().withTable(new TableDescription().withTableName(tableName).withTableStatus(TableStatus.ACTIVE));
    }

    @Override public Future<DescribeTableResult> describeTableAsync(DescribeTableRequest describeTableRequest)
        throws AmazonClientException
    {
        return asyncOp(this::describeTable, describeTableRequest);
    }

    @Override public Future<DescribeTableResult> describeTableAsync(String tableName) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<DescribeTableResult> describeTableAsync(DescribeTableRequest                                    describeTableRequest,
                                                                    AsyncHandler<DescribeTableRequest, DescribeTableResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::describeTable, describeTableRequest, asyncHandler);
    }

    @Override public Future<DescribeTableResult> describeTableAsync(String                                                  tableName,
                                                                    AsyncHandler<DescribeTableRequest, DescribeTableResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public ListTablesResult listTables()
        throws AmazonClientException
    {
        return new ListTablesResult().withTableNames(maps.keySet());
    }

    @Override public ListTablesResult listTables(ListTablesRequest listTablesRequest)
        throws AmazonClientException
    {
        return listTables();
    }

    @Override public ListTablesResult listTables(String exclusiveStartTableName)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public ListTablesResult listTables(Integer limit)
        throws AmazonClientException
    {
        return listTables();
    }

    @Override public ListTablesResult listTables(String exclusiveStartTableName, Integer limit)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync() {
        throw new UnsupportedOperationException();
    }
    @Override public Future<ListTablesResult> listTablesAsync(ListTablesRequest listTablesRequest)
        throws AmazonClientException
    {
        return asyncOp(this::listTables, listTablesRequest);
    }

    @Override public Future<ListTablesResult> listTablesAsync(AsyncHandler<ListTablesRequest, ListTablesResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync(String exclusiveStartTableName) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync(Integer limit) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync(ListTablesRequest                                 listTablesRequest,
                                                              AsyncHandler<ListTablesRequest, ListTablesResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::listTables, listTablesRequest, asyncHandler);
    }

    @Override public Future<ListTablesResult> listTablesAsync(String                                            exclusiveStartTableName,
                                                              AsyncHandler<ListTablesRequest, ListTablesResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync(String exclusiveStartTableName, Integer limit) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync(Integer limit, AsyncHandler<ListTablesRequest, ListTablesResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTablesResult> listTablesAsync(String exclusiveStartTableName, Integer limit,
                                                              AsyncHandler<ListTablesRequest, ListTablesResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public ListTagsOfResourceResult listTagsOfResource(ListTagsOfResourceRequest listTagsOfResourceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTagsOfResourceResult> listTagsOfResourceAsync(ListTagsOfResourceRequest listTagsOfResourceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ListTagsOfResourceResult> listTagsOfResourceAsync(
        ListTagsOfResourceRequest listTagsOfResourceRequest, AsyncHandler<ListTagsOfResourceRequest, ListTagsOfResourceResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public PutItemResult putItem(PutItemRequest putItemRequest)
        throws AmazonClientException
    {
        return putItem(putItemRequest.getTableName(), putItemRequest.getItem());
    }

    @Override public PutItemResult putItem(String tableName, Map<String, AttributeValue> item)
        throws AmazonClientException
    {
        final PutItemResult result = new PutItemResult();
        maps.get(tableName).put(solveKey(tableName, item), item);
        return result;
    }

    @Override public PutItemResult putItem(String tableName, Map<String, AttributeValue> item, String returnValues)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<PutItemResult> putItemAsync(PutItemRequest putItemRequest)
        throws AmazonClientException
    {
        return asyncOp(this::putItem, putItemRequest);
    }

    @Override public Future<PutItemResult> putItemAsync(PutItemRequest putItemRequest, AsyncHandler<PutItemRequest, PutItemResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::putItem, putItemRequest);
    }

    @Override public Future<PutItemResult> putItemAsync(String tableName, Map<String, AttributeValue> item) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<PutItemResult> putItemAsync(String tableName, Map<String, AttributeValue> item,
                                                        AsyncHandler<PutItemRequest, PutItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<PutItemResult> putItemAsync(String tableName, Map<String, AttributeValue> item, String returnValues) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<PutItemResult> putItemAsync(String tableName, Map<String, AttributeValue> item, String returnValues,
                                                        AsyncHandler<PutItemRequest, PutItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public QueryResult query(QueryRequest queryRequest)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<QueryResult> queryAsync(QueryRequest queryRequest)
        throws AmazonClientException
    {
        return asyncOp(this::query, queryRequest);
    }

    @Override public Future<QueryResult> queryAsync(QueryRequest queryRequest, AsyncHandler<QueryRequest, QueryResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::query, queryRequest, asyncHandler);
    }

    @Override public ScanResult scan(ScanRequest scanRequest)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public ScanResult scan(String tableName, List<String> attributesToGet)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public ScanResult scan(String tableName, Map<String, Condition> scanFilter)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public ScanResult scan(String tableName, List<String> attributesToGet, Map<String, Condition> scanFilter)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ScanResult> scanAsync(ScanRequest scanRequest)
        throws AmazonClientException
    {
        return asyncOp(this::scan, scanRequest);
    }

    @Override public Future<ScanResult> scanAsync(ScanRequest scanRequest, AsyncHandler<ScanRequest, ScanResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::scan, scanRequest, asyncHandler);
    }

    @Override public Future<ScanResult> scanAsync(String tableName, List<String> attributesToGet) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ScanResult> scanAsync(String tableName, Map<String, Condition> scanFilter) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ScanResult> scanAsync(String tableName, List<String> attributesToGet,
                                                  AsyncHandler<ScanRequest, ScanResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ScanResult> scanAsync(String tableName, Map<String, Condition> scanFilter,
                                                  AsyncHandler<ScanRequest, ScanResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ScanResult> scanAsync(String tableName, List<String> attributesToGet, Map<String, Condition> scanFilter) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<ScanResult> scanAsync(String tableName, List<String> attributesToGet, Map<String, Condition> scanFilter,
                                                  AsyncHandler<ScanRequest, ScanResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public void shutdown() {}

    @Override public TagResourceResult tagResource(TagResourceRequest tagResourceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<TagResourceResult> tagResourceAsync(TagResourceRequest tagResourceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<TagResourceResult> tagResourceAsync(TagResourceRequest                                  tagResourceRequest,
                                                                AsyncHandler<TagResourceRequest, TagResourceResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public UntagResourceResult untagResource(UntagResourceRequest untagResourceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UntagResourceResult> untagResourceAsync(UntagResourceRequest untagResourceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UntagResourceResult> untagResourceAsync(UntagResourceRequest                                    untagResourceRequest,
                                                                    AsyncHandler<UntagResourceRequest, UntagResourceResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public UpdateItemResult updateItem(UpdateItemRequest updateItemRequest)
        throws AmazonClientException
    {
        return updateItem(updateItemRequest.getTableName(), updateItemRequest.getKey(), updateItemRequest.getAttributeUpdates());
    }

    @Override public UpdateItemResult updateItem(String tableName, Map<String, AttributeValue> key,
                                                 Map<String, AttributeValueUpdate> attributeUpdates)
        throws AmazonClientException
    {
        final UpdateItemResult            result        = new UpdateItemResult();
        final Map<String, AttributeValue> currentValues = maps.get(tableName).get(solveKey(tableName, key));
        for (final Map.Entry<String, AttributeValueUpdate> o : attributeUpdates.entrySet())
            currentValues.put(o.getKey(), o.getValue().getValue());
        return result;
    }

    @Override public UpdateItemResult updateItem(String tableName, Map<String, AttributeValue> key,
                                                 Map<String, AttributeValueUpdate> attributeUpdates, String returnValues)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UpdateItemResult> updateItemAsync(UpdateItemRequest updateItemRequest)
        throws AmazonClientException
    {
        return asyncOp(this::updateItem, updateItemRequest);
    }

    @Override public Future<UpdateItemResult> updateItemAsync(UpdateItemRequest                                 updateItemRequest,
                                                              AsyncHandler<UpdateItemRequest, UpdateItemResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::updateItem, updateItemRequest, asyncHandler);
    }

    @Override public Future<UpdateItemResult> updateItemAsync(String tableName, Map<String, AttributeValue> key,
                                                              Map<String, AttributeValueUpdate> attributeUpdates) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UpdateItemResult> updateItemAsync(String tableName, Map<String, AttributeValue> key,
                                                              Map<String, AttributeValueUpdate> attributeUpdates,
                                                              AsyncHandler<UpdateItemRequest, UpdateItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UpdateItemResult> updateItemAsync(String tableName, Map<String, AttributeValue> key,
                                                              Map<String, AttributeValueUpdate> attributeUpdates, String returnValues) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UpdateItemResult> updateItemAsync(String tableName, Map<String, AttributeValue> key,
                                                              Map<String, AttributeValueUpdate> attributeUpdates, String returnValues,
                                                              AsyncHandler<UpdateItemRequest, UpdateItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public UpdateTableResult updateTable(UpdateTableRequest updateTableRequest)
        throws AmazonClientException
    {
        throw new UnsupportedOperationException();
    }

    @Override public UpdateTableResult updateTable(String tableName, ProvisionedThroughput provisionedThroughput)
        throws AmazonClientException
    {
        return new UpdateTableResult();
    }

    @Override public Future<UpdateTableResult> updateTableAsync(UpdateTableRequest updateTableRequest)
        throws AmazonClientException
    {
        return asyncOp(this::updateTable, updateTableRequest);
    }

    @Override public Future<UpdateTableResult> updateTableAsync(UpdateTableRequest                                  updateTableRequest,
                                                                AsyncHandler<UpdateTableRequest, UpdateTableResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::updateTable, updateTableRequest, asyncHandler);
    }

    @Override public Future<UpdateTableResult> updateTableAsync(String tableName, ProvisionedThroughput provisionedThroughput) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<UpdateTableResult> updateTableAsync(String tableName, ProvisionedThroughput provisionedThroughput,
                                                                AsyncHandler<UpdateTableRequest, UpdateTableResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public AmazonDynamoDBWaiters waiters() {
        throw new UnsupportedOperationException();
    }

    @Override public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override public GetItemResult getItem(GetItemRequest getItemRequest)
        throws AmazonClientException
    {
        final GetItemResult getItemResult = new GetItemResult();
        final String        tableName     = getItemRequest.getTableName();
        return getItemResult.withItem(maps.get(tableName).get(solveKey(tableName, getItemRequest.getKey())));
    }

    @Override public GetItemResult getItem(String tableName, Map<String, AttributeValue> key)
        throws AmazonClientException
    {
        return new GetItemResult().withItem(maps.get(tableName).get(solveKey(tableName, key)));
    }

    @Override public GetItemResult getItem(String tableName, Map<String, AttributeValue> key, Boolean consistentRead)
        throws AmazonClientException
    {
        return getItem(tableName, key);
    }

    @Override public Future<GetItemResult> getItemAsync(GetItemRequest getItemRequest)
        throws AmazonClientException
    {
        return asyncOp(this::getItem, getItemRequest);
    }

    @Override public Future<GetItemResult> getItemAsync(GetItemRequest getItemRequest, AsyncHandler<GetItemRequest, GetItemResult> asyncHandler)
        throws AmazonClientException
    {
        return asyncOp(this::getItem, getItemRequest, asyncHandler);
    }

    @Override public Future<GetItemResult> getItemAsync(String tableName, Map<String, AttributeValue> key) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<GetItemResult> getItemAsync(String tableName, Map<String, AttributeValue> key,
                                                        AsyncHandler<GetItemRequest, GetItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<GetItemResult> getItemAsync(String tableName, Map<String, AttributeValue> key, Boolean consistentRead) {
        throw new UnsupportedOperationException();
    }

    @Override public Future<GetItemResult> getItemAsync(String tableName, Map<String, AttributeValue> key, Boolean consistentRead,
                                                        AsyncHandler<GetItemRequest, GetItemResult> asyncHandler) {
        throw new UnsupportedOperationException();
    }

    private <T, S extends AmazonWebServiceRequest> Future<T> asyncOp(Function<S, T> op, S request) {
        return asyncOp(op, request, null);
    }
    private <T, S extends AmazonWebServiceRequest> Future<T> asyncOp(Function<S, T> op, S request, @Nullable AsyncHandler<S, T> handler) {
        T                     result = null;
        AmazonClientException ex     = null;

        try {
            result = op.apply(request);
            if (handler != null) handler.onSuccess(request, result);
        }
        catch (final AmazonClientException e) {
            ex = e;
        }
        return new CompletedFuture<>(result, ex);
    }

    private void batchWriteItem(Map.Entry<String, List<WriteRequest>> o) {
        for (final WriteRequest writeRequest : o.getValue()) {
            final String tableName = o.getKey();
            if (writeRequest.getDeleteRequest() != null) maps.get(tableName).remove(solveKey(tableName, writeRequest.getDeleteRequest().getKey()));
            else {
                final Map<String, AttributeValue> item = writeRequest.getPutRequest().getItem();
                final String                      key  = solveKey(tableName, item);
                maps.get(tableName).put(key, item);
            }
        }
    }  // end method batchWriteItem

    private String solveKey(String tableName, Map<String, AttributeValue> item) {
        final List<String> keys = mapKeys.get(tableName);
        return filter(item.keySet(), keys::contains)  //
               .foldLeft("", (s, att) -> s + item.get(att));
    }

    //~ Static Fields ................................................................................................................................

    public static final String TABLE          = "Table ";
    public static final String DOES_NOT_EXIST = " does not exist";

    //~ Inner Classes ................................................................................................................................

    public class CompletedFuture<T> implements Future<T> {
        private final Throwable re;
        private final T         v;

        /** Constructor. */
        public CompletedFuture(T v, Throwable re) {
            this.v  = v;
            this.re = re;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        public T get()
            throws ExecutionException
        {
            if (re != null) throw new ExecutionException(re);
            else return v;
        }

        public T get(long timeout, @NotNull TimeUnit unit)
            throws ExecutionException
        {
            return get();
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }
    }
}
