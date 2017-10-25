Feature: Validate the web service of daily song

  Scenario: CURD operation for Song web service
    Given a user hit video with following inputs
      | artist   | song     |
      | testName | testSong |
    Then the status code is 201
    And verify the artist and song value in the respone of video
    And verify the list of video
    Then verify user able to get the created video using id
    Then delete the created video and verify status code is 204
    And verify the deleted video


  Scenario: CURD operation for playlist web service
    Given a user hit playlist with following inputs
      | desc       | title  |
      | Myplaylist | MyList |
    Then the status code is 201
    And verify the desc and title value in the respone of playlist
    And verify the list of playlist
    Then verify user able to get the created playlist using id
    Then delete the created playlist and verify status code is 204
