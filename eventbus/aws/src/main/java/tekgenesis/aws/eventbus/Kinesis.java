
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;

/**
 * Kinesis.
 */
final class Kinesis {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final KinesisProps props;

    //~ Constructors .................................................................................................................................

    Kinesis(@NotNull KinesisProps p) {
        props = p;
    }

    //~ Methods ......................................................................................................................................

    @NotNull KinesisClientLibConfiguration createConfiguration(@NotNull String topic)
        throws UnknownHostException
    {
        final String workId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();

        final AWSCredentialsProvider provider = getAwsCredentialsProvider();

        final KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(props.appName + "-" + topic, topic, provider, workId);

        config.withFailoverTimeMillis(props.failoverTimemillis)
            .withMaxRecords(props.maxRecords)
            .withIdleTimeBetweenReadsInMillis(props.idleTimeBetweenReads)
            .withShardSyncIntervalMillis(props.shardSyncIntervalMillis)
            .withParentShardPollIntervalMillis(props.parentShardPollIntervalMillis);

        return config;
    }

    AmazonKinesis createConsumer() {
        final AWSCredentialsProvider provider = getAwsCredentialsProvider();
        return AmazonKinesisClientBuilder.standard().withCredentials(provider).build();
    }

    KinesisProducer createProducer() {
        final AWSCredentialsProvider       credentialsProvider = getAwsCredentialsProvider();
        final KinesisProducerConfiguration conf                = new KinesisProducerConfiguration();
        conf.setCredentialsProvider(credentialsProvider);
        conf.setRegion(props.region);
        return new KinesisProducer(conf);
    }

    <T> Worker createWorker(String topic, ConsumerContext<T> context)
        throws UnknownHostException
    {
        final IRecordProcessorFactory       processorFactory = EventProcessFactory.create(context);
        final KinesisClientLibConfiguration config           = createConfiguration(topic);

        return new Worker.Builder().recordProcessorFactory(processorFactory).config(config).build();
    }

    @NotNull private AWSCredentialsProvider getAwsCredentialsProvider() {
        final AWSCredentialsProvider provider;
        if (Predefined.isEmpty(props.profileName)) provider = new EC2ContainerCredentialsProviderWrapper();
        else provider = new ProfileCredentialsProvider(props.profileName);
        return provider;
    }
}  // end class Kinesis
