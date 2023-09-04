package com.hemendra.facebookintegration;



import com.restfb.*;
import com.restfb.types.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class FBJavaSDKTest {

    //https://www.youtube.com/watch?v=s8c2SMpWDOo

    //Get all the post in the page:
    // 113526811848270_122105522480023686?fields=likes,comments ---> facebook get likes and comments of a post published in a page.

    /***
     * Learings in Posting a Comment in Facebook using Graphh API
     *
     * Your app need to be live.
     * To make the app live you need to have a privacy policy
     *  So to do so I just generated a privacy policy from a website free of cost
     *  Then in Basic Settings of the page I added the privacy policy URL (which I had copied from privacy policy generator website)
     *  Then using the Graph API I created a post.
     *
     *  It is visible to all.
     */

    //Permissions Needed for getting the details from Facebook
    /**
     *
     * user_posts
     * pages_show_list
     * pages_read_engagement
     * pages_read_user_content
     * pages_manage_posts
     * public_profile
     *
     *
     */

    private static final Logger log = LoggerFactory.getLogger(FBJavaSDKTest.class);



    String pageId = "113526811848270"; //113526811848270
    String pageAccessToken
            = "EAARqZAoOZCPBABO07bYd4qGnUflOf9ZCsrI8bRAUgpIZAqdKr02AlfwRFmV2Ir5nroMCFnv8gZABPKieHczaZC0qKKgl0Cxmv2LqEbFuCmnJ8EYXa0ZAtao46bxuQJZB7ea92ZCwc8ZBnQfx0U9ZAHOQVk9nxzz6TnspFK92POPvRgEEq0JRfbLgBKUL5ZBCLnh2vZCepUKIYHfmdYCU0ByuQNFoZAafKQ2V7vHTZCvLRhb";

    @Test
    public void testSDK() {
        FacebookClient facebookClient = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
        FacebookType response = facebookClient.publish(
                pageId + "/feed",
                FacebookType.class,
                Parameter.with("message", "Hello, I added a Privacy Policy In  Facebook App to make the post visible to public from Spring boot!") // Your post content
        );

        log.info("Posted on Facebook Page: {}", response.getId());
    }

    @Test
    public void getLikesAndComments() {
        //https://stackoverflow.com/questions/60485995/posting-on-my-facebook-page-using-facebook-graph-api-not-showing-in-public#:~:text=1%20Answer&text=Posts%20made%20with%20the%20API,is%20for%20personal%20use%20only.&text=sure%20you%20can%2C%20but%20the%20app%20needs%20to%20be%20public.
        String postId = "113526811848270_122105571866023686"; //It is the combination of (pageId + actualPostId)
        FacebookClient client = new DefaultFacebookClient(pageAccessToken, Version.LATEST);
        Connection<Comment> comments = client.fetchConnection(postId + "/comments", Comment.class);
        final List<Comment> data = comments.getData();
        readComments(data);
    }

    private static void readComments(List<Comment> data) {
        for (Comment comment : data) {
            if (Objects.nonNull(comment.getFrom())) {
                User from = comment.getFrom().getAsUser();
                log.info("Comment :{}, made by : {}", comment.getMessage(), from.getName());
            } else {
                log.info("Comment :{}, made by : {}", comment.getMessage(), comment.getFrom());
            }

            if(Objects.nonNull(comment.getComments())) {
                readComments(comment.getComments().getData());
            }
        }
    }
}
