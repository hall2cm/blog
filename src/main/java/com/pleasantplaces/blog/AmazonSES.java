package com.pleasantplaces.blog;

/**
 * Created by hallfamilymac on 6/3/17.
 */

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;



import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class AmazonSES implements AmazonService {

    private static String FROM = "Pleasant Places Blog <pleasantplacesblog@gmail.com>";  // Replace with your "From" address. This address must be verified.
    private static String TO = "hall2cm@gmail.com"; // Replace with a "To" address. If your account is still in the
    // sandbox, this address must be verified.
    private static String BODY;// = "This email was sent through Amazon SES by using the AWS SDK for Java.";
    private static String SUBJECT;// = "Amazon SES test (AWS SDK for Java)";
    private static String displayName;
    private static String postTitle;
    private static String postText;


    Map<String, String> valuesMap = new HashMap<>();

    public void setName(String name) {
        this.displayName = name;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setSubject(String subject) {
        this.SUBJECT = subject;
    }

    public void setToEmail(String toEmail) {
        this.TO = toEmail;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }




    public void readHtmlFile(String fileName, Long id) {

        String blogPostId = id.toString();
        URL url = Resources.getResource(fileName);

        if (fileName == "comment-email.html") {
            valuesMap.put("blogPostId", blogPostId);
        } else {
            valuesMap.put("blogPostId", blogPostId);
            valuesMap.put("displayName", this.displayName);
            valuesMap.put("postTitle", this.postTitle);
            valuesMap.put("postText", this.postText);
        }

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        try {
            String html = Resources.toString(url, Charsets.UTF_8);
            this.BODY = sub.replace(html);
        } catch (IOException e) {
            System.out.println("Error occurred while formatting email message.");
            System.out.println("Error Message" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Async("workExecutor")
    public void sendEmail() throws IOException, InterruptedException {
        //Thread.sleep(500);

        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[]{TO}).withBccAddresses("pleasantplacesblog@gmail.com");

        // Create the subject and body of the message.
        Content subject = new Content().withData(SUBJECT);
        Content textBody = new Content().withData(BODY);
        Body body = new Body().withHtml(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);

        try
        {
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials.
            // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials
            // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables
            // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY.
            // For more information, see http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1).build();

            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your sandbox
            // status, sending limits, and Amazon SES identity-related settings are specific to a given AWS
            // region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
            // the US West (Oregon) region. Examples of other regions that Amazon SES supports are US_EAST_1
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
            // Send the email.

            client.sendEmail(request);
            System.out.println("Email sent!");
        }
        catch (Exception ex)
        {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
    }
}
