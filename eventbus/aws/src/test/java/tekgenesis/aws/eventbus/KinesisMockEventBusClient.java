
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import java.io.IOException;
import java.net.UnknownHostException;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.metrics.impl.NullMetricsFactory;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.ResourceInUseException;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.Environment;
import tekgenesis.common.logging.Logger;

import static com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream.TRIM_HORIZON;
import static com.amazonaws.services.kinesis.metrics.interfaces.MetricsLevel.NONE;

/**
 * Mock implementation pointing to a local mock kinesis (kinesislite).
 */
public class KinesisMockEventBusClient<T> extends KinesisEventBusClient<T> {

    //~ Instance Fields ..............................................................................................................................

    private AmazonDynamoDB dynamoClient = null;

    //~ Constructors .................................................................................................................................

    /**
     * Default constructor.
     *
     * @param  env
     */
    @SuppressWarnings("WeakerAccess")
    public KinesisMockEventBusClient(@NotNull Environment env) {
        super(env);
        final EndpointConfiguration configuration = new EndpointConfiguration("http://localhost:7011", "");
        dynamoClient = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(configuration).build();
    }

    //~ Methods ......................................................................................................................................

    @Override public void close()
        throws IOException
    {
        super.close();
        if (dynamoClient != null) dynamoClient.shutdown();
    }

    AmazonKinesis createConsumer() {
        final EndpointConfiguration configuration = new EndpointConfiguration("http://localhost:7010", "");
        return AmazonKinesisClientBuilder.standard().withEndpointConfiguration(configuration).build();
    }

    KinesisProducer createProducer() {
        final ProfileCredentialsProvider   provider = new ProfileCredentialsProvider("default");
        final KinesisProducerConfiguration conf     = new KinesisProducerConfiguration().setCredentialsProvider(provider)
                                                      .setKinesisEndpoint("localhost")
                                                      .setKinesisPort(7010)
                                                      .setVerifyCertificate(false)
                                                      .setRegion("us-east-1");
        return new KinesisProducer(conf);
    }

    void createTopic(String name) {
        try {
            kinesisClient.createStream(name, SHARD_COUNT);
        }
        catch (final ResourceInUseException e) {
            // ignore
        }

        final DescribeStreamResult describeStreamResult = kinesisClient.describeStream(name);
        while (!describeStreamResult.getStreamDescription().getStreamStatus().equals(STREAM_ACTIVE))
            logger.debug("Waiting for stream status ACTIVE");
    }

    Worker createWorker(@NotNull String topic, ConsumerContext<T> consumerContext)
        throws UnknownHostException
    {
        final IRecordProcessorFactory processorFactory = EventProcessFactory.create(consumerContext);

        final KinesisClientLibConfiguration config = kinesis.createConfiguration(topic)
                                                     .withMetricsLevel(NONE)
                                                     .withInitialPositionInStream(TRIM_HORIZON);
        return new Worker.Builder().recordProcessorFactory(processorFactory)
               .config(config)
               .dynamoDBClient(dynamoClient)
               .kinesisClient(kinesisClient)
               .metricsFactory(new NullMetricsFactory())
               .build();
    }

    void deleteTopic(String name) {
        kinesisClient.deleteStream(name);
    }

    //~ Static Fields ................................................................................................................................

    private static final String STREAM_ACTIVE = "ACTIVE";
    private static final int    SHARD_COUNT   = 1;

    @NotNull private static final Logger logger = Logger.getLogger(KinesisMockEventBusClient.class);
}  // end class KinesisMockEventBusClient
