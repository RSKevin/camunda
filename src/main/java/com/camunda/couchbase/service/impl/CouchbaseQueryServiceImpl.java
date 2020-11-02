package com.camunda.couchbase.service.impl;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camunda.couchbase.service.CouchbaseQueryService;
import com.camunda.couchbase.service.client.CouchbaseClientService;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.Statement;

@Service
public class CouchbaseQueryServiceImpl implements CouchbaseQueryService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    CouchbaseClientService couchbaseClientService;

	@Override
	public Map<String, Object> getByKey(String key) {
		Bucket bucket = null;
        N1qlQueryResult result = null;
        Map<String, Object> responseData = null;
        try {
            bucket = couchbaseClientService.getCouchbaseBucket();
            if (bucket != null) {
                log.info("Calling query service");
                result = bucket.query(N1qlQuery.simple("SELECT meta().id, equitypm.* FROM `" + bucket.name() + "` equitypm USE KEYS '" + key + "'"));
            }
        } catch (Exception e) {
            log.error("Unable to get couchbase bucket via ClientService: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        	couchbaseClientService.closeBucket(bucket);
        }
        if (result != null && !result.allRows().isEmpty())
            responseData = result.allRows().get(0).value().toMap();
        return responseData;
	}
	
	@Override
	public List<Map<String, Object>> getByType(String type) {
		log.info("In getByType");
		Bucket bucket = null;
        N1qlQueryResult result = null;
        List<Map<String, Object>> responseData = null;
        try {
            bucket = couchbaseClientService.getCouchbaseBucket();
            if (bucket != null) {
                log.info("Calling query service");
                Statement statement = select(x("meta().id, equitypm.*"))
                        .from(i(bucket.name())).as("equitypm")
                        .where( x("equitypm.type").eq(s(type)));
                result = bucket.query(N1qlQuery.simple(statement));
            }
        } catch (Exception e) {
            log.error("Unable to get couchbase bucket via ClientService: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        	couchbaseClientService.closeBucket(bucket);
        }
        if (result != null) {
            responseData = result.allRows()
                    .stream()
                    .map(i -> i.value().toMap())
                    .collect(Collectors.toList());
        }
        return responseData;
	}
	
	@Override
	public Map<String, Object> upsert(Map<String, Object> payload) {
		Map<String, Object> responseData = new HashMap<>();
		JsonObject content = JsonObject.from(payload);
		if (content.containsKey("id")) {
			Bucket bucket = null;
			try {
				bucket = couchbaseClientService.getCouchbaseBucket();
				if (bucket != null) {
					log.info("Calling upsert service");
					JsonDocument inserted = bucket.upsert(JsonDocument.create((String) content.get("id"), content.removeKey("id")));
					if (inserted != null) {
						responseData.putAll(inserted.content().toMap());
						responseData.put("id", inserted.id());
					}
				}
			} catch (Exception e) {
				log.error("Unable to upsert the data due to {}", e.getMessage());
				throw new RuntimeException(e);
			} finally {
				couchbaseClientService.closeBucket(bucket);
			}
		} else
			log.error("Unable to upsert the data as required key 'id' is missing");
		return responseData;
	}
	
	@Override
	public Map<String, Object> insert(Map<String, Object> payload) {
		Map<String, Object> responseData = new HashMap<>();
		Bucket bucket = null;
		JsonObject content = JsonObject.from(payload);
		String key;
        if (content.containsKey("id")) {
        	key = (String) content.get("id");
        	content.removeKey("id");
        } else if (content.containsKey("uniqueKey")) {
        	key = content.get("type").toString().concat("::").concat((String) content.get("uniqueKey"));
        	content.removeKey("uniqueKey");
        } else
        	key = content.get("type").toString().concat("::").concat(String.valueOf(Instant.now().getEpochSecond()));
        log.info("Document key {}", key);
		try {
            bucket = couchbaseClientService.getCouchbaseBucket();
            if (bucket != null) {
                log.info("Calling upsert service");
                JsonDocument inserted = bucket.upsert(JsonDocument.create(key, content));
                if (inserted != null) {
                	responseData.putAll(inserted.content().toMap());
                	responseData.put("id", inserted.id());
                }
            }
        } catch (Exception e) {
            log.error("Unable to upsert the data due to {}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
        	couchbaseClientService.closeBucket(bucket);
        }
		return responseData;
	}

	@Override
	public Map<String, Object> bulkInsert(List<Map<String, Object>> payload) {
		Map<String, Object> responseData = new HashMap<>();
		List<JsonDocument> documents = new ArrayList<JsonDocument>();
		List<String> keys = new ArrayList<>();
		payload.forEach(item -> {
			JsonObject content = JsonObject.from(item);
			String key;
			if (content.containsKey("id")) {
				key = (String) content.get("id");
				content.removeKey("id");
			} else if (content.containsKey("uniqueKey")) {
				key = content.get("type").toString().concat("::").concat((String) content.get("uniqueKey"));
				content.removeKey("uniqueKey");
			} else
				key = content.get("type").toString().concat("::")
						.concat(String.valueOf(Instant.now().getEpochSecond()));
			documents.add(JsonDocument.create(key, content));
		});

		try {
			Bucket bucket = couchbaseClientService.getCouchbaseBucket();
			if (bucket != null) {
				log.info("Calling insert service");
				documents.forEach(document -> {
					JsonDocument inserted = bucket.upsert(document);
					if (inserted != null) {
						keys.add(inserted.id());
					}
				});
				couchbaseClientService.closeBucket(bucket);
			}
		} catch (Exception e) {
			log.error("Unable to insert the data due to {}", e.getMessage());
			throw new RuntimeException(e);
		}
		responseData.put("documentKeys", keys);
		return responseData;
	}

	@Override
	public List<Map<String, Object>> selectQuery(String fields) {
		Bucket bucket = null;
        N1qlQueryResult result = null;
        List<Map<String, Object>> responseData = null;
        try {
            bucket = couchbaseClientService.getCouchbaseBucket();
            if (bucket != null) {
                log.info("Calling query service");
                result = bucket.query(N1qlQuery.simple("SELECT " + fields + " FROM `" + bucket.name() + "`"));
            }
        } catch (Exception e) {
            log.error("Unable to get couchbase bucket via ClientService: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        	couchbaseClientService.closeBucket(bucket);
        }

        if (result != null) {
            responseData = result.allRows()
                    .stream()
                    .map(i -> i.value().toMap())
                    .collect(Collectors.toList());
        }
        return responseData;
	}
	
	@Override
	public List<Map<String, Object>> selectQuery(String fields, String condition) {
		Bucket bucket = null;
        N1qlQueryResult result = null;
        List<Map<String, Object>> responseData = null;
        try {
            bucket = couchbaseClientService.getCouchbaseBucket();
            if (bucket != null) {
                log.info("Calling query service");
                result = bucket.query(N1qlQuery.simple("SELECT " + fields + " FROM `" + bucket.name() + "` WHERE " + condition));
            }
        } catch (Exception e) {
            log.error("Unable to get couchbase bucket via ClientService: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        	couchbaseClientService.closeBucket(bucket);
        }

        if (result != null) {
            responseData = result.allRows()
                    .stream()
                    .map(i -> i.value().toMap())
                    .collect(Collectors.toList());
        }
        return responseData;
	}
		
	@Override
    public List<Map<String, Object>> selectQuery(Statement statement) {
		log.info("In selectQuery for {}", statement);
		Bucket bucket = null;
        N1qlQueryResult result = null;
        List<Map<String, Object>> responseData = null;
        try {
            bucket = couchbaseClientService.getCouchbaseBucket();
            if (bucket != null) {
                log.info("Calling query service");
                result = bucket.query(N1qlQuery.simple(statement));
            }
        } catch (Exception e) {
            log.error("Unable to get couchbase bucket via ClientService: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        	couchbaseClientService.closeBucket(bucket);
        }

        if (result != null) {
            responseData = result.allRows()
                    .stream()
                    .map(i -> i.value().toMap())
                    .collect(Collectors.toList());
        }
        return responseData;
	}
	
	@Override
	public Map<String, Object> update(Map<String, Object> payload) {
		log.info("Calling update");
		Map<String, Object> responseData = new HashMap<>();
		JsonObject content = JsonObject.from(payload);
		if (content.containsKey("id")) {
			Bucket bucket = null;
			try {
				bucket = couchbaseClientService.getCouchbaseBucket();
				if (bucket != null) {
					log.info("Calling update service");
					JsonDocument updated = bucket.append(JsonDocument.create((String) content.get("id"), content.removeKey("id")));
					if (updated != null) {
						responseData.putAll(updated.content().toMap());
						responseData.put("id", updated.id());
					}
				}
			} catch (Exception e) {
				log.error("Unable to update the data due to {}", e.getMessage());
				throw new RuntimeException(e);
			} finally {
				couchbaseClientService.closeBucket(bucket);
			}
		} else
			log.error("Unable to update the data as required key 'id' is missing");
		return responseData;
	}

	@Override
	public Map<String, Object> updateByKey(String fields, String key, String retuningFields) {
		Bucket bucket = null;
		N1qlQueryResult result = null;
		Map<String, Object> responseData = null;
		try {
			bucket = couchbaseClientService.getCouchbaseBucket();
			if (bucket != null) {
				log.info("Calling update query service {}, {}", fields, key);
				//String queryString = "UPDATE `" + bucket.name() + "` SET " + fields + " WHERE meta().id = '" + key + "'";
				//log.info("queryString {}", queryString);
				//result = bucket.query(N1qlQuery.simple("UPDATE `" + bucket.name() + "` SET " + fields + " WHERE meta().id = '" + key + "'"));
				result = bucket.query(N1qlQuery.simple("UPDATE `" + bucket.name() + "` USE KEYS '" + key + "' SET " + fields + " RETURNING " + retuningFields));
			}
		} catch (Exception e) {
			log.error("Unable to update due to {}", e);
			throw new RuntimeException(e);
		} finally {
			couchbaseClientService.closeBucket(bucket);
		}
		if (result != null && !result.allRows().isEmpty())
			responseData = result.allRows().get(0).value().toMap();
		return responseData;
	}

}