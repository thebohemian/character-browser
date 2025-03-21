package com.example.ramcb.test

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith


@RunWith(Cucumber::class)
@CucumberOptions(features = ["features"], tags = "not @ignore")
class CucumberRunner