package com.example.ramcb.test

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ramcb.MainActivity
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

@OptIn(ExperimentalTestApi::class)
class KotlinSteps(
    val composeRuleHolder: ComposeRuleHolder, val activityScenarioHolder: ActivityScenarioHolder
) {

    @Given("I open the app")
    fun i_open_the_app() {
        activityScenarioHolder.launch(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext, MainActivity::class.java
            )
        )

        // waits until data is loaded
        composeRuleHolder.composeRule.waitUntilAtLeastOneExists(hasText("Rick Sanchez"), 5_000)
    }

    @Then("wait")
    fun _wait() {
        composeRuleHolder.composeRule.waitUntil(20_000) { false }
    }

    @Then("{string} is shown")
    fun character_is_shown(name: String) {
        composeRuleHolder.composeRule.waitUntilAtLeastOneExists(hasText(name), 5_000)
        composeRuleHolder.composeRule.onNodeWithText(name).assertIsDisplayed()
    }

    @Then("the character is a {string}")
    fun the_character_is_a(species: String) {
        composeRuleHolder.composeRule.onNodeWithText(species).assertIsDisplayed()
    }

    @When("I click on {string}")
    fun i_click_on_character(name: String) {
        composeRuleHolder.composeRule.onNodeWithText(name).performClick()
        composeRuleHolder.composeRule.waitForIdle()
    }

    @When("I scroll to {string}")
    fun i_scroll_to_character(name: String) {
        composeRuleHolder.composeRule.onNodeWithTag("characterPreviewList", useUnmergedTree = true).performScrollToNode(hasText(name))
        composeRuleHolder.composeRule.waitForIdle()
    }

    @When("I go back")
    fun i_go_back() {
        activityScenarioHolder.scenario?.onActivity { activity ->
            (activity as ComponentActivity).onBackPressedDispatcher.onBackPressed()
        }
    }
}