# JC
Cucumber Library for UI Automation

Project is still under construction...

JC is a light weight library for writing cucumber tests without the borden of changing your test structure.
You write your tests in plain junit, and use JC to describe your scenarios / steps like this:

// your typical JUnit class
public class MyCucumberTest {

    // just add JC to your class
    public static JC jc = new JC(
            MyCucumberTest.class, 
            ".\gherkin.feature", // link to youe feature file 
            "This is your feature file title");


    @Test
    public void test1() {
        jc.scenario("This is the first scenario", ()->{
            jc.given( "I use JC");
            // automation code here...
            
            jc.when("I log my steps");
            // automation code here...

            jc.then("I get a Gherkin report at the end");
            // automation code here...
        });
    }

    @Test
    public void test2() {
        jc.scenarioOutline("this will run for each row in the feature file Examples", ()->{
            jc.given("I make a mistake in the step title named <p1>");
            // now you can use the parameter as jc["p1"]
            
            jc.when("I run the junit test");
            
            jc.then("it will compare the steps to the feature file defined above and tell you that <p1> is different");
            
        });
    }

    @AfterClass
    public static void afterClass() {
        // this will print the gherkin report
        jc.finished();
    }
}

##You get the following benefits:
- A test that is easy to read and understand

- Turn your tests into BDD without changing their entire structure

- A validation that your steps/scenarios are identical to the steps/scenarios in the feature file.

- Error messages with context - displays all the steps until the error, so you understand what happend

- Have a feature file without code? 
  just instantiate the JC class, link it to the feature file and run -> 
  you will get an error that contains the implementation of the class. Copy. Enjoy.
  
- JUnit report describing your Gherkin syntax

- Extensible reporting. You can add other kinds of reports such as HTML.



