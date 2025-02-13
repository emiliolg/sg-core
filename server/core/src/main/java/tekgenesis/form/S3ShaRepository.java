
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.handler.Routes;

/**
 * S3 backed repository for resources by sha.
 */
public class S3ShaRepository implements ShaRepository {

    //~ Instance Fields ..............................................................................................................................

    private final String bucket;
    private AmazonS3     client = null;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public S3ShaRepository(String awsAccessKey, String awsSecretKey, String awsBucket) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        bucket = awsBucket;
    }

    //~ Methods ......................................................................................................................................

    @Override public InputStream get(String path, String sha) {
        try {
            return client.getObject(new GetObjectRequest(bucket, SHA_RESOURCES + Routes.normalize(path) + "/" + sha)).getObjectContent();
        }
        catch (final AmazonClientException e) {
            logger.warning(e);
            return null;
        }
    }

    @Override public void put(String path, String sha, byte[] bytes) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        client.putObject(bucket, SHA_RESOURCES + Routes.normalize(path) + "/" + sha, new ByteArrayInputStream(bytes), metadata);
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(S3ShaRepository.class);

    public static final String SHA_RESOURCES = "sha-resources";
}  // end class S3ShaRepository
