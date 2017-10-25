package com.pressassociation.qa.stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;


public class StepDefinitions {
    private Response response;
    private ValidatableResponse json;

    String artistName;
    String songName;
    String publishDate;
    String videoId;
    String descName;
    String titleName;

    private String baseUri = "http://turing.niallbunting.com:3002/api/";

    /**
     * create new song entry or new new playlist
     *
     * @param endPoint
     * @param table
     */

    @Given("^a user hit (.*) with following inputs$")
    public void user_hit_post_call(String endPoint, List<SongDetails> table) {
        String body = null;
        SongDetails songDetails = table.get(0);

        if (endPoint.contains("video")) {
            artistName = songDetails.artist + "_" + appendDate();
            songName = songDetails.song + "_" + appendDate();
            publishDate = getCurrentDate();
            body = "{ \"artist\": \"" + artistName + "\", \"song\": \"" + songName + "\", \"publishDate\": \"" + publishDate + "\" }";

        } else {
            descName = songDetails.desc + "_" + appendDate();
            titleName = songDetails.title + "_" + appendDate();
            body = "{\"desc\": \"" + descName + "\", \"title\": \"" + titleName + "\"}";
        }

        System.out.print(baseUri + endPoint);

        response = given()
                .contentType("application/json").
                        body(body).
                        when().
                        post(baseUri + endPoint);
        response.prettyPrint();
        videoId = response.path("_id").toString();
    }

    /**
     * Verify the status
     *
     * @param statusCode
     */
    @Then("^the status code is (\\d+)$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    /**
     * Verify the give key & value pair on the json respone
     *
     * @param key1
     * @param key2
     * @param endPoint
     */

    @And("^verify the (.*) and (.*) value in the respone of (.*)$")
    public void verify_the_respone(String key1, String key2, String endPoint) {
        if (endPoint.equals("video")) {
            json.body(key1, equalTo(artistName));
            json.body(key2, equalTo(songName));
        } else {
            json.body(key1, equalTo(descName));
            json.body(key2, equalTo(titleName));
        }
    }

    /**
     * Verify the respones of list of values
     *
     * @param endPoint
     */

    @And("verify the list of (.*)")
    public void verify_the_list(String endPoint) {
        response = when().get(baseUri + endPoint);
        json = response.then().statusCode(200);
    }

    /**
     * Verify video / playlist is created
     *
     * @param endPoint
     */

    @Then("^verify user able to get the created (.*) using id$")
    public void verify_the_created_video(String endPoint) {

        System.out.print(baseUri + endPoint + "/" + videoId);
        response = when().get(baseUri + endPoint + "/" + videoId);
        json = response.then().statusCode(200);

    }

    /**
     * delete the video/playlist and verify it
     *
     * @param endPoint
     * @param statusCode
     * @throws Throwable
     */

    @Then("^delete the created (.*) and verify status code is (\\d+)")
    public void delete_the_created_song(String endPoint, int statusCode) throws Throwable {
        System.out.print(baseUri + endPoint + "/" + videoId);
        response = when().delete(baseUri + endPoint + "/" + videoId);
        json = response.then().statusCode(statusCode);
    }

    /**
     * Verify the deleted items
     *
     * @param endPoint
     * @throws Throwable
     */

    @Then("^verify the deleted (.*)")
    public void verify_the_deleted_song(String endPoint) throws Throwable {
        System.out.print(baseUri + endPoint + "/" + videoId);
        response = when().get(baseUri + endPoint + "/" + videoId);
        String rs = response.prettyPrint();
        Assert.assertTrue(rs.equals("null"));

    }

    /**
     * Get the current date with yyyy-MM-dd formate
     *
     * @return current date
     */

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }

    /**
     * Get the current date with yyyy-MM-dd formate
     *
     * @return current date
     */

    public String appendDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }

    /**
     * Song details
     */
    private class SongDetails {
        public String artist;
        public String song;
        public String desc;
        public String title;
    }
}
