# JC - Cucumber with Regular JUnit Tests

**Project is still under construction...**

### Keep your junit tests. use BDD methodology.
- JC allows to annotate regular junit tests using Gherkin syntax.  
- Then, it can optionally validate those annotations against a feature file.  

### JS helps to Integrate with ALM Octane
- It also creates a report that allows uploading results to ALM Octane as Scenario Tests.

### JC upgrades your error messages
- JC generates clear error message: it writes all the steps up to the failure so you have context
- When feature file changes, get a detailed error on which annotation is changed

### JC builds test skeletons from a feature file
- When you want to automate a new feature file, JS can build the skeleton of all of your tests in a breeze.
- Just point to the feature file and run -
- JC will write the whole class with annotations inside the error message 

### JC is extensible 
- You can create your own plugin that collects information during the run and print report at the end

### Example:

```java
@FeatureFileAt("/features/lavaSoft.feature")
@Feature("My LavaSoft specification and use cases")
public class MyLavaSoftTests {

  // your typical JUnit class with 1 test

  @Test
  public void test1() {
    // test is annotated with scenario name and steps description  
    scenario("First scenario for LavaSoft", () -> {
      given("I use JC");
      // automation code here...

      when("I decorate my tests with given, when, then");
      // automation code here...

      then("I get a Gherkin report at the end");
      // automation code here...
    });
  }
  
  @AfterClass
  public static void afterClass() {
    // need to call finished() to print report at end
    finished();
  }
}
```

### How to use
In your junit file:
1. Add @FeatureFileAt() annotation to point to the feature file
2. Add @Feature annotation which describe the feature title
3. Annotate your tests with scenario, given, when, then as in the example
4. DON'T FORGET to call to finished() at the end of all tests.   
   This will generate the report / validation of the feature file structure


