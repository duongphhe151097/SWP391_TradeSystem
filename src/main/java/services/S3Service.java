package services;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class S3Service {
    private static final String accessKey = "YS1LQQTRXWZOSGY94EUB";
    private static final String secretKey = "Lb5vwg0zFQDaSAyjRfxaSQGiYhBPlnh9YnfG9gCA";
    private static final String bucketName = "trade-sys";
    private static final String serviceEndpoint = "https://s3.cloudfly.vn";

    public static AmazonS3 createConnection()
            throws SdkClientException {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, ""))
                .withPathStyleAccessEnabled(true)
                .build();

        return amazonS3;
    }

    public static void upload(String fileName, InputStream fileStream) throws IOException {
        AmazonS3 s3Client = createConnection();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileStream.available());
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                fileName,
                fileStream,
                metadata
        );

        s3Client.putObject(putObjectRequest);
    }

    public static String getUrl(String fileName){
        AmazonS3 s3Client = createConnection();
        Date exp = new Date(System.currentTimeMillis() + 3600000);
        URL url = s3Client.generatePresignedUrl(bucketName, fileName, exp);

        return url.toString();
    }
}
