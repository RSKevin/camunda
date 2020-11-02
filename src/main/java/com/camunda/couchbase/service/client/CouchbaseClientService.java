package com.camunda.couchbase.service.client;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.couchbase.config.BeanNames;

import com.camunda.couchbase.config.CouchbaseConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

@Configuration
@EnableConfigurationProperties
public class CouchbaseClientService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Resource
    private CouchbaseConfig couchbaseConfig;

    @Resource
    private org.springframework.core.env.Environment environment;

    public CouchbaseClientService() {
    }

    //@Bean(destroyMethod = "close", name = BeanNames.COUCHBASE_BUCKET)
    public Bucket getCouchbaseBucket() throws Exception {

        String[] tmpActiveProfiles = environment.getActiveProfiles();
        log.info("Active profile: {}", tmpActiveProfiles);

        /*if(log.isTraceEnabled())
        {
            log.trace("--------------------------getCouchbaseBucket() starts------------------------------");
            final MutablePropertySources sources = ((AbstractEnvironment) environment).getPropertySources();
            StreamSupport.stream(sources.spliterator(), false)
                    .filter(ps -> ps instanceof EnumerablePropertySource)
                    .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                    .flatMap(Arrays::stream)
                    .distinct()
                    .filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
                    .forEach(prop -> log.trace("{}: {}", prop, environment.getProperty(prop)));
            log.trace("--------------------------getCouchbaseBucket() ends------------------------------");
        }
        log.info("{} called", method);*/

        //Cluster cluster = couchbaseCluster();rt
        //TODO: put the cluster in yaml
        log.info("Creating cluster with hosts: {}", couchbaseConfig.getServers());
        Cluster cluster = CouchbaseCluster.create(couchbaseConfig.getServers());

        log.info("Created cluster {}", cluster);

        log.info("Authorizing using {}", couchbaseConfig.getCouchbaseUsername());

        if(!couchbaseConfig.getCouchbaseUsername().contentEquals(couchbaseConfig.getBucket())){
            cluster.authenticate(couchbaseConfig.getCouchbaseUsername(), couchbaseConfig.getCouchbasePassword());
        } else if (!couchbaseConfig.getCouchbasePassword().isEmpty()) {
            return cluster.openBucket(couchbaseConfig.getBucket(), couchbaseConfig.getCouchbasePassword());
        }
        log.info("Authorization completed... Creating bucket");
        Bucket bucket = cluster.openBucket(couchbaseConfig.getBucket(),600l, TimeUnit.SECONDS);
        //Bucket bucket = cluster.openBucket(couchbaseConfig.getBucket());

        log.info("Bucket {} created", bucket);
        return bucket;
    }

    public void closeBucket(Bucket bucket) {
        if (bucket != null)
            bucket.close();
    }

}