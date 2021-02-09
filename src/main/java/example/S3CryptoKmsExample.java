package example;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class S3CryptoKmsExample {

    public static final String BUCKET_NAME = "your-bucket-name-goes-here";
    public static final String OBJECT_KEY = "my-file-name.txt";
    public static final String OBJECT_CONTENT = "This text file should be encrypted";

    public static final String AWS_PROFILE_NAME = "AWSprofileName";

    public static final String KMS_KEY_ID = "your-kms-key-id";

    public S3CryptoKmsExample() throws NoSuchAlgorithmException {

        // Make sure your AWS credential profile is created and located at ~/.aws/credentials
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(AWS_PROFILE_NAME);

        // Build our S3 encryption client
        AmazonS3EncryptionV2 s3Crypto = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(credentialsProvider)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterialsProvider(new KMSEncryptionMaterialsProvider(KMS_KEY_ID))
                .build();

        // Encrypt and store our object
        s3Crypto.putObject(BUCKET_NAME, OBJECT_KEY, OBJECT_CONTENT);

        // Retrieve our object
        System.out.println(s3Crypto.getObjectAsString(BUCKET_NAME, OBJECT_KEY));

        // Close our connection
        s3Crypto.shutdown();
    }

}
