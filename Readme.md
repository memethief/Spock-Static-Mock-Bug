# Static mock bug

Given two unrelated classes, each with a static method with the same name, stubbing both methods in the same Spock test fails to behave as expected. Instead, the stub defined first overrides the second.

